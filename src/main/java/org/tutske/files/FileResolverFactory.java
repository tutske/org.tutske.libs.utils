package org.tutske.files;

import org.tutske.utils.XDate;


public class FileResolverFactory {

	public FileResolver regularFileResolver () {
		return new RegularFileResolver ();
	}

	public FileResolver backupFileResolver () {
		return backupFileResolver (XDate.newCurrentDate ());
	}

	public FileResolver backupFileResolver (XDate date) {
		return new BackupFileResolver (date);
	}

}
