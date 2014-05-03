package org.tutske.files;

import java.io.File;

import org.tutske.utils.XDate;


class BackupFileResolver implements FileResolver {

	private static final String SEPERATOR = "-";
	private XDate date;

	BackupFileResolver (XDate date) {
		this.date = date;
	}

	@Override
	public File getFile (String filename) {
		String finalName = filename + SEPERATOR + date.getTimestampString ();
		return new File (finalName);
	}

}
