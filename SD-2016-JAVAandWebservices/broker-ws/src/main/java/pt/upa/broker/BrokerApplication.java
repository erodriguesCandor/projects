package pt.upa.broker;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;


import java.util.ArrayList;


import javax.xml.ws.Endpoint;


import example.ws.handler.UpaHandler;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.BrokerApplication;
import pt.upa.broker.ws.BrokerPort;
import pt.upa.broker.ws.cli.BrokerClient;



public class BrokerApplication {
 
	static BrokerClient dup = null;
	
	public static void main(String[] args) throws Exception {
		System.out.println(BrokerApplication.class.getSimpleName() + " starting...");
			
		// Check arguments
		if (args.length < 3) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL wsName wsURL%n", BrokerApplication.class.getName());
			return;
		}

		String uddiURL = args[0];
		String name = args[1];
		String url = args[2];
		String infected = args[3];

		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		
		UpaHandler.wsName=name;
		UpaHandler.uddiURL=uddiURL;
		if(infected.equals("true"))
			UpaHandler.infected=true;
		else
			UpaHandler.infected=false;
		
		try {
			
			//urls transport discovery
			uddiNaming = new UDDINaming(uddiURL);
			int i = 1;
			String result;
			ArrayList<String> urls = new ArrayList<String>();
			urls.add(0, args[0]); //yehh
			while(true) {
				result = uddiNaming.lookup("UpaTransporter" + i);
				if(result != null) {
					urls.add(i, result);
					i++;
				}
				else
					break;
			}
			
			
			BrokerPort port = null;						
			//find backup broker
			if(name.equals("UpaBroker")){
			 dup = new BrokerClient(uddiURL, "UpaBroker10");
			 dup.setVerbose(true);
			 port = new BrokerPort(urls, dup, true, name, url);
			 System.out.println(dup.getWsURL());
			 System.out.println("Connected to Backup.");
			 
			}else {// publish endpoint
			 port = new BrokerPort(urls, dup, false, name, url); 
			}
			
			endpoint = Endpoint.create(port);
			System.out.printf("Starting %s%n", url);
			endpoint.publish(url);

			// publish to UDDI
			System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
			uddiNaming = new UDDINaming(uddiURL);
			uddiNaming.rebind(name, url);
			// wait
			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
			
			System.in.read();
			port.stopTimer();
			
			
			
			} catch (Exception e) {
				System.out.printf("Caught exception: %s%n", e);
				e.printStackTrace();
			
			}

		finally {
			try {
				if (endpoint != null) {
					// stop endpoint
					endpoint.stop();
					System.out.printf("Stopped %s%n", url);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
			try {
				if (uddiNaming != null) {
					// delete from UDDI
					uddiNaming.unbind(name);
					System.out.printf("Deleted '%s' from UDDI%n", name);
					}
			} catch (Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
		}

	}
	

}
