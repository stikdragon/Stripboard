package uk.co.stikman.strip.client.model;

import java.util.ArrayList;
import java.util.List;

import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector2i;

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
	private List<PinInstance>	pins		= new ArrayList<>();
	private int					rotation	= 0;				// 0,1,2,3
	private Matrix3				tmpM		= new Matrix3();
	private Board				board;

	public ComponentInstance(Board board, Component component) {
		super();
		this.board = board;
		this.component = component;
		for (Pin p : component.getPins())
			pins.add(new PinInstance(this, p, component.isStretchy()));
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
		// TODO: rotate
		PinInstance one = pins.get(0);
		for (int i = 1; i < pins.size(); ++i)
			pins.get(i).getPosition().set(one.getPosition().x, one.getPosition().y).add(component.getPins().get(i).getPosition());
	}

	public final List<PinInstance> getPins() {
		return pins;
	}

	public boolean containsPoint(int x0, int y0, int delta) {
		return getComponent().containsPoint(this, x0, y0, delta);
	}

	public final Board getBoard() {
		return board;
	}

}
