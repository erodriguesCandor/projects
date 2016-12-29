package pt.upa.broker.ws;
import javax.jws.HandlerChain;

import java.io.IOException;
import java.lang.Math;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jws.WebService;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

import example.ws.handler.UpaHandler;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.cli.BrokerClient;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.transporter.ws.cli.TransporterClientException;



@WebService(
	    endpointInterface="pt.upa.broker.ws.BrokerPortType",
	    wsdlLocation="broker.2_0.wsdl",
	    name="Broker",
	    portName="BrokerPort",
	    targetNamespace="http://ws.broker.upa.pt/",
	    serviceName="BrokerService"
	)
public class BrokerPort implements BrokerPortType {

	final int LIFE_PROOF_TIME = 3000;
	final int TIME_ON_NETWORK = 2000;
	String nameServer;
	String urlServer;
	static Long AtualTime;
	private static boolean backupRunning = false;
	static BrokerClient bclient = null; 
	private ArrayList<String> TransporterUrls;
	private ArrayList<TransporterClient> TransporterUrlsDestinations = new ArrayList<>();

	private BindingProvider bindingProviderTransporter;
	private Map<TransportView, JobView> map = new HashMap<TransportView, JobView>();
	Integer mapId = 0;
	
	String [] RegionNorth = {"Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança"};
	String [] RegionCenter = {"Lisboa", "Leiria", "Santarém", "Castelo Branco", "Coimbra", "Aveiro", "Viseu", "Guarda"};
	String [] NorthCenter = {"Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança" +  RegionCenter};
	String [] SouthCenter = {"Setúbal", "Évora", "Portalegre", "Beja", "Faro" + RegionCenter};
	String [] allRegion = {"Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança" , "Lisboa", "Leiria", "Santarém", "Castelo Branco", "Coimbra", "Aveiro", "Viseu", "Guarda","Setúbal", "Évora", "Portalegre", "Beja", "Faro" };
	
	public BrokerPort(ArrayList<String> urls, BrokerClient dup, boolean TransporterLink, String nameServer, String urlServer) throws JAXRException, TransporterClientException {
		startTimer();
		this.bclient = dup;
		TransporterUrls = urls;
		this.urlServer = urlServer;
		this.nameServer = nameServer;
		transporterLinking(TransporterLink);
	}
	
	private void transporterLinking(boolean transporterLink) throws TransporterClientException {
		for (int i = 1; transporterLink && i < TransporterUrls.size() ; i++) {	
			TransporterUrlsDestinations.add(i-1, new TransporterClient(TransporterUrls.get(i)));
			System.out.println(TransporterUrlsDestinations.get(i-1));
		}	
		
		if(transporterLink) {System.out.println("Connected to:" + (TransporterUrls.size()-1) + " Transporters");}
	}
	

	private class MyTask extends TimerTask {
	 

		@Override
		public void run() {
				if(bclient != null){ //pings backups with actual time
					bclient.ping(String.valueOf(System.currentTimeMillis()));
				}
				if(bclient == null){ 
					if (AtualTime != null){ System.out.println("Time lapsed:" +( AtualTime - System.currentTimeMillis()));}
					if( (AtualTime != null) && (Math.abs(AtualTime - System.currentTimeMillis())> LIFE_PROOF_TIME + TIME_ON_NETWORK) ){
						//System.out.println("Time lapsed:"+ (AtualTime*1000 - System.currentTimeMillis()*1000));
						try {
							
							throw new BrokerDeadException("Check Broker");
						} catch (BrokerDeadException e) {
							System.out.println("Something happened to Broker :(");
							try {
								UDDINaming uddiNaming = new UDDINaming(TransporterUrls.get(0));
								UpaHandler.wsName="UpaBroker";
								System.out.printf("Publishing '%s' to UDDI at %s%n", "UpaBroker", urlServer);
								uddiNaming.rebind("UpaBroker", urlServer);					
								transporterLinking(true);
								backupRunning = true;
								System.out.println("Backup took place of Broker!");
								stopTimer();
								
							} catch (TransporterClientException e1) {e1.printStackTrace();
							} catch (JAXRException e1) { e1.printStackTrace();}
					}		
				}
			}	
		}
	}
			
	TimerTask tasknew = new MyTask();
	Timer timer = new Timer();
		
	public void startTimer() {
		int delay = LIFE_PROOF_TIME; //try this
		timer.schedule(tasknew, 10000, delay); //you have 10 seconds to start broker
	}
	
	public void stopTimer() {
		timer.cancel();
	}
		

	@Override
	public String ping(String name){
		String result = "";
		if( bclient!= null| backupRunning && bclient ==null){
		result = "";
		for (TransporterClient tport : TransporterUrlsDestinations){
			result += tport.ping(name)+ "\n"; 
			result = result + "Ping OK!";}		
		} 
		if( !backupRunning && bclient == null){ 
			System.out.println("Received proof of life.");
			AtualTime = Long.valueOf(name);
			result = String.valueOf(AtualTime);}
		return result;
	
	}
				
			
	
	
	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {	
		
		//Origem ou destino desconhecido
		int v = 0;
		for(String inputOrigin : allRegion ) {
			if(inputOrigin.equals(origin))
				v++;
		}
	 
		for(String inputDestination : allRegion) {
			if(inputDestination.equals(destination))
				v++ ;
		}
		
		if (v!= 2) throw new UnknownLocationFault_Exception(destination, null);
		
		if(origin.length() == 0 || origin == null || destination.length() == 0 || destination == null)
		throw new UnavailableTransportFault_Exception("Error: No available transport", new UnavailableTransportFault());
		
		
		//Preço menor que 0
		if(price < 0)
			throw new InvalidPriceFault_Exception("Error: Invalid Price", new InvalidPriceFault());
		
		
		//Coloca o pedido no mapa de viagens do corrector no estado REQUESTED
		TransportView tv = new TransportView();
		
		tv.setId(mapId.toString());
		tv.setOrigin(origin);
		tv.setDestination(destination);
		tv.setPrice(price);
		tv.setState(TransportStateView.REQUESTED);
		
		map.put(tv, null);
		mapId++;
		if(backupRunning | bclient != null){ 
			//updateMap("add", tv, null);
			//updateID(Integer.toString(mapId));
		}
		
		
		//Faz pedido a todas as transportadoras
		ArrayList<JobView> result = new ArrayList<JobView>();
		JobView jv = null;
		for(TransporterClient tport : TransporterUrlsDestinations) {
			try {
				jv = tport.requestJob(origin, destination, price);
			}
			
			catch(Exception e) {
				System.out.println("Error in requestJob: " + e.getMessage());
				
				jv = null;
			}
			
			if(jv != null) {
				result.add(jv);
			}
		}
		
		
		//Não existe transporte da origem para o destino
		if(result.isEmpty())
			throw new UnavailableTransportFault_Exception("Error: No available transport", new UnavailableTransportFault());
		
		
		//Procura pelo preço mais barato		
		int cheaperPrice = Integer.MAX_VALUE;
		JobView cheaperJob = null;
		for(JobView j : result) {
			if(j.getJobPrice() < cheaperPrice) {
				cheaperJob = j;
				cheaperPrice = j.getJobPrice();
			}
		}
		
		
		//Não existe transporte dentro do preço definido
		if(cheaperPrice > price) {		
			//Altera estado de todas as viagens para FAILED
			for(Entry<TransportView, JobView> entry : map.entrySet()) {
				if(entry.getKey().getState().equals(TransportStateView.BUDGETED))
					entry.getKey().setState(TransportStateView.FAILED);
			}
			
			
			throw new UnavailableTransportPriceFault_Exception("Error: No available transport for defined price", new UnavailableTransportPriceFault());
		}
		
		
		//Altera preço para o orçamento dado pela transportadora e estado para BUDGETED
		map.remove(tv);
		tv.setPrice(cheaperJob.getJobPrice());
		tv.setState(TransportStateView.BUDGETED);
		tv.setTransporterCompany(cheaperJob.getCompanyName());
		map.put(tv, cheaperJob);
		if(!backupRunning || bclient != null){
			updateMap("add", tv, cheaperJob);
			updateID(Integer.toString(mapId));
			}
		
		
		//Procura pelo port correspondente através do nome da transportadora
		String companyName = cheaperJob.getCompanyName();
		int pos =  Integer.valueOf(companyName.replaceAll("[a-zA-Z]", ""));
		
		TransporterClient tp = TransporterUrlsDestinations.get(pos - 1);
		
		
		//Tudo OK, tenta marcar transporte
		String cheaperPosition = null;
		try {
			tp.decideJob(cheaperJob.getJobIdentifier(), true);
		}
		
		catch(Exception e) {
			tp = null;
			System.out.println("Error in decideJob: " + e.getMessage());
		}
				
				
		//Altera estado para BOOKED
		if(tp != null) {	
			for(Entry<TransportView, JobView> entry : map.entrySet()) {
				if(entry.getValue() != null) {
					if(entry.getValue().getCompanyName().equals(cheaperJob.getCompanyName()) && entry.getValue().getJobIdentifier().equals(cheaperJob.getJobIdentifier())) {
						entry.getKey().setState(TransportStateView.BOOKED);
						cheaperPosition = entry.getKey().getId();
					}
				}
			}
			
			
			//Retorna Id da viagem no corrector
			return cheaperPosition;
		}
		
		else
			return null;
		
	}
	
	
	
	
	
	
	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		
		//Transporte não existe		
		if(map.size() == 0)
			throw new UnknownTransportFault_Exception("Error: Unknown Transport", new UnknownTransportFault());
		
		
		//Procura pelo transporte no corrector
		TransportView tv = null;
		
		for(Entry<TransportView, JobView> entry : map.entrySet()){
			if(entry.getKey().getId().equals(id))
				tv = entry.getKey();
		}
		
		if(tv != null) {
			//Procura pelo port correspondente através do nome da transportadora
			String companyName = tv.getTransporterCompany();
			int i =  Integer.valueOf(companyName.replaceAll("[a-zA-Z]", ""));
		
			TransporterClient tport = TransporterUrlsDestinations.get(i-1);
		
			//Actualiza o estado da viagem			
			if(map.get(tv) != null) {
				JobView jv = tport.jobStatus(map.get(tv).getJobIdentifier());
		
				if(jv != null) {
					if(!tv.getState().equals(TransportStateView.FAILED)) {
						JobStateView state = jv.getJobState();
						if(state.equals(JobStateView.HEADING))
							tv.setState(TransportStateView.HEADING);
		
						if(state.equals(JobStateView.ONGOING))
							tv.setState(TransportStateView.ONGOING);
		
						if(state.equals(JobStateView.COMPLETED))
							tv.setState(TransportStateView.COMPLETED);
					}
		
				
					//Retorna viagem
					return tv;
				}
		
				else {
					//Se isto acontece, algo de errado se passa...
					System.out.println("Error: jobStatus returning null!");
					return null;
				}
			}
			
			else {
				//Retorna viagem
				return tv;
			}
		}
		
		else {
			//Não encontrou transporte
			throw new UnknownTransportFault_Exception("Error: Unknown Transport", new UnknownTransportFault());
		}
		
	}
	
	
	
	
	
	
	@Override
	public List<TransportView> listTransports() {
		
		
		if( backupRunning| bclient != null){
		
		LinkedList<TransportView> transportList = new LinkedList<TransportView>();
		
		for(Entry<TransportView, JobView> entry : map.entrySet()) {
			TransportView tv = null;
			try {
				tv = viewTransport(entry.getKey().getId());
			}
			
			catch (Exception e) {
				System.out.println("Error in viewTransport: " + e.getMessage());
			}
			
			if(tv != null) {
				int index = Integer.valueOf(tv.getId());
				while(transportList.size() <= index)
					transportList.add(null);
				
				transportList.set(index, tv);

			}
		}
		
		return transportList;
		}
				
		if( !backupRunning && bclient == null){
			for(Entry<TransportView, JobView> entry : map.entrySet()) {
				if(entry.getKey() != null) {
					
					System.out.println("ID: " + entry.getKey().getId());

				}
			}
			return new LinkedList<TransportView>();
		}
		
		return new LinkedList<TransportView>();

		
	}
	
	
	
	
	
	
	@Override
	public void clearTransports() {
	
		if( !backupRunning  && bclient != null){
			map.clear();
			mapId = 0;
			bclient.clearTransports();
		
		}
			//Pede às transportadoras para limpar viagens
			for(TransporterClient tport : TransporterUrlsDestinations)
				tport.clearJobs();
			
		
		if(  bclient == null){ 
			map.clear();
			mapId = 0;
		}

		
	}


	@Override
	public String updateMap(String action, TransportView tv, JobView jb) {
		if(bclient!=null){
			if(action.equals("add")){  bclient.updateMap("add", tv, jb);}
			if(action.equals("rm")){  bclient.updateMap("rm", tv, jb); }
		}
		if( backupRunning || bclient == null ){
			System.out.println("Map updated");
			if(action.equals("add")){ map.put(tv, jb); }
			if(action.equals("rm")){ map.remove(tv); }
			}
		
		return "Backup done";
	}

	
	@Override
	public String updateID(String id) {
		if(bclient!=null){ bclient.updateID(id);}
		if(backupRunning || bclient == null){mapId = Integer.valueOf(id); System.out.println("ID updated");}
		return "Backup ID done";
	}
	
	




	


	

	


	


	

	
	
	




}
