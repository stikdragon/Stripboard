package uk.co.stikman.strip.client;

import java.util.HashMap;
import java.util.Map;

import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector2;
import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.model.Component;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.ComponentType;
import uk.co.stikman.strip.client.model.PinInstance;

public class ComponentRenderer {

	private interface ComponentRendererMethod {
		void go(RenderIntf ctx, ComponentInstance comp, int x0, int y0, boolean ghost);
	}

	private static Map<ComponentType, ComponentRendererMethod>	lkp			= new HashMap<>();
	private static final Matrix3								tmpm		= new Matrix3();
	private static Map<Component, float[]>						vertCache	= new HashMap<>();

	static {
		lkp.put(ComponentType.R, ComponentRenderer::resistor);
		lkp.put(ComponentType.IC_DIP, ComponentRenderer::dip);
		lkp.put(ComponentType.WIRE, ComponentRenderer::wire);
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
	 * @param ghost
	 * @param x1
	 * @param y1
	 */
	public static void render(Stripboard app, ComponentInstance comp, int x0, int y0, boolean ghost) {
		RenderIntf r = app.getRenderer();
		ComponentRendererMethod m = lkp.get(comp.getComponent().getType());
		if (m == null)
			m = ComponentRenderer::missing;
		m.go(r, comp, x0, y0, ghost);
	}

	private static void missing(RenderIntf ctx, ComponentInstance comp, int x0, int y0, boolean ghost) {
	}

	private static void resistor(RenderIntf ctx, ComponentInstance comp, int x0, int y0, boolean ghost) {
		Vector2i p = comp.getPin(1).getPosition(); // position in component

		int x1 = p.x;
		int y1 = p.y;

		if (!(x0 == x1 && y0 == y1)) {
			if (!ghost) {
				ctx.drawPin(x0, y0);
				ctx.drawPin(x1, y1);
			}

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

	private static void wire(RenderIntf ctx, ComponentInstance comp, int x0, int y0, boolean ghost) {
		PinInstance p1 = comp.getPin(0);
		PinInstance p2 = comp.getPin(1);

		if (!(x0 == p2.getPosition().x && y0 == p2.getPosition().y)) {
			ctx.drawPin(p1.getPosition().x, p1.getPosition().y);
			ctx.drawPin(p2.getPosition().x, p2.getPosition().y);
			ctx.drawWire(p1.getPosition(), p2.getPosition());
		}
	}

	private static void dip(RenderIntf ctx, ComponentInstance comp, int x0, int y0, boolean ghost) {
		//
		// work out the bounding rect, draw that
		//
		Component model = comp.getComponent();
		Vector2i sz = model.getSize();

		if (!ghost) {
			for (PinInstance p : comp.getPins())
				ctx.drawPin(p.getPosition().x, p.getPosition().y);
		}

		float[] verts = vertCache.get(model);
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
			vertCache.put(model, verts);
		}
		tmpm.makeTranslation(x0, y0);
		ctx.drawPoly(ctx.getApp().getTheme().getComponentFill().css(), tmpm, verts);
	}
}
