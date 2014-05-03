package org.tutske.files;

import java.io.File;


class DirectoryFileResolver implements FileResolver {

	private File directory;

	protected DirectoryFileResolver (File directory) {
		this.directory = directory;
	}

	@Override
	public File getFile (String filename) {
		return new File (directory, filename);
	}

}
