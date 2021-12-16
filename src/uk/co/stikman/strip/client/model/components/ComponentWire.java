package uk.co.stikman.strip.client.model.components;

import java.util.ArrayList;
import java.util.List;

import uk.co.stikman.strip.client.math.Vector2;
import uk.co.stikman.strip.client.model.Board;
import uk.co.stikman.strip.client.model.Component;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.ComponentPoly;
import uk.co.stikman.strip.client.model.ComponentPolyType;
import uk.co.stikman.strip.client.model.ComponentType;
import uk.co.stikman.strip.client.model.PolyCache;

public class ComponentWire extends Component {

	public ComponentWire(String group, String name) {
		super(group, name);
	}

	@Override
	public ComponentType getType() {
		return ComponentType.WIRE;
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
			Vector2 p0 = new Vector2();
			Vector2 p4 = new Vector2(inst.getPin(1).getPosition()).sub(inst.getPin(0).getPosition());
			Vector2 d = new Vector2(p4).sub(p0).normalize();
			Vector2 p2 = new Vector2(p0).addMultiply(d, 0.5f);
			Vector2 p2_2 = new Vector2(p0).addMultiply(d, 1f);
			Vector2 p3 = new Vector2(p4).addMultiply(d, -0.5f);
			Vector2 p3_2 = new Vector2(p4).addMultiply(d, -1f);

			polys = new ArrayList<>();
			//
			// leg from p0-2, wire from p2-3, leg from p3-p1
			//
			float[] arr = new float[4];
			arr[0] = p2.x + 0.5f;
			arr[1] = p2.y + 0.5f;
			arr[2] = p3.x + 0.5f;
			arr[3] = p3.y + 0.5f;
			polys.add(new ComponentPoly(ComponentPolyType.WIRE, arr));

			arr = new float[4];
			arr[0] = p0.x + 0.5f;
			arr[1] = p0.y + 0.5f;
			arr[2] = p2_2.x + 0.5f;
			arr[3] = p2_2.y + 0.5f;
			polys.add(new ComponentPoly(ComponentPolyType.LEAD, arr));

			arr = new float[4];
			arr[0] = p3_2.x + 0.5f;
			arr[1] = p3_2.y + 0.5f;
			arr[2] = p4.x + 0.5f;
			arr[3] = p4.y + 0.5f;
			polys.add(new ComponentPoly(ComponentPolyType.LEAD, arr));

			cache.put(inst, polys);
		}
		return polys;
	}

}
