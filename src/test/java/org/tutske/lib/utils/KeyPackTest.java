package org.tutske.lib.utils;

import org.junit.jupiter.api.Test;

import java.security.KeyStore;
import java.security.cert.Certificate;


public class KeyPackTest {

	@Test
	public void it_should_read_keystores () throws Exception {
		KeyStore keystore = KeyPack.createKeyStore ("resource://test.jks", "testpass".toCharArray ());

		Certificate cert = keystore.getCertificate ("test");
	}

}
