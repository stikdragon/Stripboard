package uk.co.stikman.strip.client.model;

import uk.co.stikman.strip.client.math.Vector2i;

public class Pin {
	private Component	owner;
	private int			id;
	private Vector2i	position	= new Vector2i();

	public final Component getOwner() {
		return owner;
	}

	public Pin(Component owner, int id) {
		super();
		this.owner = owner;
		this.id = id;
	}

	public final int getId() {
		return id;
	}

	public Vector2i getPosition() {
		return position;
	}
}
