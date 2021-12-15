package uk.co.stikman.strip.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hole {
	private boolean				broken	= false;
	private List<PinInstance>	pins	= null;
	private int					x;
	private int					y;

	public boolean isBroken() {
		return broken;
	}

	public void setBroken(boolean broken) {
		this.broken = broken;
	}

	public List<PinInstance> getPins() {
		if (pins == null)
			return Collections.emptyList();
		return pins;
	}

	public void addPin(PinInstance pin) {
		if (pins == null)
			pins = new ArrayList<>();
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

}
