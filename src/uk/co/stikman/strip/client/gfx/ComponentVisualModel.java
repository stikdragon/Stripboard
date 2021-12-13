package uk.co.stikman.strip.client.gfx;

import uk.co.stikman.strip.client.RenderIntf;
import uk.co.stikman.strip.client.RenderState;
import uk.co.stikman.strip.client.model.ComponentInstance;

public interface ComponentVisualModel {
	void render(RenderIntf ctx, ComponentInstance comp, int x0, int y0, RenderState state);

	/**
	 * return a polygon that outlines this
	 * 
	 * @param comp
	 * @return
	 */
	float[] outline(ComponentInstance comp);

	/**
	 * return true if point x0,y0 is inside this component, +delta
	 * 
	 * @param comp
	 * @param x0
	 * @param y0
	 * @param delta
	 * @return
	 */
	boolean contains(ComponentInstance comp, int x0, int y0, int delta);
}
