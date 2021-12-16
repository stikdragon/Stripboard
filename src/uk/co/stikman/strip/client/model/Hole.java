package uk.co.stikman.strip.client.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Hole {
	private boolean				broken	= false;
	private Set<PinInstance>	pins	= null;
	private int					x;
	private int					y;

	public boolean isBroken() {
		return broken;
	}

	public void setBroken(boolean broken) {
		this.broken = broken;
	}

	public Set<PinInstance> getPins() {
		if (pins == null)
			return Collections.emptySet();
		return pins;
	}

	public void addPin(PinInstance pin) {
		if (pins == null)
			pins = new HashSet<>();
		pins.add(pin);
	}

	public final int getY() {
		return y;

	}

	public Hole(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public final int getX() {
		return x;
	}

	public void removePin(PinInstance pin) {
		if (pins != null)
			pins.remove(pin);
	}

}
