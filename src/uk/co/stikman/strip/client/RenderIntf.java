package uk.co.stikman.strip.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector2;
import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.math.Vector3;
import uk.co.stikman.strip.client.util.Colour;
import uk.co.stikman.strip.client.util.Poly;

public class RenderIntf {
	private static final float	PI2		= 2.0f * 3.14159f;
	private Matrix3				view;
	private Stripboard			app;
	private Context2d			context;
	private String				pinColour;
	private String				leadColour;
	private String				wireColour;
	private String				holecolour;
	private String				breakcolour;
	private String				ghostColour;
	private String				errorColour;
	private Vector3				tmpv	= new Vector3();
	private Vector3				tmpv2	= new Vector3();
	private Vector3				tmpv3	= new Vector3();
	private Vector3				tmpv4	= new Vector3();
	private Vector3				tmpv5	= new Vector3();
	private Matrix3				tmpm	= new Matrix3();

	public RenderIntf(Matrix3 view, Stripboard app, Context2d context) {
		super();
		this.view = view;
		this.app = app;
		this.context = context;
		pinColour = app.getTheme().getPinColour().css();
		leadColour = app.getTheme().getLeadColour().css();
		breakcolour = app.getTheme().getBrokenHoleColour().css();
		holecolour = app.getTheme().getHoleColour().css();
		wireColour = app.getTheme().getWireColour().css();
		ghostColour = app.getTheme().getGhostColour().css();
		errorColour = app.getTheme().getErrorColour().css();
	}

	public final Matrix3 getView() {
		return view;
	}

	public final Stripboard getApp() {
		return app;
	}

	public final Context2d getContext() {
		return context;
	}

	public void setFillStyle(CssColor c) {
		context.setFillStyle(c);
	}

	public void setFillStyle(String c) {
		context.setFillStyle(c);
	}

	public void fillRect(float x0, float y0, float x1, float y1) {
		CanvasUtil.fillRect(context, view, x0, y0, x1, y1);
	}

	public void drawPin(int x, int y, RenderState state) {
		context.beginPath();
		switch (state) {
			case ERROR:
				context.setFillStyle(errorColour);
				break;
			case GHOST:
				context.setFillStyle(ghostColour);
				break;
			case NORMAL:
				context.setFillStyle(pinColour);
				break;

		}
		CanvasUtil.circle(context, view, x + 0.5f, y + 0.5f, 0.4f);
		context.fill();
	}

	public void drawLead(float x0, float y0, float x1, float y1, Matrix3 xfm) {
		Matrix3 m = getTransformView(xfm);
		m.multiply(tmpv.set(x0, y0, 1.0f), tmpv2);
		float fx0 = tmpv2.x;
		float fy0 = tmpv2.y;
		m.multiply(tmpv.set(x1, y1, 1.0f), tmpv2);
		float fx1 = tmpv2.x;
		float fy1 = tmpv2.y;

		context.setStrokeStyle(leadColour);
		context.setLineWidth(3.0f);
		context.beginPath();
		context.moveTo(fx0, fy0);
		context.lineTo(fx1, fy1);
		context.stroke();
	}

	public void drawPoly(String fill, String stroke, Matrix3 xfm, Poly poly) {
		Matrix3 m = getTransformView(xfm);
		int n = poly.size();
		float[] verts = poly.getVerts();
		context.beginPath();
		for (int i = 0; i < n; ++i) {
			tmpv.set(verts[i * 2], verts[i * 2 + 1], 1.0f);
			m.multiply(tmpv, tmpv2);
			if (i == 0)
				context.moveTo(tmpv2.x, tmpv2.y);
			else
				context.lineTo(tmpv2.x, tmpv2.y);
		}
		context.closePath();

		if (fill != null) {
			context.setFillStyle(fill);
			context.fill();
		}
		if (stroke != null) {
			context.setStrokeStyle(stroke);
			context.setLineWidth(2.0f);
			context.stroke();
		}
	}

	/**
	 * uses <code>tmpm</code>
	 * 
	 * @param xfm
	 * @return
	 */
	private Matrix3 getTransformView(Matrix3 xfm) {
		if (xfm == null) {
			return view;
		} else {
			tmpm.copy(view);
			tmpm.multiply(xfm);
			return tmpm;
		}
	}

	public void drawBreak(int x, int y) {
		tmpv.set(0.5f, 0.32f, 0); // radius, and unit size for cross
		Vector3 sizes = view.multiply(tmpv, tmpv3);
		tmpv.set(x + 0.5f, y + 0.5f, 1);
		Vector3 v = view.multiply(tmpv, tmpv2);
		context.beginPath();
		context.setFillStyle(breakcolour);
		context.arc(v.x, v.y, sizes.x, 0, PI2);
		context.fill();

		context.beginPath();
		context.setStrokeStyle("red");
		context.setLineWidth(4.0f);
		float f = sizes.y;
		context.moveTo(v.x - f, v.y - f);
		context.lineTo(v.x + f, v.y + f);
		context.moveTo(v.x - f, v.y + f);
		context.lineTo(v.x + f, v.y - f);
		context.stroke();

		context.setFillStyle(holecolour);

	}

	public void drawWire(Vector2i a, Vector2i b) {
		//
		// draw an inner wire in lead colour, then an outer one that's shorter and fatter
		//
		tmpv.set(a.x + 0.5f, a.y + 0.5f, 0);
		Vector3 va = view.multiply(tmpv, tmpv2);
		tmpv.set(b.x + 0.5f, b.y + 0.5f, 0);
		Vector3 vb = view.multiply(tmpv, tmpv3);

		context.setLineWidth(3.0f);
		context.setStrokeStyle(leadColour);
		context.beginPath();
		context.moveTo(va.x, va.y);
		context.lineTo(vb.x, vb.y);
		context.stroke();

		//
		// fat bit
		//
		tmpv.set(a.x + 0.5f, a.y + 0.5f, 0);
		tmpv2.set(b.x + 0.5f, b.y + 0.5f, 0);
		Vector3 gap = tmpv2.sub(tmpv, tmpv3).normalize().multiply(0.38f); // 0.38 of a hole shorter

		tmpv.set(a.x + 0.5f, a.y + 0.5f, 0).add(gap);
		va = view.multiply(tmpv, tmpv2);
		tmpv.set(b.x + 0.5f, b.y + 0.5f, 0).sub(gap);
		vb = view.multiply(tmpv, tmpv3);

		context.setLineWidth(6.0f);
		context.setStrokeStyle(wireColour);
		context.beginPath();
		context.moveTo(va.x, va.y);
		context.lineTo(vb.x, vb.y);
		context.stroke();
	}

	public void drawCircle(float x, float y, float radius, String fill, String stroke) {
		tmpv.set(radius, 0, 0);
		float r = view.multiply(tmpv, tmpv2).x;
		tmpv.set(x, y, 1);
		Vector3 v = view.multiply(tmpv, tmpv2);

		context.beginPath();
		context.arc(v.x, v.y, r, 0, PI2);

		if (fill != null) {
			context.setFillStyle(fill);
			context.fill();
		}
		if (stroke != null) {
			context.setStrokeStyle(stroke);
			context.setLineWidth(3.0f);
			context.stroke();
		}

	}

	private static native void setLineDash(Context2d ctx, int[] pattern) /*-{
		ctx.setLineDash(pattern);
	}-*/;

	public void drawLine(Colour colour, Vector2 start, Vector2 end, Matrix3 xfm, boolean dotted) {
		view.multiply(tmpv.set(start.x, start.y, 1.0f), tmpv2);
		float fx0 = tmpv2.x;
		float fy0 = tmpv2.y;
		view.multiply(tmpv.set(end.x, end.y, 1.0f), tmpv2);
		float fx1 = tmpv2.x;
		float fy1 = tmpv2.y;

		context.setStrokeStyle(colour.css());
		context.setLineWidth(3.0f);
		context.beginPath();
		if (dotted)
			setLineDash(context, new int[] { 10, 10 });
		context.moveTo(fx0, fy0);
		context.lineTo(fx1, fy1);
		context.stroke();
		if (dotted)
			setLineDash(context, new int[] {});
	}

}
