package org.tutske.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;


public class Resource {

	public static InputStream getResource (String uri) throws Exception {
		try { return getResource (new URI (uri)); }
		catch ( Exception e ) { return new FileInputStream (uri); }
	}

	public static InputStream getResource (URI uri) throws Exception {
		if ( uri == null ) { throw new IllegalArgumentException ("Expected a uri but got null"); }

		String scheme = uri.getScheme ();
		String path = uri.getSchemeSpecificPart ().substring (2);

		if ( "resource".equals (scheme) ) { return ClassLoader.getSystemResourceAsStream (path); }
		else if ( "file".equals (scheme) ) { return new FileInputStream (path); }
		else { throw new IllegalArgumentException ("Expected a uri with 'resource://' or 'file://' but got " + uri); }
	}

}
