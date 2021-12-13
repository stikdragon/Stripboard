package uk.co.stikman.strip.client.model;

public class ComponentPoly {
	private ComponentPolyType	type;
	private float[]				verts;

	public ComponentPoly(ComponentPolyType type) {
		super();
		this.type = type;
	}

	public ComponentPoly(ComponentPolyType type, float[] verts) {
		super();
		this.type = type;
		this.verts = verts;
	}

	public final ComponentPolyType getType() {
		return type;
	}

	public final float[] getVerts() {
		return verts;
	}

	public final void setType(ComponentPolyType type) {
		this.type = type;
	}

	public final void setVerts(float[] verts) {
		this.verts = verts;
	}
}
