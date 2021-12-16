package uk.co.stikman.strip.client.model;

import java.util.ArrayList;
import java.util.List;

import de.lighti.clipper.ClipperOffset;
import de.lighti.clipper.DefaultClipper;
import de.lighti.clipper.Path;
import de.lighti.clipper.Paths;
import de.lighti.clipper.Clipper.ClipType;
import de.lighti.clipper.Clipper.EndType;
import de.lighti.clipper.Clipper.JoinType;
import de.lighti.clipper.Clipper.PolyType;
import de.lighti.clipper.Point.LongPoint;
import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.util.Poly;

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
	private Poly				outlinePoly;
	private Vector2i tv = new Vector2i();

	public ComponentInstance(Board board, Component component) {
		super();
		this.board = board;
		this.component = component;
		for (Pin p : component.getPins())
			pins.add(new PinInstance(this, p, component.isStretchy()));
		modified();
	}

	public final Component getComponent() {
		return component;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
		modified();
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
		modified();
	}

	/**
	 * Update all pin positions from the location of the first pin
	 */
	public void updatePinPositions() {
		// TODO: rotate
		PinInstance one = pins.get(0);
		for (int i = 1; i < pins.size(); ++i)
			pins.get(i).setPosition(tv.set(one.getPosition().x, one.getPosition().y).add(component.getPins().get(i).getPosition()));
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

	public void pinChanged(PinInstance pin) {
		modified();
	}

	private void modified() {
		invalidateLayout();
		getBoard().getPolyCache().remove(this);
		getBoard().setModified(true);
	}

	public void invalidateLayout() {
		this.outlinePoly = null;
	}

	/**
	 * can return <code>null</code> if it doesn't have a poly
	 * 
	 * @return
	 */
	public Poly getOutlinePoly() {
		if (outlinePoly == null)
			generateOutline();
		return outlinePoly;
	}

	private void generateOutline() {
		List<ComponentPoly> polys = getComponent().getPolys(this);
		if (polys == null || polys.isEmpty())
			return;
		DefaultClipper clip = new DefaultClipper();
		int n = 0;
		for (ComponentPoly p : polys) {
			Path pth = new Path();

			if (p.getType() == ComponentPolyType.OPEN) {
				//
				// a lead, so make it an infinitely thin polygon which satisfies clipper
				//
				float[] arr = p.getVerts();
				for (int i = 0; i < arr.length; i += 2)
					pth.add(new LongPoint((int) (arr[i] * 1000), (int) (arr[i + 1] * 1000)));
				for (int i = arr.length - 4; i >= 0; i -= 2)
					pth.add(new LongPoint((int) (arr[i] * 1000) + 10, (int) (arr[i + 1] * 1000) + 10));

			} else {
				float[] arr = p.getVerts();
				for (int i = 0; i < arr.length; i += 2)
					pth.add(new LongPoint((int) (arr[i] * 1000), (int) (arr[i + 1] * 1000)));
			}
			clip.addPath(pth, n == 0 ? PolyType.SUBJECT : PolyType.CLIP, true);
			++n;
		}
		Paths out = new Paths();
		clip.execute(ClipType.UNION, out);
		clip.clear();

		ClipperOffset off = new ClipperOffset();
		off.addPath(out.get(0), JoinType.ROUND, EndType.CLOSED_POLYGON);
		Paths out2 = new Paths();
		off.execute(out2, 500.0f);

		float[] res = new float[out2.get(0).size() * 2];
		int i = 0;
		for (LongPoint lp : out2.get(0)) {
			res[i++] = (float) lp.getX() / 1000.0f;
			res[i++] = (float) lp.getY() / 1000.0f;
		}
		outlinePoly = new Poly(res);
	}

}
