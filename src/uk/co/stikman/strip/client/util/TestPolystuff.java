package uk.co.stikman.strip.client.util;

import uk.co.stikman.strip.client.math.Vector2;

public class TestPolystuff {
	public static void main(String[] args) {

		float[] verts = new float[] {
				10, 10,
				20, 10,
				20, 20, 
				10, 20
		};
		
		Poly poly = new Poly(verts);

		System.out.println(poly.contains(new Vector2(15, 15)));
		
		
	}
}
