package uk.co.stikman.strip.client.util;

public class Colour {
	private int	r;
	private int	g;
	private int	b;
	private int	a;

	public Colour(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public Colour(int r, int g, int b, int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public Colour(String rgbhex) {
		if (rgbhex.length() != 6 && rgbhex.length()!=8)
			throw new IllegalArgumentException("Expected RRGGBB[AA]");
		r = Integer.parseInt(rgbhex.substring(0, 2), 16);
		g = Integer.parseInt(rgbhex.substring(2, 4), 16);
		b = Integer.parseInt(rgbhex.substring(4, 6), 16);
		if (rgbhex.length() == 8)
			a = Integer.parseInt(rgbhex.substring(6, 8), 16);
	}

	public final int getR() {
		return r;
	}

	public final void setR(int r) {
		this.r = r;
	}

	public final int getG() {
		return g;
	}

	public final void setG(int g) {
		this.g = g;
	}

	public final int getB() {
		return b;
	}

	public final void setB(int b) {
		this.b = b;
	}

	public final int getA() {
		return a;
	}

	public final void setA(int a) {
		this.a = a;
	}

	public String css() {
		if (a == 0)
			return "#" + hex2(r) + hex2(g) + hex2(b);
		return "#" + hex2(r) + hex2(g) + hex2(b) + hex2(a);
	}

	private static String hex2(int v) {
		if (v <= 0xf)
			return "0" + Integer.toHexString(v);
		return Integer.toHexString(v);
	}

}
