package uk.co.stikman.strip.client.model;

public class Hole {
	private boolean		broken	= false;
	private PinInstance	pin		= null;
	private int			x;
	private int			y;

	public boolean isBroken() {
		return broken;
	}

	public void setBroken(boolean broken) {
		this.broken = broken;
	}

	public PinInstance getPin() {
		return pin;
	}

	public void setPin(PinInstance pin) {
		this.pin = pin;
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
