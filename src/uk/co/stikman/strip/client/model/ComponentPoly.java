package uk.co.stikman.strip.client.model;

import uk.co.stikman.strip.client.util.Poly;

public class ComponentPoly extends Poly {
	private ComponentPolyType type;

	public ComponentPoly(ComponentPolyType type) {
		super(null);
		this.type = type;
	}

	public ComponentPoly(ComponentPolyType type, float[] verts) {
		super(verts);
		this.type = type;
	}

	public final ComponentPolyType getType() {
		return type;
	}

	public final void setType(ComponentPolyType type) {
		this.type = type;
	}

}
