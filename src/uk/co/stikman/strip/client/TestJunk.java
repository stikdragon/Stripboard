package uk.co.stikman.strip.client;

import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector3;

public class TestJunk {

	public static void main(String[] args) {
		Vector3 v1 = new Vector3(1.0f, 1.0f, 0.0f);
		Vector3 v2 = new Vector3(1.0f, 1.0f, 1.0f);
		
		Matrix3 m = new Matrix3();
		m.makeIdentity();
		m.translate(5, 5);
		m.scale(2);
		m.translate(-15, 8);
		
		System.out.println("v1 = " + m.multiply(v1, new Vector3()));
		System.out.println("v2 = " + m.multiply(v2, new Vector3()));
		
	}

}
