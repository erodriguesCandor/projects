package pt.upa.broker;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import com.sun.xml.ws.client.ClientTransportException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.TransportView;


public class BrokerClientApplication {

	public static void main(String[] args) throws Exception {
		System.out.println(BrokerClientApplication.class.getSimpleName() + " starting...");
		
		// Check arguments
				if (args.length < 2) {
					System.err.println("Argument(s) missing!");
					System.err.printf("Usage: java %s uddiURL name%n", BrokerClientApplication.class.getName());
				return;
				}

				String uddiURL = args[0];
				String name = args[1];

				System.out.printf("Contacting UDDI at %s%n", uddiURL);
				UDDINaming uddiNaming = new UDDINaming(uddiURL);

				System.out.printf("Looking for '%s'%n", name);
				String endpointAddress = uddiNaming.lookup(name);

				if (endpointAddress == null) {
					System.out.println("Not found!");
					return;
				} else {
					System.out.printf("Found %s%n", endpointAddress);
				}

				System.out.println("Creating stub ...");
				BrokerService service = new BrokerService();
				BrokerPortType port = service.getBrokerPort();
								
			
				System.out.println("Setting endpoint address ...");
				BindingProvider bindingProvider = (BindingProvider) port;
				Map<String, Object> requestContext = bindingProvider.getRequestContext();
				requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
				
				 int connectionTimeout = 1000;
		            // The connection timeout property has different names in different versions of JAX-WS
		            // Set them all to avoid compatibility issues
		            final List<String> CONN_TIME_PROPS = new ArrayList<String>();
		            CONN_TIME_PROPS.add("com.sun.xml.ws.connect.timeout");
		            CONN_TIME_PROPS.add("com.sun.xml.internal.ws.connect.timeout");
		            CONN_TIME_PROPS.add("javax.xml.ws.client.connectionTimeout");
		            // Set timeout until a connection is established (unit is milliseconds; 0 means infinite)
		            for (String propName : CONN_TIME_PROPS)
		                requestContext.put(propName, connectionTimeout);
		            System.out.printf("Set connection timeout to %d milliseconds%n", connectionTimeout);

		            int receiveTimeout = 3000;
		            // The receive timeout property has alternative names
		            // Again, set them all to avoid compability issues
		            final List<String> RECV_TIME_PROPS = new ArrayList<String>();
		            RECV_TIME_PROPS.add("com.sun.xml.ws.request.timeout");
		            RECV_TIME_PROPS.add("com.sun.xml.internal.ws.request.timeout");
		            RECV_TIME_PROPS.add("javax.xml.ws.client.receiveTimeout");
		            // Set timeout until the response is received (unit is milliseconds; 0 means infinite)
		            for (String propName : RECV_TIME_PROPS)
		                requestContext.put(propName, receiveTimeout);
		            System.out.printf("Set receive timeout to %d milliseconds%n", receiveTimeout);
				
				//-----
				int i = 0;
				while(i <=10){
				try {
					String result;
					
					System.out.println("-->ping");
					String result0 = port.ping("friend");
					System.out.println(result0);
					System.out.println();
				
					System.out.println("-->requestTransport1 -Lisboa -Faro -10");
					result = port.requestTransport("Lisboa", "Faro", 10);
					System.out.println(result);
					System.out.println();
					
					//System.out.println("-->requestTransport2 -Beja -Porto -10");
					//String result2 = port.requestTransport("Beja", "Porto", 10);
					//System.out.println(result2);
					//System.out.println();
					
					System.out.println("-->requestTransport3 -Lisboa -Coimbra -10");
					String result3 = port.requestTransport("Lisboa", "Coimbra", 10);
					System.out.println(result3);
					System.out.println();
					
					System.out.println("-->viewTransport1");
					TransportView t = port.viewTransport(result);
					System.out.println("id: " + t.getId() + " price: " + t.getPrice() + " state: " + t.getState());
					System.out.println();
					
					//System.out.println("-->viewTransport2");
					//TransportView t2 = port.viewTransport(result2);
					//System.out.println("id: " + t2.getId() + " price: " + t2.getPrice() + " state: " + t2.getState());
					//System.out.println();
					
					System.out.println("-->viewTransport3");
					TransportView t3 = port.viewTransport(result3);
					System.out.println("id: " + t3.getId() + " price: " + t3.getPrice() + " state: " + t3.getState());
					System.out.println();
					
					System.out.println("-->listTransports");
					List<TransportView> transp = port.listTransports();
					for(TransportView tv : transp)
						System.out.println("id: " + tv.getId() + " price: " + tv.getPrice() + " state: " + tv.getState());
					System.out.println();
					
					System.out.println("Press enter to shutdown");
				
					System.out.println("-->clearTransports");
					//port.clearTransports();
					System.out.println();
					break;
					
				}catch (ClientTransportException ce ){
					 errorTreatment(ce, uddiURL, name , uddiNaming, requestContext);
					i++; //just for debug	
					
				} catch(WebServiceException wse) {
	               errorTreatment(wse, uddiURL, name , uddiNaming, requestContext);
	               i++;
				
				}catch(Exception e) {
					e.printStackTrace();
					i++;				
				}finally {
					//port.clearTransports();	//debug
				}
			}		
				
	}
	
	public static void errorTreatment(Exception wse, String uddiURL, String name, UDDINaming uddiNaming, Map<String, Object> requestContext) throws JAXRException{
		System.out.println("Caught: " + wse);
        Throwable cause = wse.getCause();
        if (cause != null && (cause instanceof SocketTimeoutException | cause instanceof ClientTransportException) ) {
            System.out.println("The cause was a timeout exception: " + cause); System.out.printf("Contacting UDDI at %s%n", uddiURL);
			System.out.printf("Looking for '%s'%n", name);
			String NewEndpointAddress = uddiNaming.lookup(name);
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, NewEndpointAddress);}	 
	}
	

}
