package uk.co.stikman.strip.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolyCache {
	private Map<ComponentInstance, List<ComponentPoly>> map = new HashMap<>();

	public List<ComponentPoly> get(ComponentInstance inst) {
		return map.get(inst);
	}

	public void put(ComponentInstance inst, List<ComponentPoly> polys) {
		map.put(inst, polys);
	}

}
