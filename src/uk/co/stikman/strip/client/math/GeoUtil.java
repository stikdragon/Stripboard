package uk.co.stikman.strip.client.math;

public class GeoUtil {
	private static final Vector2	tmpv1	= new Vector2();
	private static final Vector2	tmpv2	= new Vector2();

	/**
	 * return distance from p to line segment w-v, squared
	 * 
	 * @param v
	 * @param w
	 * @param p
	 * @return
	 */
	public static float distanceToLineSq(Vector2 v, Vector2 w, Vector2 p) {
		float mu = w.distanceToSq(v);
		if (mu == 0.0)
			return p.distanceToSq(w); // 0 length line

		Vector2 a = p.sub(v, tmpv1);
		Vector2 b = w.sub(v, tmpv2);
		mu = a.dot(b) / mu;
		if (mu < 0) // clamp to the ends
			mu = 0;
		if (mu > 1)
			mu = 1;

		Vector2 q = v.add(b.multiply(mu), tmpv1);
		return q.distanceToSq(p);
	}
}
