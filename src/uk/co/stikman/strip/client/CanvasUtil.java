package uk.co.stikman.strip.client;

import com.google.gwt.canvas.dom.client.Context2d;

import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector3;

public class CanvasUtil {
	private static final Vector3	tmpv	= new Vector3();
	private static final Vector3	tmpv2	= new Vector3();

	public static final float		PI		= 3.14159f;
	public static final float		PI2		= PI * 2;

	public static void fillRect(Context2d ctx, Matrix3 view, float x0, float y0, float w, float h) {
		float x0_;
		float y0_;
		float x1_;
		float y1_;

		tmpv.set(x0, y0, 1);
		view.multiply(tmpv, tmpv2);
		x0_ = tmpv2.x;
		y0_ = tmpv2.y;

		tmpv.set(w, h, 0);
		view.multiply(tmpv, tmpv2);
		x1_ = tmpv2.x;
		y1_ = tmpv2.y;

		ctx.fillRect(x0_, y0_, x1_, y1_);
	}

	public static void circle(Context2d ctx, Matrix3 view, float x, float y, float radius) {
		tmpv.set(radius, 0, 0);
		float r = view.multiply(tmpv, tmpv2).x;
		tmpv.set(x, y, 1);
		Vector3 v = view.multiply(tmpv, tmpv2);

		ctx.arc(v.x, v.y, r, 0, PI2);
	}

}
