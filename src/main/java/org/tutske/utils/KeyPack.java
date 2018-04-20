package org.tutske.utils;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;


public class KeyPack {

	public static KeyStore createKeyStore (String resource, char [] password) throws Exception {
		return createKeyStore (KeyStore.getDefaultType (), resource, password);
	}

	public static KeyStore createKeyStore (String type, String resource, char [] password) throws Exception {
		return createKeyStore (type, Resource.getResource (resource), password);
	}

	public static KeyStore createKeyStore (File file, char [] password) throws Exception {
		return createKeyStore (KeyStore.getDefaultType (), file, password);
	}

	public static KeyStore createKeyStore (String type, File file, char [] password) throws Exception {
		try ( InputStream stream = new FileInputStream (file) ) {
			return createKeyStore (type, stream, password);
		}
	}

	public static KeyStore createKeyStore (String type, InputStream stream, char [] password) throws Exception {
		KeyStore store = KeyStore.getInstance (type);
		store.load (stream , password);
		return store;
	}

	public static KeyPack fromResources (String keys, String trusts, String password) throws Exception {
		try ( InputStream keystream = Resource.getResource (keys); InputStream truststream = Resource.getResource (trusts)) {
			return new KeyPack (
				createKeyStore (KeyStore.getDefaultType (), keystream, password.toCharArray ()),
				createKeyStore (KeyStore.getDefaultType (), truststream, password.toCharArray ()),
				password
			);
		}
	}

	private final KeyStore keystore;
	private final KeyStore truststore;
	private final char [] password;

	public KeyPack (KeyStore keystore, KeyStore truststore, String password) {
		this.keystore = keystore;
		this.truststore = truststore;
		this.password = password.toCharArray ();
	}

	public KeyManagerFactory keyManager () throws Exception {
		KeyManagerFactory factory = KeyManagerFactory.getInstance (KeyManagerFactory.getDefaultAlgorithm ());
		factory.init (keystore, password);
		return factory;
	}

	public KeyManager [] keyManagers () throws Exception {
		return keyManager ().getKeyManagers ();
	}

	public TrustManagerFactory trustManager () throws Exception {
		TrustManagerFactory factory = TrustManagerFactory.getInstance (TrustManagerFactory.getDefaultAlgorithm ());
		factory.init (truststore);
		return factory;
	}

	public TrustManager [] trustManagers () throws Exception {
		return trustManager ().getTrustManagers ();
	}

	public KeyStore keystore () {
		return keystore;
	}

	public KeyStore truststore () {
		return truststore;
	}

}
