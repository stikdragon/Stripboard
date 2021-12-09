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

	private static final class VertCacheKey {
		Component	component;
		String		extra;

		public VertCacheKey(Component component) {
			this.component = component;
			this.extra = null;
		}

		public VertCacheKey(Component component, String extra) {
			super();
			this.component = component;
			this.extra = extra;
		}

		public VertCacheKey set(Component component) {
			this.component = component;
			this.extra = null;
			return this;
		}

		public VertCacheKey set(Component component, String extra) {
			this.component = component;
			this.extra = extra;
			return this;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((component == null) ? 0 : component.hashCode());
			result = prime * result + ((extra == null) ? 0 : extra.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			VertCacheKey other = (VertCacheKey) obj;
			if (component == null) {
				if (other.component != null)
					return false;
			} else if (!component.equals(other.component))
				return false;
			if (extra == null) {
				if (other.extra != null)
					return false;
			} else if (!extra.equals(other.extra))
				return false;
			return true;
		}

	}

	private interface ComponentRendererMethod {
		void go(RenderIntf ctx, ComponentInstance comp, int x0, int y0, RenderState state);

		/**
		 * return a polygon that outlines this
		 * 
		 * @param comp
		 * @return
		 */
		float[] outline(ComponentInstance comp);
	}

	private static Map<ComponentType, ComponentRendererMethod>	lkp				= new HashMap<>();
	private static final Matrix3								tmpm			= new Matrix3();
	protected static final float								EXPAND_AMOUNT	= 0.2f;
	private static Map<VertCacheKey, float[]>					vertCache		= new HashMap<>();

	static {
		lkp.put(ComponentType.R, ComponentRenderer.resistor());
		lkp.put(ComponentType.IC_DIP, ComponentRenderer.dip());
		lkp.put(ComponentType.WIRE, ComponentRenderer.wire());
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
	public static void render(Stripboard app, ComponentInstance comp, int x0, int y0, RenderState state) {
		RenderIntf ctx = app.getRenderer();
		ComponentRendererMethod m = lkp.get(comp.getComponent().getType());
		if (m == null)
			missing(ctx, comp, x0, y0, state);
		else
			m.go(ctx, comp, x0, y0, state);
	}

	private static void missing(RenderIntf ctx, ComponentInstance comp, int x0, int y0, RenderState state) {
	}

	private static ComponentRendererMethod resistor() {
		return new ComponentRendererMethod() {
			private float[] outline;

			@Override
			public float[] outline(ComponentInstance comp) {
				if (outline == null) {
					outline = new float[RESISTOR_VERTS.length];
					System.arraycopy(RESISTOR_VERTS, 0, outline, 0, RESISTOR_VERTS.length);
					outline = expandPoly(outline, EXPAND_AMOUNT);
				}
				return outline;
			}

			@Override
			public void go(RenderIntf ctx, ComponentInstance comp, int x0, int y0, RenderState state) {
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
					xm.scale(3.0f / 34.0f); // make it 3 units long

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
						}
					} else { // upright
						// TODO: upright resistors
					}

				}

			}
		};
	}

	private static ComponentRendererMethod wire() {
		return new ComponentRendererMethod() {
			@Override
			public float[] outline(ComponentInstance comp) {
				PinInstance p1 = comp.getPin(0);
				PinInstance p2 = comp.getPin(1);
				return null;
				//Vector2 a = new Vector2(p1.getPosition();

			}

			@Override
			public void go(RenderIntf ctx, ComponentInstance comp, int x0, int y0, RenderState state) {
				PinInstance p1 = comp.getPin(0);
				PinInstance p2 = comp.getPin(1);

				if (!(x0 == p2.getPosition().x && y0 == p2.getPosition().y)) {
					ctx.drawPin(p1.getPosition().x, p1.getPosition().y, state);
					ctx.drawPin(p2.getPosition().x, p2.getPosition().y, state);
					ctx.drawWire(p1.getPosition(), p2.getPosition());
				}
			}
		};
	}

	private static final VertCacheKey tKey = new VertCacheKey(null);

	private static ComponentRendererMethod dip() {
		return new ComponentRendererMethod() {

			@Override
			public float[] outline(ComponentInstance comp) {
				Component model = comp.getComponent();
				Vector2i sz = model.getSize();
				float[] verts = generateVerts(comp);
				float[] res = vertCache.get(tKey.set(model, "outline"));
				if (res == null) {
					res = expandPoly(verts, EXPAND_AMOUNT);
					vertCache.put(new VertCacheKey(model, "outline"), res);
				}
				return res;
			}

			private float[] generateVerts(ComponentInstance comp) {
				Component model = comp.getComponent();
				Vector2i sz = model.getSize();

				float[] verts = vertCache.get(tKey.set(model));
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
					vertCache.put(new VertCacheKey(model), verts);
				}
				return verts;
			}

			@Override
			public void go(RenderIntf ctx, ComponentInstance comp, int x0, int y0, RenderState state) {

				//
				// work out the bounding rect, draw that
				//
				Component model = comp.getComponent();
				Vector2i sz = model.getSize();

				for (PinInstance p : comp.getPins())
					ctx.drawPin(p.getPosition().x, p.getPosition().y, state);

				AppTheme th = ctx.getApp().getTheme();
				tmpm.makeTranslation(x0, y0);
				float[] verts = generateVerts(comp);

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
				}
			}
		};
	}

	protected static float[] expandPoly(float[] poly, float amt) {
		//
		// find normal for each edge, then add them to get a vertex normal, adjust along them
		//
		float[] copy = new float[poly.length];
		System.arraycopy(poly, 0, copy, 0, poly.length);
		Vector2 va = new Vector2();
		Vector2 vb = new Vector2();
		Vector2 vc = new Vector2();
		Vector2 v1 = new Vector2();
		Vector2 v2 = new Vector2();
		int n = poly.length / 2;

		for (int i = 1; i <= n; ++i) {
			int a = i - 1;
			int b = i % n;
			int c = (i + 1) % n;
			va.set(poly[a * 2], poly[a * 2] + 1);
			vb.set(poly[b * 2], poly[b * 2] + 1);
			vc.set(poly[c * 2], poly[c * 2] + 1);

			v1.set(vb.y - va.y, va.x - vb.x).normalize();
			v2.set(vc.y - vb.y, vb.x - vc.x).normalize().add(v1).normalize();

			v2.multiply(amt);
			copy[b * 2] = v2.x;
			copy[b * 2 + 1] = v2.y;
		}
		return copy;
	}

}
