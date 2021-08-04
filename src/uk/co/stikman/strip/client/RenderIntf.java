package uk.co.stikman.strip.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector3;

public class RenderIntf {
	private static final float	PI2		= 2.0f * 3.14159f;
	private Matrix3				view;
	private Stripboard			app;
	private Context2d			context;
	private String				pinColour;
	private String				leadColour;
	private String				holecolour;
	private String				breakcolour;
	private Vector3				tmpv	= new Vector3();
	private Vector3				tmpv2	= new Vector3();
	private Vector3				tmpv3	= new Vector3();
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

	public void drawPin(int x, int y) {
		context.beginPath();
		context.setFillStyle(pinColour);
		CanvasUtil.circle(context, view, x + 0.5f, y + 0.5f, 0.4f);
		context.fill();
	}

	public void drawLead(int x0, int y0, int x1, int y1) {
		view.multiply(tmpv.set(x0 + 0.5f, y0 + 0.5f, 1.0f), tmpv2);
		float fx0 = tmpv2.x;
		float fy0 = tmpv2.y;
		view.multiply(tmpv.set(x1 + 0.5f, y1 + 0.5f, 1.0f), tmpv2);
		float fx1 = tmpv2.x;
		float fy1 = tmpv2.y;

		context.setFillStyle(leadColour);
		context.setLineWidth(3.0f);
		context.beginPath();
		context.moveTo(fx0, fy0);
		context.lineTo(fx1, fy1);
		context.stroke();
	}

	public void drawPoly(String fill, Matrix3 xfm, float[] verts) {
		Matrix3 m;
		if (xfm == null) {
			m = view;
		} else {
			tmpm.copy(view);
			tmpm.multiply(xfm);
			m = tmpm;
		}
		int n = verts.length / 2;
		context.setFillStyle(fill);
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
		context.fill();
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

}
