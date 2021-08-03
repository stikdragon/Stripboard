package uk.co.stikman.strip.client;

import java.util.HashMap;
import java.util.Map;

import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector2;
import uk.co.stikman.strip.client.model.Component;
import uk.co.stikman.strip.client.model.ComponentType;

public class ComponentRenderer {

	private interface ComponentRendererMethod {
		void go(RenderIntf ctx, Component comp, int x0, int y0, int x1, int y1);
	}

	private static Map<ComponentType, ComponentRendererMethod>	lkp		= new HashMap<>();
	private static final Matrix3								tmpm	= new Matrix3();

	static {
		lkp.put(ComponentType.R, ComponentRenderer::resistor);
	}

	//@formatter:off
	private static float[] RESISTOR_VERTS = new float[] {
			-17,-1,
			-15,-4,
			-12,-5,
			-7,-5,
			-4,-3,
			0,-3,
			4,-3,
			7,-5,
			12,-5,
			15,-4,
			17,-1,
			17,2,
			15,5,
			12,6,
			7,6,
			4,4,
			0,4,
			-4,4,
			-7,6,
			-12,6,
			-15,5,
			-17,2			
	};
	//@formatter:on

	/**
	 * it's valid to pass x1==x0 and y1==y0, which might mean the component
	 * isn't rendered at all (eg. it's a resistor that needs dragging first)
	 * 
	 * @param ctx
	 * @param view
	 * @param comp
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 */
	public static void render(Stripboard app, Component comp, int x0, int y0, int x1, int y1) {
		RenderIntf r = app.getRenderer();
		ComponentRendererMethod m = lkp.get(comp.getType());
		if (m == null)
			m = ComponentRenderer::missing;
		m.go(r, comp, x0, y0, x1, y1);
	}

	private static void missing(RenderIntf ctx, Component comp, int x0, int y0, int x1, int y1) {
	}

	private static void resistor(RenderIntf ctx, Component comp, int x0, int y0, int x1, int y1) {

		ctx.drawPin(x0, y0);
		if (!(x0 == x1 && y0 == y1)) {
			ctx.drawPin(x1, y1);

			float dx = x1 - x0;
			float dy = y1 - y0;

			Matrix3 xm = tmpm.makeIdentity();
			xm.translate(0.5f + (x0 + x1) / 2.0f, 0.5f + (y0 + y1) / 2.0f); // in middle of lead
			xm.scale(3.0f / 34.0f); // make it 3 units long

			float mu = (float) Math.sqrt(dx * dx + dy * dy);
			if (mu > 0.0f)
				xm.rotate(dy / mu, dx / mu); // normalise for rotation vector

			ctx.drawLead(x0, y0, x1, y1);
			if (dx * dx + dy * dy >= 3 * 3) { // flat
				ctx.drawPoly(ctx.getApp().getTheme().getComponentFill().css(), xm, RESISTOR_VERTS);
			} else { // upright

			}

		}
	}

}
