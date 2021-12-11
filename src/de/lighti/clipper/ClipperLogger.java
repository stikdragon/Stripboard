package de.lighti.clipper;

/**
 * just dumping this in to remove the dependency on java.util.logger
 * @author stikd
 *
 */
public class ClipperLogger {

	private String name;

	public ClipperLogger(String name) {
		this.name = name;
	}

	public void entering(String cls, String mthd) {
		log("Entering [" + cls + "." + mthd + "]");
	}

	private void log(String s) {
		// i really couldn't care less about the output from this lib, so not doing anything
//		Util.log(name + ": " + s);
	}

	public void finest(String s) {
		log("FINEST: " + s);
	}

	public void exiting(String cls, String mthd) {
		log("Exiting [" + cls + "." + mthd + "]");
	}
	
}
