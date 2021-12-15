package uk.co.stikman.strip.client.model;

import uk.co.stikman.strip.client.math.Vector2i;

public class PinInstance {
	private final Pin			model;
	private final boolean		stretchy;
	private Vector2i			position	= new Vector2i();
	private ComponentInstance	owner;

	public PinInstance(ComponentInstance owner, Pin model, boolean stretchy) {
		super();
		this.owner = owner;
		this.model = model;
		this.stretchy = stretchy;
	}

	/**
	 * for non-stretchy components this returns its model's pin position
	 * relative to pin 0, otherwise it returns the local one
	 * 
	 * @return
	 */
	public Vector2i getPosition() {
		if (stretchy) {
			return position;
		} else {
			PinInstance a = owner.getPin(0);
			if (a == this)
				return position;
			return position.set(model.getPosition()).add(a.getPosition());
		}
	}

	public final void setPosition(Vector2i position) {
		this.position.set(position);
		if (owner!=null)
			owner.pinChanged(this);
	}

	public final ComponentInstance getComponentInstance() {
		return owner;
	}

	/**
	 * the {@link Pin} in the model for this component
	 * 
	 * @return
	 */
	public final Pin getModel() {
		return model;
	}

}
