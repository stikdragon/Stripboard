package uk.co.stikman.strip.client.model;

import java.util.ArrayList;
import java.util.List;

import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.math.Vector3;

public class Board {
	private int						width;
	private int						height;
	private Hole[]					holes;
	private List<ComponentInstance>	components	= new ArrayList<>();

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
			getHole(p.getPosition().x, p.getPosition().y).setPin(p);
	}

	/**
	 * fill the provided list with {@link HitResult} instances, indicating
	 * what's under the cursor.
	 * 
	 * @param pos
	 * @param results
	 */
	public void findThingsUnder(Vector3 pos, List<HitResult> results, float distance) {
		Vector2i v = new Vector2i((int) pos.x, (int) pos.y);
		if (!isValidCoord(v))
			return;
		Hole h = getHole(v);
		if (h.getPin() != null) {
			HitResult hr = new HitResult(HitResultType.PIN_INSTANCE, h.getPin());
			results.add(hr);
		}

	}

}
