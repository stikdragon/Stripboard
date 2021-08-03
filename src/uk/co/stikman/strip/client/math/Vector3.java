package uk.co.stikman.strip.client.math;

public class Vector3 {
	public static final Vector3	ZERO	= new Vector3(0, 0, 0);
	public float				x;
	public float				y;
	public float				z;

	public Vector3() {
	}

	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3(Vector3 copy) {
		x = copy.x;
		y = copy.y;
		z = copy.z;
	}

	public final float length() {
		return (float) Math.sqrt((x * x) + (y * y) + (z * z));
	}

	public final float lengthSquared() {
		return (x * x) + (y * y) + (z * z);
	}

	public final Vector3 add(Vector3 v2, Vector3 dest) {
		dest.x = x + v2.x;
		dest.y = y + v2.y;
		dest.z = z + v2.z;
		return dest;
	}

	public final Vector3 add(Vector3 v2) {
		x += v2.x;
		y += v2.y;
		z += v2.z;
		return this;
	}

	public final Vector3 sub(Vector3 v2, Vector3 dest) {
		dest.x = x - v2.x;
		dest.y = y - v2.y;
		dest.z = z - v2.z;
		return dest;
	}

	public final Vector3 sub(Vector3 v2) {
		x -= v2.x;
		y -= v2.y;
		z -= v2.z;
		return this;
	}

	public final Vector3 multiply(float s) {
		x *= s;
		y *= s;
		z *= s;
		return this;
	}

	public final Vector3 multiply(float s, Vector3 dest) {
		dest.x = x * s;
		dest.y = y * s;
		dest.z = z * s;
		return dest;
	}

	public final Vector3 divide(float d) {
		x /= d;
		y /= d;
		z /= d;
		return this;
	}

	public final Vector3 divide(float d, Vector3 dest) {
		dest.x = x / d;
		dest.y = y / d;
		dest.z = z / d;
		return dest;
	}

	public final Vector3 normalize() {
		float f = (float) Math.sqrt((x * x) + (y * y) + (z * z));
		if (f == 0.0f) {
			x = 0;
			y = 0;
			z = 0;
			return this;
		}

		float in = 1.0f / f;
		x *= in;
		y *= in;
		z *= in;
		return this;
	}

	public final float dot(float vx, float vy, float vz) {
		return vx * x + vy * y + vz * z;
	}

	public float dot(Vector3 t) {
		return t.x * x + t.y * y + t.z * z;
	}

	public static final Vector3 cross(Vector3 v1, Vector3 v2, Vector3 dest) {
		dest.x = (v1.y * v2.z) - (v1.z * v2.y);
		dest.y = (v1.z * v2.x) - (v1.x * v2.z);
		dest.z = (v1.x * v2.y) - (v1.y * v2.x);
		return dest;
	}

	public final Vector3 set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3 copy(Vector3 v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		return this;
	}

	@Override
	public String toString() {
		return x + "," + y + "," + z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
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
		Vector3 other = (Vector3) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

	public float distanceSq(Vector3 v) {
		float dx = v.x - x;
		float dy = v.y - y;
		float dz = v.z - z;
		return dx * dx + dy * dy + dz * dz;
	}

	public Vector3 addMult(Vector3 n, float f) {
		this.x += n.x * f;
		this.y += n.y * f;
		this.z += n.z * f;
		return this;
	}

	public Vector3 add(float dx, float dy, float dz) {
		x += dx;
		y += dy;
		z += dz;
		return this;
	}

	public static Vector3 sub(Vector3 a, Vector3 b, Vector3 out) {
		return a.sub(b, out);
	}

}
