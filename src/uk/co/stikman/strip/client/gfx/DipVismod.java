package uk.co.stikman.strip.client.gfx;

import java.util.Map;

import uk.co.stikman.strip.client.AppTheme;
import uk.co.stikman.strip.client.ComponentRenderer;
import uk.co.stikman.strip.client.RenderIntf;
import uk.co.stikman.strip.client.RenderState;
import uk.co.stikman.strip.client.Stripboard;
import uk.co.stikman.strip.client.VertCacheKey;
import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.model.Component;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.PinInstance;

public class DipVismod implements ComponentVisualModel {

	private Map<VertCacheKey, float[]>	polyCache;
	private VertCacheKey				tKeyTmp	= new VertCacheKey(null);
	private Matrix3						tmpm	= new Matrix3();

	public DipVismod(ComponentRenderer owner) {
		this.polyCache = owner.getPolyCache();
	}

	@Override
	public float[] outline(ComponentInstance comp) {
		Component model = comp.getComponent();
		float[] verts = generateVerts(comp.getComponent());
		float[] res = polyCache.get(tKeyTmp.set(model, "outline"));
		if (res == null) {
			res = ComponentRenderer.expandPoly(verts, Stripboard.EXPAND_AMOUNT);
			polyCache.put(new VertCacheKey(model, "outline"), res);
		}
		return res;
	}

	private float[] generateVerts(Component model) {
		Vector2i sz = model.getSize();

		float[] verts = polyCache.get(tKeyTmp.set(model));
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
			polyCache.put(new VertCacheKey(model), verts);
		}
		return verts;
	}

	@Override
	public void render(RenderIntf ctx, ComponentInstance comp, int x0, int y0, RenderState state) {

		//
		// work out the bounding rect, draw that
		//
		Component model = comp.getComponent();
		Vector2i sz = model.getSize();

		for (PinInstance p : comp.getPins())
			ctx.drawPin(p.getPosition().x, p.getPosition().y, state);

		AppTheme th = ctx.getApp().getTheme();
		tmpm.makeTranslation(x0, y0);
		float[] verts = generateVerts(comp.getComponent());

		switch (state) {
		case ERROR:
			String c = th.getErrorColour().css();
			ctx.drawPoly(c, c, tmpm, verts);
			break;
		case GHOST:
			c = th.getGhostColour().css();
			ctx.drawPoly(c, c, tmpm, verts);
			break;
		case NORMAL:
			ctx.drawPoly(th.getComponentFill().css(), th.getComponentOutline().css(), tmpm, verts);
			break;
		case OUTLINE:
			ctx.drawPoly(null, th.getComponentOutline().css(), tmpm, outline(comp));
			break;
		}
	}
}