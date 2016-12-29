package pt.upa.transporter.ws;


import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.jws.HandlerChain;
import javax.jws.WebService;


@WebService(
	    endpointInterface="pt.upa.transporter.ws.TransporterPortType",
	    wsdlLocation="transporter.1_0.wsdl",
	    name="Ping",
	    portName="TransporterPort",
	    targetNamespace="http://ws.transporter.upa.pt/",
	    serviceName="TransporterService"
	)
@HandlerChain(file = "/hello_handler-chain.xml")
public class TransporterPort extends TimerTask implements TransporterPortType {
			
		private Integer JobInt = 0;
		private String _identifier;
		private Random random = new Random();
		List<String> par = new LinkedList<>();
		List<String>  impar = new LinkedList<>();
		List<String>  all = new LinkedList<>();
		String [] RegionSouth = {"Setúbal", "Évora", "Portalegre", "Beja", "Faro"};
		String [] RegionNorth = {"Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança"};
		String [] RegionCenter = {"Lisboa", "Leiria", "Santarém", "Castelo Branco", "Coimbra", "Aveiro", "Viseu", "Guarda"};
		String [] NorthCenter = {"Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança" +  RegionCenter};
		String [] SouthCenter = {"Setúbal", "Évora", "Portalegre", "Beja", "Faro" + RegionCenter};
		String [] allRegion = {"Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança" , "Lisboa", "Leiria", "Santarém", "Castelo Branco", "Coimbra", "Aveiro", "Viseu", "Guarda","Setúbal", "Évora", "Portalegre", "Beja", "Faro" };
		List<JobView> JobViews = new ArrayList<>();
		//private static TransporterPort instance = null;
		
		private class MyTask extends TimerTask {
			@Override
			public void run() {
				for(JobView job : JobViews){
					if((JobStateView.ACCEPTED).equals(job.getJobState())){
						job.setJobState(JobStateView.HEADING);continue;}
					if((JobStateView.HEADING).equals(job.getJobState())){
						job.setJobState(JobStateView.ONGOING); continue;}
					if((JobStateView.ONGOING).equals(job.getJobState())){
						job.setJobState(JobStateView.COMPLETED); continue;}
				}	
			}
		}
			
		TimerTask tasknew = new MyTask();
		Timer timer = new Timer();
		
		
		public TransporterPort(String identifier) {			
				_identifier = identifier;
			for(String region : NorthCenter)
				par.add(region);
			for(String region : SouthCenter )
				impar.add(region);
			startTimer();
		}
				
		public void startTimer() {
			int delay = 2000 + random.nextInt(3000);
			timer.schedule(tasknew, 2000, delay);
		}
		
		public void stopTimer() {
			timer.cancel();
		}
		
		@Override
		public String ping(String name){
			int nextInt = random.nextInt(3);
				if (nextInt == 0) {
					//PingFault faultInfo = new PingFault();
					//faultInfo.setNumber(nextInt);
					return "Pong from : " + _identifier + " " +  name + " -> Simulated error in server";
				}
				
				return "Pong from : " + _identifier +" "+  name + "!";
			}

		@Override
		public JobView requestJob(String origin, String destination, int price)
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			
			
			if(price <= 0 ) 
				throw new BadPriceFault_Exception("Price must be greater than Zero", new BadPriceFault());
			String ServerName = _identifier;
			
			int handler =  Integer.valueOf(ServerName.replaceAll("[a-zA-Z]", ""));
			
			int j = 0;
			
			for (String inputOrigin : allRegion ){
				if(inputOrigin.equals(origin)) { j++; }}
		 
			for(String inputDestination : allRegion){
				if(inputDestination.equals(destination)){ j++ ;}}
			
			if(j!= 2) throw new BadLocationFault_Exception(origin, new BadLocationFault());
				
			//same for any transporter
			if(price > 100) return null;
			if(price > 10 && price <= 100){	
			
				//impar
				j = 0;
				if( handler % 2 != 0){
					for(String inputDestination :  RegionNorth ) {
						if(destination.equals(inputDestination)){ 
							j++ ;
						}
					}
					
					for (String inputOrigin : RegionNorth ) {
							if(inputOrigin.equals(origin)){ 
								j++;
							}
						}
					
				
					if(j != 0 ) return null;	//reject on delivery
					
					if(price % 2 != 0){
						while(true){
							int  i = random.nextInt(101);
							if(i < price){ price = i; break;}
						}
					} else{ 
						while(true){
							int ii  = random.nextInt(101) ;
							if(ii >= price){ price = ii; break;}
						}
					}
				
				}
										
				//par
				j = 0;	
				if( handler % 2 == 0){
					for (String inputOrigin : RegionSouth )
						if(inputOrigin.equals(origin)) { j++;}
					for(String inputDestination : RegionSouth)
						if(inputDestination.equals(destination)){ j++ ;}
		
					if(j!= 0 ) return null; //reject on delivery
					
					if(price % 2 == 0){
						while(true){
							int  i = random.nextInt(101) ;
							if(i < price){ price = i; break;}
						}
					} else{ 
						while(true){
							int ii  = random.nextInt(101);
							if(ii >= price){ price = ii; break;}
						}
					}
					
					
				}	
			}
			
			if(price <= 10){
				
				j=0;
				if( handler % 2 == 0){
					for (String inputOrigin : RegionSouth )
						if(inputOrigin.equals(origin)) { j++;}
					for(String inputDestination : RegionSouth)
						if(inputDestination.equals(destination)){ j++ ;}
							
					if(j!= 0 ) return null; //reject on delivery
				}
				
				j = 0;
				if( handler % 2 != 0){
					for(String inputDestination :  RegionNorth ) 
						if(destination.equals(inputDestination)){ j++ ;}		
					for (String inputOrigin : RegionNorth ) 
						if(inputOrigin.equals(origin)){ j++;}
							
						if(j != 0 ) return null;
					}
				
				while(true){
					int  i = random.nextInt(10);
					if(i < 10 && i > 0){ price = i; break;}
				}
			}	
				
			//jobID
			JobView job = new JobView();
			job.setCompanyName(this._identifier);
			job.setJobDestination(destination);
			job.setJobIdentifier(JobInt.toString());
			JobInt++;
			job.setJobOrigin(origin);
			job.setJobPrice(price);
			job.setJobState(JobStateView.PROPOSED);
			JobViews.add(Integer.parseInt(job.getJobIdentifier()), job);
									
			return job;
		}

		@Override
		public   JobView  decideJob(String id, boolean accept) throws BadJobFault_Exception {
			
			if ( id != null && !id.isEmpty()  ){
				if(Integer.parseInt(id) > JobViews.size()-1 ) throw new BadJobFault_Exception(id, new BadJobFault());
			
				JobView JobToDecideOn = JobViews.get(Integer.parseInt(id));
			
				if(JobToDecideOn == null | JobToDecideOn.getJobState().equals(JobStateView.ACCEPTED)| JobToDecideOn.getJobState().equals(JobStateView.REJECTED)){
					throw new BadJobFault_Exception(id, null);			
				}
				
				if(accept){JobToDecideOn.setJobState(JobStateView.ACCEPTED);}
				else {JobToDecideOn.setJobState(JobStateView.REJECTED);}
		
				return JobToDecideOn;
			
			} else throw new BadJobFault_Exception(id, new BadJobFault());
			
		}

		@Override
		public JobView jobStatus(String id) {
			if(id!= null && !id.isEmpty() &&
					Integer.parseInt(id) < JobViews.size() && JobViews.get(Integer.parseInt(id))!= null){ return JobViews.get(Integer.parseInt(id)); }
			return null;
		}

		@Override
		public List<JobView> listJobs() {
			return JobViews;
		}

		@Override
		public void clearJobs() {
			JobViews.clear();
			JobInt = 0;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}

		


}
