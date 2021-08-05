package uk.co.stikman.strip.client.model;

import java.util.ArrayList;
import java.util.List;

import uk.co.stikman.strip.client.math.Vector2i;

public class Component {
	private String			group;
	private String			name;
	private String			desc;
	private ComponentType	type;
	private List<Pin>		pins	= new ArrayList<>();
	private Vector2i		size	= null;

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
		case LED:
		case WIRE:
		case R:
			return true;
		default:
			return false;
		}
	}

	public final List<Pin> getPins() {
		return pins;
	}

	public Vector2i getSize() {
		return size;
	}

	public void calcSize() {
		size = new Vector2i();
		int mx = 0;
		int my = 0;
		for (Pin p : pins) {
			mx = Math.max(mx, p.getPosition().x);
			my = Math.max(my, p.getPosition().y);
		}
		size.set(mx + 1, my + 1);
	}
}
