package uk.co.stikman.strip.client.model;

public class Hole {
	private boolean	broken	= false;
	private Pin		pin		= null;
	private int		x;
	private int		y;

	public boolean isBroken() {
		return broken;
	}

	public void setBroken(boolean broken) {
		this.broken = broken;
	}

	public Pin getPin() {
		return pin;
	}

	public void setPin(Pin pin) {
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
