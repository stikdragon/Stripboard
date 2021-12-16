package uk.co.stikman.strip.client.model.components;

import java.util.ArrayList;
import java.util.List;

import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector2;
import uk.co.stikman.strip.client.model.Board;
import uk.co.stikman.strip.client.model.Component;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.ComponentPoly;
import uk.co.stikman.strip.client.model.ComponentPolyType;
import uk.co.stikman.strip.client.model.ComponentType;
import uk.co.stikman.strip.client.model.PolyCache;

public class ComponentResistor extends Component {

	//@formatter:off
	private static float[] RESISTOR_VERTS = new float[] {
			-17,0,		
			-17,2,		
			-15,5,
			-12,6,
			-7,6,
			-4,4,
			0,4,
			4,4,
			7,6,
			12,6,
			15,5,
			17,2,
			17,0,
			17,-1,
			15,-4,
			12,-5,
			7,-5,
			4,-3,
			0,-3,
			-4,-3,
			-7,-5,
			-12,-5,
			-15,-4,
			-17,-1	
	};
	//@formatter:on

	private static final Matrix3	tmpm			= new Matrix3();
	private static final float		SCALE_FACTOR	= 3.0f / 34.0f;	// the coordinates for the resistor i drew are far too big

	static {
		for (int i = 0; i < RESISTOR_VERTS.length; ++i)
			RESISTOR_VERTS[i] *= SCALE_FACTOR;
		// :)
	}

	public ComponentResistor(String group, String name) {
		super(group, name);
	}

	@Override
	public ComponentType getType() {
		return ComponentType.R;
	}

	@Override
	public boolean containsPoint(ComponentInstance inst, int x0, int y0, int delta) {
		return false;
	}

	@Override
	public List<ComponentPoly> getPolys(ComponentInstance inst) {
		Board brd = inst.getBoard();

		PolyCache cache = brd.getPolyCache();
		List<ComponentPoly> polys = cache.get(inst);
		if (polys == null) {
			Vector2 p0 = new Vector2(inst.getPin(0).getPosition());
			Vector2 p1 = new Vector2(inst.getPin(1).getPosition());
			float dx = p1.x - p0.x;
			float dy = p1.y - p0.y;
			float mu = (float) Math.sqrt(dx * dx + dy * dy);
			float udx = 0;
			float udy = 0;
			if (mu > 0.0f) {
				udx = dx / mu;
				udy = dy / mu;
			}

			Matrix3 xm = tmpm.makeIdentity();
			xm.translate(0.5f + dx / 2.0f, 0.5f + dy / 2.0f); // in middle of lead

			if (mu > 0.0f)
				xm.rotate(dy / mu, dx / mu); // normalise for rotation vector

			polys = new ArrayList<>();

			//
			// generate poly for body
			//
			Vector2 v = new Vector2();
			Vector2 out = new Vector2();
			float[] verts = new float[RESISTOR_VERTS.length];
			for (int i = 0; i < RESISTOR_VERTS.length; i += 2) {
				v.set(RESISTOR_VERTS[i], RESISTOR_VERTS[i + 1]);
				xm.multiply(v, out);
				verts[i] = out.x;
				verts[i + 1] = out.y;
			}
			polys.add(new ComponentPoly(ComponentPolyType.CLOSED, verts));

			//
			// so leg from p1->[0,1]  and [24,25]->p0
			//
			float[] leg = new float[4];
			leg[0] = dx + 0.5f;
			leg[1] = dy + 0.5f;
			leg[2] = verts[24] - udx * 0.1f;
			leg[3] = verts[25] - udy * 0.1f;
			polys.add(new ComponentPoly(ComponentPolyType.OPEN, leg));

			leg = new float[4];
			leg[0] = verts[0] + udx * 0.1f;
			leg[1] = verts[1] + udy * 0.1f;
			leg[2] = 0.5f;
			leg[3] = 0.5f;
			polys.add(new ComponentPoly(ComponentPolyType.OPEN, leg));

			cache.put(inst, polys);
		}

		return polys;
	}

}
