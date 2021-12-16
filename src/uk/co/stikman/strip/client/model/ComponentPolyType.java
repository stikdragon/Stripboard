package uk.co.stikman.strip.client.model;

public enum ComponentPolyType {
	BODY(true),
	LEAD(false),
	WIRE(false);

	private boolean ispoly;

	private ComponentPolyType(boolean ispoly) {
		this.ispoly = ispoly;
	}

	boolean isLine() {
		return !ispoly;
	}

	boolean isPoly() {
		return ispoly;
	}

}
