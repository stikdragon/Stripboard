package uk.co.stikman.strip.client.model;

public class Hole {
	private boolean	broken	= false;
	private Pin		pin		= null;

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

}
