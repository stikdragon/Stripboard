package uk.co.stikman.strip.client.model.components;

import java.util.Collections;
import java.util.List;

import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.model.Component;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.ComponentPoly;
import uk.co.stikman.strip.client.model.ComponentPolyType;
import uk.co.stikman.strip.client.model.ComponentType;

public class ComponentDip extends Component {

	private float[] verts;

	public ComponentDip(String group, String name) {
		super(group, name);
	}

	@Override
	public ComponentType getType() {
		return ComponentType.IC_DIP;
	}

	
	private float[] generateVerts() {
		Vector2i sz = getSize();

		if (verts == null) {
			//
			// generate rect for this one
			//
			verts = new float[14];
			int i = 0;
			final float SHRINKX = 0.4f; // chip body should be smaller than the pins 
			final float SHRINKY = 0.1f;
			//@formatter:off
				verts[i++] = SHRINKX;            verts[i++] = SHRINKY;
				verts[i++] = sz.x / 2.0f - 0.5f; verts[i++] = SHRINKY;
				verts[i++] = sz.x / 2.0f;        verts[i++] = 0.5f;
				verts[i++] = sz.x / 2.0f + 0.5f; verts[i++] = SHRINKY;
				verts[i++] = sz.x - SHRINKX;     verts[i++] = SHRINKY;
				verts[i++] = sz.x - SHRINKX;     verts[i++] = sz.y - SHRINKY;
				verts[i++] = SHRINKX;            verts[i++] = sz.y - SHRINKY;
			//@formatter:on
		}
		return verts;
	}
	

	
	@Override
	public boolean containsPoint(ComponentInstance inst, int x0, int y0, int delta) {
		if (inst.getComponent()!= this)
			throw new IllegalArgumentException("Comp inst does not match");
		Vector2i pos = inst.getPin(0).getPosition();
		Vector2i sz = getSize();
		x0 -= pos.x;
		y0 -= pos.y;
		if (x0 >= 0 && y0 >= 0 && x0 <= sz.x && y0 <= sz.y)
			return true;
		return false;
	}

	@Override
	public List<ComponentPoly> getPolys(ComponentInstance inst) {
		ComponentPoly p = new ComponentPoly(ComponentPolyType.BODY);
		p.setVerts(generateVerts());
		return Collections.singletonList(p);
	}
	
}
