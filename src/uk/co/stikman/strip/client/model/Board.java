package uk.co.stikman.strip.client.model;

import java.util.ArrayList;
import java.util.List;

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

	public Hole getHole(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new IllegalArgumentException("Hole coord [" + x + ", " + y + "] out of range");
		return holes[y * width + x];
	}

	public boolean isValidCoord(int x, int y) {
		return (x >= 0 && y >= 0 && x < width && y < height);
	}

	public List<ComponentInstance> getComponents() {
		return components;
	}

}
