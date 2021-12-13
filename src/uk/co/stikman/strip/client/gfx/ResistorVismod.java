package uk.co.stikman.strip.client.gfx;

import uk.co.stikman.strip.client.AppTheme;
import uk.co.stikman.strip.client.ComponentRenderer;
import uk.co.stikman.strip.client.RenderIntf;
import uk.co.stikman.strip.client.RenderState;
import uk.co.stikman.strip.client.Stripboard;
import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.model.ComponentInstance;

public class ResistorVismod implements ComponentVisualModel {

	//@formatter:off
	private static float[] RESISTOR_VERTS = new float[] {
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

	private static float SCALE_FACTOR = 3.0f / 34.0f; // the coordinates i drew are far too big
	
	private static final Matrix3	tmpm			= new Matrix3();

	private float[]					outline;

	public ResistorVismod(ComponentRenderer owner) {
	}

	@Override
	public float[] outline(ComponentInstance comp) {
		if (outline == null) 
			outline = ComponentRenderer.expandPoly(RESISTOR_VERTS, Stripboard.EXPAND_AMOUNT / SCALE_FACTOR);
		return outline;
	}

	@Override
	public void render(RenderIntf ctx, ComponentInstance comp, int x0, int y0, RenderState state) {
		Vector2i p = comp.getPin(1).getPosition(); // position in component

		int x1 = p.x;
		int y1 = p.y;

		if (!(x0 == x1 && y0 == y1)) {
			ctx.drawPin(x0, y0, state);
			ctx.drawPin(x1, y1, state);

			float dx = x1 - x0;
			float dy = y1 - y0;

			Matrix3 xm = tmpm.makeIdentity();
			xm.translate(0.5f + (x0 + x1) / 2.0f, 0.5f + (y0 + y1) / 2.0f); // in middle of lead
			xm.scale(SCALE_FACTOR); // make it 3 units long

			float mu = (float) Math.sqrt(dx * dx + dy * dy);
			if (mu > 0.0f)
				xm.rotate(dy / mu, dx / mu); // normalise for rotation vector

			ctx.drawLead(x0, y0, x1, y1);
			if (dx * dx + dy * dy >= 3 * 3) { // flat
				AppTheme th = ctx.getApp().getTheme();
				switch (state) {
				case ERROR:
					String c = th.getErrorColour().css();
					ctx.drawPoly(c, c, xm, RESISTOR_VERTS);
					break;
				case GHOST:
					c = th.getGhostColour().css();
					ctx.drawPoly(c, c, xm, RESISTOR_VERTS);
					break;
				case NORMAL:
					ctx.drawPoly(th.getComponentFill().css(), th.getComponentOutline().css(), xm, RESISTOR_VERTS);
					break;
				case OUTLINE:
					ctx.drawPoly(null, th.getComponentOutline().css(), tmpm, outline(comp));
					break;
				}
			} else { // upright
				// TODO: upright resistors
			}

		}

	}
	
	@Override
	public boolean contains(ComponentInstance comp, int x0, int y0, int delta) {
		return false;
	}
	
}