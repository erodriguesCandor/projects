package ca.ws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.jws.WebService;

@WebService
public interface CA {

	
	//public KeyPair read(String publicKeyPath, String privateKeyPath) throws NoSuchAlgorithmException, FileNotFoundException, IOException, InvalidKeySpecException;
	
	public byte[] getSignedCertificate(String serverName) throws Exception;

}
