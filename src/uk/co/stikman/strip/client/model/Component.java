package uk.co.stikman.strip.client.model;

public class Component {
	private String			group;
	private String			name;
	private String			desc;
	private ComponentType	type;

	public Component(String group, String name) {
		super();
		this.group = group;
		this.name = name;
		
	}

	public final String getGroup() {
		return group;
	}

	public final String getName() {
		return name;
	}

	public final String getDesc() {
		return desc;
	}

	public final void setDesc(String desc) {
		this.desc = desc;
	}

	public final ComponentType getType() {
		return type;
	}

	public final void setType(ComponentType type) {
		this.type = type;
	}

}
