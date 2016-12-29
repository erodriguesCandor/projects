package ca.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.crypto.Cipher;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

// classes generated from WSDL
import ca.ws.CA;
import ca.ws.CAImplService;
import ca.ws.Exception_Exception;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

public class CAClient {
	
	private static final String CERTIFICATE_FILE = "CA.cer";
	private CA port;
	
	public CAClient(String uddi, String CAname) throws JAXRException{
		connect(uddi,CAname);
	}
	
	private void connect(String uddi, String CAname) throws JAXRException {
		
		if (uddi==null || CAname==null) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL name%n", CAClient.class.getName());
			return;
		}

		String uddiURL = uddi;
		String name = CAname;

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
		CAImplService service = new CAImplService();
		port = service.getCAImplPort();

		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);		
		
	}
	
	public byte[] getSignedCertificate(String serverName) throws Exception{
		
		return port.getSignedCertificate(serverName);
	
	}
	
	
	
}
