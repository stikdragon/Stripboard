package uk.co.stikman.strip.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.lighti.clipper.Clipper;
import de.lighti.clipper.Clipper.ClipType;
import de.lighti.clipper.Clipper.EndType;
import de.lighti.clipper.Clipper.JoinType;
import de.lighti.clipper.Clipper.PolyType;
import de.lighti.clipper.ClipperOffset;
import de.lighti.clipper.DefaultClipper;
import de.lighti.clipper.Path;
import de.lighti.clipper.Paths;
import de.lighti.clipper.Point.LongPoint;
import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector2;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.ComponentPoly;
import uk.co.stikman.strip.client.model.ComponentPolyType;
import uk.co.stikman.strip.client.model.PinInstance;
import uk.co.stikman.strip.client.util.Poly;

public class ComponentRenderer {

	private Map<VertCacheKey, float[]>	polyCache	= new HashMap<>();
	private Stripboard					app;
	private AppTheme					theme;
	private static final Matrix3		tmpm		= new Matrix3();
	private static final Vector2		tv1			= new Vector2();
	private static final Vector2		tv2			= new Vector2();

	public ComponentRenderer(Stripboard app) {
		this.app = app;
		this.theme = app.getTheme();
	}

	/**
	 * it's valid to pass x1==x0 and y1==y0, which might mean the component isn't
	 * rendered at all (eg. it's a resistor that needs dragging first)
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
	public void render(Stripboard app, ComponentInstance comp, int x0, int y0, RenderState state) {
		RenderIntf ctx = app.getRenderer();

		List<ComponentPoly> polys = comp.getComponent().getPolys(comp);
		if (polys == null || polys.isEmpty()) {
			missing(ctx, comp, x0, y0, state);
		} else {
			tmpm.makeTranslation(x0, y0);

			for (PinInstance pin : comp.getPins())
				ctx.drawPin(pin.getPosition().x, pin.getPosition().y, state);

			if (state == RenderState.OUTLINE) {
				//
				// generate a union poly, then an outline for it
				//
				Poly res = comp.getOutlinePoly();
				ctx.drawPoly(null, app.getTheme().getHighlightColour().css(), tmpm, res);

			} else {
				for (ComponentPoly p : polys) {
					switch (p.getType()) {
						case CLOSED:
							ctx.drawPoly(app.getTheme().getComponentFill().css(), app.getTheme().getComponentOutline().css(), tmpm, p);
							break;

						case OPEN:
							// 
							// a line (or lead maybe?)
							//
							float[] a = p.getVerts();
							ctx.drawLead(a[0], a[1], a[2], a[3], tmpm);
							break;
					}
				}
			}
		}
	}

	private void missing(RenderIntf ctx, ComponentInstance comp, int x0, int y0, RenderState state) {
		Matrix3 xm = tmpm.makeIdentity();
		xm.translate(x0, y0);

		for (PinInstance p : comp.getPins())
			ctx.drawPin(p.getPosition().x, p.getPosition().y, state);

		//
		// if there's only two pins then draw a single line instead of a poly
		//
		if (comp.getPins().size() == 2) {
			PinInstance a = comp.getPins().get(0);
			PinInstance b = comp.getPins().get(1);
			tv1.set(a.getPosition().x + 0.5f, a.getPosition().y + 0.5f);
			tv2.set(b.getPosition().x + 0.5f, b.getPosition().y + 0.5f);
			ctx.drawLine(theme.getErrorColour(), tv1, tv2, xm, true);
		} else if (comp.getPins().size() > 2) {
			for (int i = 0; i < comp.getPins().size(); ++i) {
				PinInstance a = comp.getPins().get(i == 0 ? comp.getPins().size() - 1 : i - 1);
				PinInstance b = comp.getPins().get(i);
				tv1.set(a.getPosition().x + 0.5f, a.getPosition().y + 0.5f);
				tv2.set(b.getPosition().x + 0.5f, b.getPosition().y + 0.5f);
				ctx.drawLine(theme.getErrorColour(), tv1, tv2, xm, true);
			}
		}

	}

	public static float[] expandPoly(float[] poly, float amt) {
		//
		// for some reason clipper works in integer space, so we're going to multiply through by 1000 for 
		// some more res...
		//
		ClipperOffset clip = new ClipperOffset();
		Path p = new Path();
		for (int i = 0; i < poly.length; i += 2)
			p.add(new LongPoint((long) (poly[i] * 1000.0), (long) (poly[i + 1] * 1000.0)));
		clip.addPath(p, JoinType.ROUND, EndType.CLOSED_POLYGON);
		Paths res = new Paths();
		clip.execute(res, 1000 * amt);
		Path p1 = res.get(0);
		float[] out = new float[p1.size() * 2];
		int i = 0;
		for (LongPoint lp : p1) {
			out[i++] = lp.getX() / 1000.0f;
			out[i++] = lp.getY() / 1000.0f;
		}
		return out;
	}

	public final Map<VertCacheKey, float[]> getPolyCache() {
		return polyCache;
	}

}
