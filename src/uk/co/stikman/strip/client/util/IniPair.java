package uk.co.stikman.strip.client.util;

public class IniPair {
	private String	name;
	private String	value;

	public IniPair(String key, String val) {
		this.name = key;
		this.value = val;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getValue() {
		return value;
	}

	public final void setValue(String value) {
		this.value = value;
	}
}
