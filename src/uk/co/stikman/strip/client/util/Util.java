package uk.co.stikman.strip.client.util;

import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector2;

public class Util {
	private static Vector2	tv	= new Vector2();
	private static Vector2	tv2	= new Vector2();

	public static native void log(String s) /*-{
		console.log(s);
	}-*/;

	public static float[] circlePoly(float radius, int vertexcount) {
		float[] res = new float[vertexcount * 2];
		for (int i = 0; i < vertexcount * 2; ++i) {
			float f = 3.14159f * 2f * i / vertexcount;
			res[i] = (float) (Math.cos(f) * radius);
			++i;
			res[i] = (float) (Math.sin(f) * radius);
		}
		return res;
	}

	public static void multiplyMat3VertV(Matrix3 m, float[] verts) {
		for (int i = 0; i < verts.length; ++i) {
			tv.x = verts[i];
			tv.y = verts[i + 1];
			m.multiply(tv, tv2);
			verts[i++] = tv2.x;
			verts[i] = tv2.y;
		}
	}
}
