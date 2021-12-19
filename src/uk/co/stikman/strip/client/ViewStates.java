package uk.co.stikman.strip.client;

import java.util.HashMap;
import java.util.Map;

public class ViewStates {
	public static final String		BODY			= "body";
	public static final String		PIN				= "pin";
	public static final String		COMPONENT_NAME	= "comp_name";
	public static final String		PIN_NAME		= "pin_name";

	private Map<String, Boolean>	states			= new HashMap<>();

	public ViewStates() {
		super();
		states.put(BODY, true);
		states.put(PIN, true);
		states.put(COMPONENT_NAME, true);
		states.put(PIN_NAME, false);
	}

	public boolean isOn(String name) {
		return states.get(name).booleanValue();
	}

	public void toggle(String name) {
		states.put(name, !states.get(name).booleanValue());
	}

}
