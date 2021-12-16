package uk.co.stikman.strip.client.math;


public class Vector2 {
	public static final Vector2	ZERO	= new Vector2(0, 0);
	public static final Vector2	ONE		= new Vector2(1, 1);

	public float				x;
	public float				y;

	public Vector2() {
	}

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(Vector2 copy) {
		x = copy.x;
		y = copy.y;
	}

	public Vector2(double x, double y) {
		this((float) x, (float) y);
	}

	public Vector2(Vector2i copy) {
		this.x = copy.x;
		this.y = copy.y;
	}

	public final float length() {
		return (float) Math.sqrt((x * x) + (y * y));
	}

	public final float lengthSquared() {
		return (x * x) + (y * y);
	}

	public final Vector2 add(Vector2 v2, Vector2 dest) {
		dest.x = x + v2.x;
		dest.y = y + v2.y;
		return dest;
	}

	public final Vector2 add(Vector2 v2) {
		x += v2.x;
		y += v2.y;
		return this;
	}

	public final Vector2 sub(Vector2 v2, Vector2 dest) {
		dest.x = x - v2.x;
		dest.y = y - v2.y;
		return dest;
	}

	public final Vector2 sub(Vector2 v2) {
		x -= v2.x;
		y -= v2.y;
		return this;
	}

	/**
	 * Multiply in place
	 * 
	 * @param s
	 * @return
	 */
	public final Vector2 multiply(float s) {
		x *= s;
		y *= s;
		return this;
	}

	public final Vector2 multiply(float s, Vector2 dest) {
		dest.x = x * s;
		dest.y = y * s;
		return dest;
	}

	public final Vector2 divide(float d) {
		x /= d;
		y /= d;
		return this;
	}

	public final Vector2 divide(float d, Vector2 dest) {
		dest.x = x / d;
		dest.y = y / d;
		return dest;
	}

	public final Vector2 normalize() {
		float in = (float) (1.0f / Math.sqrt((x * x) + (y * y)));
		x *= in;
		y *= in;
		return this;
	}

	public final float dot(float vx, float vy) {
		return vx * x + vy * y;
	}
	
	public final float dot(Vector2 v) {
		return v.x * x + v.y * y;
	}

	public final Vector2 set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	public void addMultiplyLocal(Vector2 v, float scale) {
		x += v.x * scale;
		y += v.y * scale;
	}

	public float distanceToSq(Vector2 v) {
		float dx = x - v.x;
		float dy = y - v.y;
		return dx * dx + dy * dy;
	}

	public float distanceTo(Vector2 v) {
		float dx = x - v.x;
		float dy = y - v.y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
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
		Vector2 other = (Vector2) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	public Vector2 set(Vector2 copy) {
		this.x = copy.x;
		this.y = copy.y;
		return this;
	}

	public Vector2 addLocal(Vector2 v) {
		this.x += v.x;
		this.y += v.y;
		return this;
	}

	public Vector2 addLocal(float dx, float dy) {
		this.x += dx;
		this.y += dy;
		return this;
	}

	public Vector2 set(Vector2 v, float scale) {
		this.x = v.x * scale;
		this.y = v.y * scale;
		return this;
	}

	public static float distanceSq(Vector2 a, Vector2 b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;
		return dx * dx + dy * dy;
	}

	/**
	 * Returns the minimum of the given list. So it's really "vec2(min(v.x),
	 * min(v.y))"
	 * 
	 * @param vs
	 * @param out
	 * @return
	 */
	public static Vector2 min(Vector2[] vs, Vector2 out) {
		if (vs.length == 0)
			return Vector2.ZERO;
		if (vs.length == 1)
			return vs[0];
		float mx = vs[0].x;
		float my = vs[0].y;
		for (int i = 1; i < vs.length; ++i) {
			Vector2 v = vs[i];
			if (v.x < mx)
				mx = v.x;
			if (v.y < my)
				my = v.y;
		}
		return out.set(mx, my);
	}

	public Vector2 min(Vector2 v, Vector2 out) {
		float mx = Math.min(v.x, x);
		float my = Math.min(v.y, y);
		return out.set(mx, my);
	}

	/**
	 * Returns the maximum of the given list. So it's really "vec2(max(v.x),
	 * max(v.y))"
	 * 
	 * @param vs
	 * @param out
	 * @return
	 */
	public static Vector2 max(Vector2[] vs, Vector2 out) {
		if (vs.length == 0)
			return Vector2.ZERO;
		if (vs.length == 1)
			return vs[0];
		float mx = vs[0].x;
		float my = vs[0].y;
		for (int i = 1; i < vs.length; ++i) {
			Vector2 v = vs[i];
			if (v.x > mx)
				mx = v.x;
			if (v.y > my)
				my = v.y;
		}
		return out.set(mx, my);
	}

	public float cross(Vector2 v2) {
		return x * v2.y - y * v2.x;

	}

	/**
	 * Multiplies components by either other, so
	 * <code>this = [v.x * this.x, v.y * this.y]</code>
	 * 
	 * @param v
	 * @return
	 */
	public Vector2 multiply(Vector2 v) {
		x *= v.x;
		y *= v.y;
		return this;
	}

	public Vector2 add(float dx, float dy) {
		x += dx;
		y += dy;
		return this;
	}

	public Vector2 subLocal(Vector2 dv) {
		x -= dv.x;
		y -= dv.y;
		return this;
	}

	public Vector2 set(Vector2i vi) {
		x = vi.x;
		y = vi.y;
		return this;
	}

	public Vector2 sub(Vector2i v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

}
