package pt.upa.transporter;


import javax.xml.ws.Endpoint;


import example.ws.handler.UpaHandler;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.TransporterPort;


public class TransporterApplication {
	
	
	public static void main(String[] args) throws Exception {
		//SwingUtilities.invokeLater(new Runnable());
		
		System.out.println(TransporterApplication.class.getSimpleName() + " starting...");
		
		// Check arguments
				if (args.length < 3) {
					System.err.println("Argument(s) missing!");
					System.err.printf("Usage: java %s uddiURL wsName wsURL%n", TransporterApplication.class.getName());
					return;
				}

				String uddiURL = args[0];
				String name = args[1];
				String nameServer = args[1].substring(3,args[1].length());
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
					TransporterPort port = new TransporterPort(name);
					endpoint = Endpoint.create(port);

					// publish endpoint
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

				} finally {
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
						}
					} catch (Exception e) {
						System.out.printf("Caught exception when deleting: %s%n", e);
					}
				}

	}
	

}
