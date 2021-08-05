package uk.co.stikman.strip.client;

import uk.co.stikman.strip.client.math.Vector2i;

public class ErrorMarker {
	private Vector2i position = new Vector2i();

	public final Vector2i getPosition() {
		return position;
	}

	public final void setPosition(Vector2i position) {
		this.position.set(position);
	}
}
