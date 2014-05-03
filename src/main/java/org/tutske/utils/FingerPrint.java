package org.tutske.utils;

import java.util.UUID;

public class FingerPrint {

	private UUID uuid;
	private XDate date;

	public static FingerPrint newInstance () {
		XDate date = XDate.newCurrentDate ();
		return new FingerPrint (date);
	}

	public FingerPrint (XDate date) {
		this.uuid = UUID.randomUUID ();
		this.date = date;
	}

	public UUID getUUID () { return uuid; }
	public String getUUIDAsString () { return uuid.toString (); }

	public long getTimestamp () { return date.getTimestamp (); }
	public String getTimestampString () { return date.getTimestampString (); }
	public XDate getTime () { return date; }

}
