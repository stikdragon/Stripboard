package uk.co.stikman.strip.client.model;

import java.util.ArrayList;
import java.util.List;

import uk.co.stikman.strip.client.math.Matrix3;

/**
 * pin 0 sets the position of the component, the other pins can either be
 * inferred from that and the rotation (if it's a fixed layout) or manually
 * moved around (for example a resistor, or very leggy mosfet)
 * 
 * @author stikd
 *
 */
public class ComponentInstance {
	private final Component		component;
	private String				name;
	private Hole				hole;
	private List<PinInstance>	pins		= new ArrayList<>();
	private int					rotation	= 0;				// 0,1,2,3
	private Matrix3				tmpM		= new Matrix3();

	public ComponentInstance(Component component) {
		super();
		this.component = component;
		for (Pin p : component.getPins())
			pins.add(new PinInstance(p, component.isStretchy()));
	}

	public final Component getComponent() {
		return component;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ComponentInstance [component=" + component + ", name=" + name + "]";
	}

	/**
	 * return the hole pin 1 is in
	 * 
	 * @return
	 */
	public Hole getHole() {
		return hole;
	}

	public final void setHole(Hole hole) {
		this.hole = hole;
	}

	public PinInstance getPin(int idx) {
		return pins.get(idx);
	}

	public final int getRotation() {
		return rotation;
	}

	public final void setRotation(int rotation) {
		this.rotation = rotation;
	}

	/**
	 * Update all pin positions from the location of the first pin
	 */
	public void updatePinPositions() {
		if (!component.isStretchy()) {
			// TODO: rotate
			PinInstance one = pins.get(0);
			for (int i = 1; i < pins.size(); ++i)
				pins.get(i).getPosition().set(one.getPosition().x, one.getPosition().y).add(component.getPins().get(i).getPosition());
		}
	}
}
