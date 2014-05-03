package org.tutske.files;

import java.io.File;


class RegularFileResolver implements FileResolver {

	RegularFileResolver () {
	}

	@Override
	public File getFile (String filename) {
		return new File (filename);
	}

}
