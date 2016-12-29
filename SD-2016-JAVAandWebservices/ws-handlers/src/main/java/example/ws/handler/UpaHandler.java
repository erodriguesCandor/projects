package example.ws.handler;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.Set;

//import javax.wsdl.extensions.soap.SOAPBody;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
//import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.NodeList;

import ca.ws.cli.CAClient;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;


/**
 *  This SOAPHandler shows how to set/get values from headers in
 *  inbound/outbound SOAP messages.
 *
 *  A header is created in an outbound message and is read on an
 *  inbound message.
 *
 *  The value that is read from the header
 *  is placed in a SOAP message context property
 *  that can be accessed by other handlers or by the application.
 */
public class UpaHandler implements SOAPHandler<SOAPMessageContext> {
    
    public static final String KEYSTOREPASSWORD = "1nsecure";
    
    public static final String KEYPASSWORD = "ins3cur3";   
    
    public final int ARRIVAL_TIME = 30000;
    
    private static ArrayList<String> requests = new ArrayList<String>();
    
    private static Map<String,Integer> requestsForSender = new HashMap<String,Integer>();
  
    public static String wsName;
    
    public static String uddiURL;
    
    public static boolean infected;
    //
    // Handler interface methods
    //
    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        System.out.println("We're going to analyse your message...");

        Boolean outboundElement = (Boolean) smc
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try {
            if (outboundElement.booleanValue()) {
                System.out.println("Processing OutBound SOAPMessage,...");
                
                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                
                // add header
                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();               

                //Adicionar Sender 
                
                	//creat SenderName
                	Name senderName = se.createName("SENDER", "s", "pt.upa."+wsName);
                	SOAPHeaderElement elementSender = sh.addHeaderElement(senderName);

	                // add header element value
	                elementSender.addTextNode(wsName);
                
                //Adicionar Nounce
                
                	//creat NounceName
                	Name nounceName = se.createName("NOUNCE", "n", "pt.upa."+wsName);
                	SOAPHeaderElement elementNounce = sh.addHeaderElement(nounceName);                
            		//Adicionar ao header
	                Timestamp nounce = new Timestamp(new Date().getTime());
	                elementNounce.addTextNode(nounce.toString());
                
                //Adiconar Signature
	                msg.saveChanges();
                	//criar Signature
                	byte[] sig = SOAPtoByteArray(msg);
                	byte[] sigMessage = makeDigitalSignature(sig,wsName);            
                	
                	//criar SignatureName
                	Name signatureName = se.createName("SIGNATURE", "sig", "pt.upa."+wsName);
                	SOAPHeaderElement signatureNounce = sh.addHeaderElement(signatureName);         
                //Adicionar ao header     
                    signatureNounce.addTextNode(printBase64Binary(sigMessage));
                    
                    if(infected){
                    	System.out.println("We're going to infect your message...");
                    	SOAPBody body = msg.getSOAPBody();
                    	Name devilName = se.createName("DEVIL", "d", "pt.upa."+wsName);
                    	SOAPHeaderElement elementDevil = sh.addHeaderElement(devilName);
                    	elementDevil.addTextNode("IM DEVIL! YOU ARE SO INFECTED NOW! I DELETED YOUR SOAPBODY");
                    	body.detachNode();                    	
                    	msg.saveChanges();
                    	System.out.println("Your Message is Infected now.. Muahaha");
                    	msg.writeTo(System.out);
                    }
                    if(!infected){
                System.out.println("Message sent with TimeStamp:"+nounce.toString());
                System.out.println("It's Done!!");
                System.out.println("Message Processed With Sucess!! Your Message is Safe, don't Worry!");}	

            } else {
                System.out.println("Reading inbound SOAP message...");
                System.out.println("We're going to check it! Trust Us");

                
                // get SOAP envelope header
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                SOAPHeader sh = se.getHeader();
                

                // check header
                if (sh == null) {
                    System.out.println("Header not found.");
                    return true;
                }
                String receiver=null;
                String nounce=null;
                String signatureText="";
                byte[] signatureBytes, SOAPBytes;
                NodeList nodes = sh.getChildNodes();
                
                //Processing Nodes
                for(int i = 0;i<nodes.getLength();i++){
                	if(nodes.item(i).getLocalName().equals("SENDER"))
                		receiver=nodes.item(i).getTextContent();
                	if(nodes.item(i).getLocalName().equals("NOUNCE"))
                		nounce=nodes.item(i).getTextContent();
                	if(nodes.item(i).getLocalName().equals("SIGNATURE")){
                		signatureText=nodes.item(i).getTextContent();
                		nodes.item(i).getParentNode().removeChild(nodes.item(i));
                		sh.normalize();
                		msg.saveChanges();
                	}
                } 
                
                //In case header was changed
                if(receiver==null || nounce==null || signatureText==null)
                	throw new InvalidMessageException();

                //Valid SOAPMessage                
           
                SOAPBytes = SOAPtoByteArray(msg);
                signatureBytes = parseBase64Binary(signatureText);
                
                if(!verifyDigitalSignature(signatureBytes,SOAPBytes,getPublicKeyFromCertificate(receiver)))
                		throw new InvalidMessageException();
                
                //Valid Atributes from SOAPMessage
                if(!validTime(nounce))
                	throw new OutOfTimeException();
                if(!validExistance(nounce))
                	throw new NounceAlreadyProcessedException(); 

                

            }
        } catch (InvalidMessageException | OutOfTimeException | NounceAlreadyProcessedException e) {
            throw e;
        } 
        catch (Exception e) {
            System.out.print("Caught exception in handleMessage: ");
            System.out.println(e);
            System.out.println("Continue normal processing...");
        }
        System.out.println("It's Done!");
        System.out.println("You can trust this message");
        return true;
    }
	/**
	 * auxiliary method to get public key from certificate
	 * verifys if updateCertificate is needed
	 * @throws Exception 
	 */
    private PublicKey getPublicKeyFromCertificate(String receiver) throws Exception {
    	if(needUpdateCertificate(receiver))
    		updateCertificate(receiver);
		Certificate certReceiver = readCertificateFile(receiver+".cer");
		//get receiver public key from certificate
		return certReceiver.getPublicKey();
	}
    
	/**
	 * auxiliary method to know if update certificate is needed
	 * @return true if requestsForSender > 30 and false otherWise
	 */
	private boolean needUpdateCertificate(String receiver) {
		if(!requestsForSender.containsKey(receiver)){
			requestsForSender.put(receiver, 0);
		}			
		int operations=requestsForSender.get(receiver);
		if(operations==0 || operations > 30){
			requestsForSender.put(receiver, 1);
			return true;
		}
		else{
			operations++;
			requestsForSender.put(receiver, operations);
			return false;
		}

		
	}
	
	/**
	 * update the current receiver Certificate File
	 */
	private void updateCertificate(String receiver) throws Exception {

		//get ca certificate
			KeyStore jksWsName = readKeystoreFile(wsName+".jks", KEYPASSWORD.toCharArray());
			Certificate certCa = jksWsName.getCertificate("ca");
		//get ca public key from certificate
			PublicKey caPubKey = certCa.getPublicKey();
		
		
		//connect to CA and get receiver Certificate
			CAClient Ca = new CAClient(uddiURL, "CA");
			byte[] certReceiver = Ca.getSignedCertificate(receiver);
		//convert to certificate
			writetoFile(certReceiver,  "Suposed.cer");
			Certificate certificate =  readCertificateFile("Suposed.cer");
		//verify certificate is signed for CA
			certificate.verify(caPubKey);
		//save own certificate		
			try{			
				writetoFile(certReceiver, receiver+".cer");
				(new File("Suposed.cer")).delete();
			
				} catch (Exception e){
					e.printStackTrace();				
			}
		
	}

	/**
	 * auxiliary method to if nounce was already processed for this handler
	 * @return true if not processed and false otherwise
	 */
	private boolean validExistance(String nounce) {
    	if(requests.contains(nounce))
    		return false;
    	return true;
	}

	/**
	 * checks if nounce has arrived in ARRIVAL_TIME
	 * @return true if time is valid and false otherwise
	 */
	private boolean validTime(String nounce) {
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
			Date parsedDate = dateFormat.parse(nounce);
			Timestamp timestamp = new Timestamp(parsedDate.getTime());
			
			Timestamp now = new Timestamp(new Date().getTime());
			
			long diff = now.getTime() - timestamp.getTime();
			
			if(diff >= ARRIVAL_TIME)
				return false;
			else
				return true;
		}
		
		catch(ParseException e){
			System.out.println(e.getStackTrace());
			return false;
		}
	}


	private byte[] SOAPtoByteArray(SOAPMessage soap) {
    	ByteArrayOutputStream out = new ByteArrayOutputStream(); 
    	try{soap.writeTo(out);
    	} 
    	catch (SOAPException | IOException e ){
    		e.printStackTrace();
    	}
    	byte[] messageArray = out.toByteArray();
		return messageArray;
	}

	public boolean handleFault(SOAPMessageContext smc) {
        System.out.println("Ignoring fault message...");
        return true;
    }

    public void close(MessageContext messageContext) {
    }    
    
	/** auxiliary method to calculate digest from text and cipher it */
	public static byte[] makeDigitalSignature(byte[] bytes, String sender) throws Exception {
		// get a signature object using the SHA-1 and RSA combo
		// and sign the plain-text with the private key
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(getPrivateKeyFromKeystore(sender+".jks",sender));
		sig.update(bytes);
		byte[] signature = sig.sign();
		return signature;
	}
	
	/**
	 * Reads a PrivateKey from a key-store
	 * 
	 * @return The PrivateKey
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyFromKeystore(String keyStoreFilePath, String sender) throws Exception {
		KeyStore keystore = readKeystoreFile(sender+".jks", KEYPASSWORD.toCharArray());
		PrivateKey key = (PrivateKey) keystore.getKey(sender, KEYSTOREPASSWORD.toCharArray());
		return key;
	}
	
	/**
	 * Reads a KeyStore from a file
	 * 
	 * @return The read KeyStore
	 * @throws Exception
	 */
	public static KeyStore readKeystoreFile(String keyStoreFilePath, char[] keyStorePassword) throws Exception {
		FileInputStream fis;
		try {
			fis = new FileInputStream(keyStoreFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Keystore file <" + keyStoreFilePath + "> not fount.");
			return null;
		}
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(fis, keyStorePassword);
		return keystore;
	}
	
	/**
	 * auxiliary method to calculate new digest from text and compare it to the
	 * to deciphered digest
	 */
	public static boolean verifyDigitalSignature(byte[] cipherDigest, byte[] bytes, PublicKey publicKey)
			throws Exception {

		// verify the signature with the public key
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initVerify(publicKey);
		sig.update(bytes);

		try {
			return sig.verify(cipherDigest);
		} catch (SignatureException se) {
			System.err.println("Caught exception while verifying signature " + se);
			return false;
		}
	}
	
	/**
	 * Writes bytes to file
	 * @param certificateBytes - bytes readed from certificate
	 * @throws Exception
	 */
	public static void writetoFile(byte[] certificateBytes, String Path) throws Exception {
		FileOutputStream fos = new FileOutputStream(Path);
		fos.write(certificateBytes);
		fos.close();
	}
	
	/**
	 * Reads bytes from file and returns a certificate
	 * @param certificateFilePath
	 * @throws Exception
	 */	
	public  static Certificate readCertificateFile(String certificateFilePath) throws Exception {
		FileInputStream fis;

		try {
			fis = new FileInputStream(certificateFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Certificate file <" + certificateFilePath + "> not fount.");
			return null;
		}
		BufferedInputStream bis = new BufferedInputStream(fis);

		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		if (bis.available() > 0) {
			Certificate cert = cf.generateCertificate(bis);
			return cert;			
		}
		
		bis.close();
		fis.close();
		return null;
	}
		
}