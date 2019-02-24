package org.tutske.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Resource {

	public static InputStream getResource (String uri) throws Exception {
		try { return getResource (new URI (uri)); }
		catch ( Exception e ) { return new FileInputStream (uri); }
	}

	public static InputStream getResource (URI uri) throws Exception {
		if ( uri == null ) { throw new IllegalArgumentException ("Expected a uri but got null"); }

		String scheme = uri.getScheme ();
		String path = uri.getSchemeSpecificPart ().substring (2);

		if ( "resource".equals (scheme) || "classpath".equals (scheme) ) {
			return ClassLoader.getSystemResourceAsStream (path);
		} else if ( "file".equals (scheme) ) {
			return Files.newInputStream (Paths.get (path));
		} else {
			throw new IllegalArgumentException ("Expected a resource uri with 'resource://', 'classpath://' or 'file://' but got " + uri);
		}
	}

}
