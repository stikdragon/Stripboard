package uk.co.stikman.strip.client;

public class ToolUIHint {

	private String	desc;
	private String	key;

	public ToolUIHint(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public String getKey() {
		return key;
	}

}
