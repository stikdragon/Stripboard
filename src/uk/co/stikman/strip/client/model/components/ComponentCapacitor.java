package uk.co.stikman.strip.client.model.components;

import java.util.List;

import uk.co.stikman.strip.client.model.Component;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.ComponentPoly;
import uk.co.stikman.strip.client.model.ComponentType;

public class ComponentCapacitor extends Component {

	private ComponentType type;

	public ComponentCapacitor(String group, String name, ComponentType type) {
		super(group, name);
		this.type = type;
	}

	@Override
	public ComponentType getType() {
		return type;
	}

	@Override
	public boolean containsPoint(ComponentInstance inst, int x0, int y0, int delta) {
		return false;
	}

	@Override
	public List<ComponentPoly> getPolys(ComponentInstance inst) {
		return null;
	}

}
