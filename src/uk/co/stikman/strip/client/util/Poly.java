package uk.co.stikman.strip.client.util;

import uk.co.stikman.strip.client.math.Vector2;

public class Poly {

	private float[]					verts;
	private Vector2					bbA			= new Vector2();
	private Vector2					bbB			= new Vector2();

	private static final Vector2	tmpv		= new Vector2();
	private static final Vector2	tmpv2		= new Vector2();
	private static final Vector2	tmpv3		= new Vector2();
	private static final Vector2	xsect_v1	= new Vector2();
	private static final Vector2	xsect_v2	= new Vector2();

	public Poly(float[] verts) {
		this.verts = verts;
		calcBB();
	}

	/**
	 * calculate extents
	 */
	private void calcBB() {
		if (verts == null)
			return;
		bbA.set(verts[0], verts[1]);
		bbB.set(verts[0], verts[1]);
		for (int i = 0; i < verts.length; ++i) {
			bbA.x = verts[i] < bbA.x ? verts[i] : bbA.x;
			bbB.x = verts[i] > bbB.x ? verts[i] : bbB.x;
			++i;
			bbA.y = verts[i] < bbA.y ? verts[i] : bbA.y;
			bbB.y = verts[i] > bbB.y ? verts[i] : bbB.y;
		}
	}

	public float[] getVerts() {
		return verts;
	}

	public void setVerts(float[] verts) {
		this.verts = verts;
		calcBB();
	}

	public int size() {
		return verts.length / 2;
	}

	public boolean contains(Vector2 p) {
		if (p.x < bbA.x || p.y < bbA.y || p.x > bbB.x || p.y > bbB.y)
			return false;
		// 
		// pick point outside bounding box, see how many edges it touches 
		// on the way to the given point
		//
		Vector2 q = tmpv.set(bbA).add(-10, -10); // outside the min bounding box
		int sz = size();
		int cnt = 0;
		for (int i = 0; i < sz; ++i) {
			int j = (i + 1) % sz;
			tmpv2.set(verts[i * 2], verts[i * 2 + 1]);
			tmpv3.set(verts[j * 2], verts[j * 2 + 1]);
			if (xsect2(tmpv2, tmpv3, p, q, null))
				++cnt;
		}

		return cnt % 2 == 1;
	}

	/**
	 * line segments a0-a1, b0-b1, returns <code>true</code> if they intersect
	 * 
	 * @param a0
	 * @param a1
	 * @param b0
	 * @param b1
	 * @param res
	 * @return
	 */
	private boolean xsect2(Vector2 a0, Vector2 a1, Vector2 b0, Vector2 b1, Vector2 res) {
		Vector2 adir = a1.sub(a0, xsect_v1);
		Vector2 bdir = b1.sub(b0, xsect_v2);
		float d = adir.cross(bdir);
		if (d == 0.0f)
			return false; // parallel (never meet)
		float a = (adir.x * (a0.y - b0.y) - adir.y * (a0.x - b0.x)) / d;
		float b = (bdir.x * (a0.y - b0.y) - bdir.y * (a0.x - b0.x)) / d;
		if (b < 0 || b > 1 || a < 0 || a > 1)
			return false;
		if (res != null)
			res.set(a0).addMultiply(adir, b);
		return true;
	}

	public void getBoundingBox(Vector2 outMin, Vector2 outMax) {
		outMin.set(bbA);
		outMax.set(bbB);
	}
}
