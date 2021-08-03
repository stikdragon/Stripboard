package uk.co.stikman.strip.client.model;

import java.util.ArrayList;
import java.util.List;

public class Component {
	private String			group;
	private String			name;
	private String			desc;
	private ComponentType	type;
	private List<Pin>		pins	= new ArrayList<>();

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

	public boolean isStretchy() {
		switch (type) {
		case C_AXIAL:
		case C_DISC:
		case C_RADIAL:
		case IC_DIP:
		case LED:
		case R:
			return true;
		default:
			return false;
		}
	}

	public final List<Pin> getPins() {
		return pins;
	}
}
