package uk.co.stikman.strip.client.model;

import java.util.ArrayList;
import java.util.List;

import uk.co.stikman.strip.client.json.JSONObject;
import uk.co.stikman.strip.client.math.Vector2;
import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.util.Poly;

public class Board {
	private int						width;
	private int						height;
	private Hole[]					holes;
	private List<ComponentInstance>	components	= new ArrayList<>();
	private PolyCache				polyCache	= new PolyCache();
	private boolean					modified;
	private String					filename;

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		holes = new Hole[width * height];
		for (int i = 0; i < holes.length; ++i)
			holes[i] = new Hole(i % width, i / width);
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	public Hole getHole(Vector2i v) {
		return getHole(v.x, v.y);
	}

	public Hole getHole(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new IllegalArgumentException("Hole coord [" + x + ", " + y + "] out of range");
		return holes[y * width + x];
	}

	public boolean isValidCoord(Vector2i v) {
		return (v.x >= 0 && v.y >= 0 && v.x < width && v.y < height);
	}

	public boolean isValidCoord(int x, int y) {
		return (x >= 0 && y >= 0 && x < width && y < height);
	}

	public List<ComponentInstance> getComponents() {
		return components;
	}

	public void placeComponent(ComponentInstance inst) {
		components.add(inst);
		for (PinInstance p : inst.getPins())
			getHole(p.getPosition().x, p.getPosition().y).addPin(p);
		modified = true;
	}

	/**
	 * fill the provided list with {@link HitResult} instances, indicating
	 * what's under the cursor.
	 * 
	 * @param pos
	 * @param results
	 */
	public void findThingsUnder(Vector2 pos, List<HitResult> results, float distance) {
		Vector2i v = new Vector2i(pos);
		if (!isValidCoord(v))
			return;
		Hole h = getHole(v);
		for (PinInstance p : h.getPins()) {
			HitResult hr = new HitResult(HitResultType.PIN_INSTANCE, p);
			results.add(hr);
		}

		//
		// look for components too
		//
		Vector2 p = new Vector2();
		for (ComponentInstance ci : components) {
			Poly poly = ci.getOutlinePoly();
			if (poly != null) {
				p.set(pos.x, pos.y).sub(ci.getPin(0).getPosition());
				if (poly.contains(p))
					results.add(new HitResult(HitResultType.COMPONENT_INSTANCE, ci));
			}
		}

	}

	public PolyCache getPolyCache() {
		return polyCache;
	}

	public boolean isModified() {
		return modified;
	}

	public final void setModified(boolean modified) {
		this.modified = modified;
	}

	public String getFilename() {
		return filename;
	}

	public final void setFilename(String filename) {
		this.filename = filename;
	}

	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * make sure all holes have the correct things in
	 */
	public void updatePinPositions() {
		for (Hole h : holes)
			h.clearPins();
		for (ComponentInstance ci : components)
			for (PinInstance pi : ci.getPins())
				getHole(pi.getPosition()).addPin(pi);
	}

}
