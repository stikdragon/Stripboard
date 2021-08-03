package uk.co.stikman.strip.client.util;

public class Util {
	public static native void log(String s) /*-{
		console.log(s);
	}-*/;
}
