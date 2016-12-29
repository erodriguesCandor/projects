package ca.ws;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.jws.WebService;

@WebService(endpointInterface = "ca.ws.CA")
public class CAImpl implements CA {
	
	final static String CERTIFICATE_FILE = "CA.cer";

	final static String KEYSTORE_FILE = "CAkeystore.jks";
	final static String KEYSTORE_PASSWORD = "1nsecure";

	final static String KEY_ALIAS = "example";
	final static String KEY_PASSWORD = "ins3cur3";
	
	
	
	@Override
	public byte[] getSignedCertificate(String serverName) throws Exception {		
		return getCertificate(serverName+".cer");
		
	}	
	
	
	private byte[] getCertificate(String pathFile) {
		Path path = Paths.get(pathFile);
		try {
			byte[] data = Files.readAllBytes(path);
			return data;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public PrivateKey getPrivateKeyFromKeystore() throws Exception {

		KeyStore keystore = readKeystoreFile(KEYSTORE_FILE, KEYSTORE_PASSWORD.toCharArray());
		PrivateKey key = (PrivateKey) keystore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());

		return key;
	}
	
	public KeyStore readKeystoreFile(String keyStoreFilePath, char[] keyStorePassword) throws Exception {
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



	/*
	@Override
	public KeyPair read(String publicKeyPath, String privateKeyPath) throws NoSuchAlgorithmException, FileNotFoundException, IOException, InvalidKeySpecException {

		System.out.println("Reading public key from file " + publicKeyPath + " ...");
		byte[] pubEncoded = readFile(publicKeyPath);

		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubEncoded);
		KeyFactory keyFacPub = KeyFactory.getInstance("RSA");
		PublicKey pub = keyFacPub.generatePublic(pubSpec);
		System.out.println(pub);

		System.out.println("---");

		System.out.println("Reading private key from file " + privateKeyPath + " ...");
		byte[] privEncoded = readFile(privateKeyPath);

		PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privEncoded);
		KeyFactory keyFacPriv = KeyFactory.getInstance("RSA");
		PrivateKey priv = keyFacPriv.generatePrivate(privSpec);

		System.out.println(priv);
		System.out.println("---");

		KeyPair keys = new KeyPair(pub, priv);
		return keys;
	}
	
	
	private static byte[] readFile(String path) throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(path);
		byte[] content = new byte[fis.available()];
		fis.read(content);
		fis.close();
		return content;
	}
	*/

}
