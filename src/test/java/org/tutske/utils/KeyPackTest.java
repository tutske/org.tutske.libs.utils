package org.tutske.utils;

import org.junit.Test;

import java.security.KeyStore;
import java.security.cert.Certificate;

import static org.junit.Assert.assertThat;


public class KeyPackTest {

	@Test
	public void it_should_read_keystores () throws Exception {
		KeyStore keystore = KeyPack.createKeyStore ("resource://test.jks", "testpass".toCharArray ());

		Certificate cert = keystore.getCertificate ("test");
	}

}
