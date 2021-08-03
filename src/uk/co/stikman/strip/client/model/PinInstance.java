package uk.co.stikman.strip.client.model;

import uk.co.stikman.strip.client.math.Vector2i;

public class PinInstance {
	private final Pin		model;
	private final boolean	stretchy;
	private Vector2i		position	= new Vector2i();

	public final Pin getModel() {
		return model;
	}

	public PinInstance(Pin model, boolean stretchy) {
		super();
		this.model = model;
		this.stretchy = stretchy;
	}

	/**
	 * for non-stretchy components this returns its model's pin position,
	 * otherwise it returns the local one
	 * 
	 * @return
	 */
	public Vector2i getPosition() {
		if (stretchy)
			return position;
		else
			return model.getPosition();
	}

	public final void setPosition(Vector2i position) {
		this.position.set(position);
	}

}
