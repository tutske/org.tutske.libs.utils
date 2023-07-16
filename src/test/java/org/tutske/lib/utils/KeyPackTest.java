package org.tutske.lib.utils;

import org.junit.jupiter.api.Test;

import java.security.KeyStore;
import java.security.cert.Certificate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


class KeyPackTest {

	@Test
	void it_should_read_keystores () throws Exception {
		KeyStore keystore = KeyPack.createKeyStore ("resource://test.jks", "testpass".toCharArray ());
		Certificate cert = keystore.getCertificate ("test");
		assertThat (cert, not (nullValue ()));
	}

}
