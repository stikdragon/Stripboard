package uk.co.stikman.strip.client;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Style.Cursor;

import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.math.Vector3;
import uk.co.stikman.strip.client.model.Component;
import uk.co.stikman.strip.client.model.ComponentInstance;

public class PlaceComponentCursor extends CursorTool {
	private final Component		comp;
	private ComponentInstance	inst;
	private int					currentHoleX;
	private int					currentHoleY;
	private Vector3				downAt;
	private String				hilightColour;

	public PlaceComponentCursor(Component comp) {
		super();
		this.comp = comp;
		reset();
	}

	private void reset() {
		inst = new ComponentInstance(comp);		
	}

	@Override
	public void mouseDown(Vector3 pos, int button) {
		downAt = new Vector3(pos);
		inst.getPin(0).setPosition(new Vector2i((int) pos.x, (int) pos.y));

		//
		// update pin positions
		//
		inst.updatePinPositions();
	}

	@Override
	public void mouseMove(Vector3 pos) {
		currentHoleX = (int) pos.x;
		currentHoleY = (int) pos.y;
		if (inst.getComponent().isStretchy()) 
			inst.getPin(1).getPosition().set(currentHoleX, currentHoleY);
	}

	@Override
	public void mouseUp(Vector3 pos, int button) {
		if (downAt == null)
			return;
		int x1 = (int) pos.x;
		int y1 = (int) pos.y;
		int x0 = (int) downAt.x;
		int y0 = (int) downAt.y;
		downAt = null;
		
		//
		// add component to the board
		//
		getApp().getBoard().placeComponent(inst);
		reset();
	}

	@Override
	public void render() {
		RenderIntf ctx = getApp().getRenderer();

		if (downAt != null) {
			int x0 = (int) downAt.x;
			int y0 = (int) downAt.y;

			ctx.setFillStyle(CssColor.make("rgba(255, 255, 255, 0.4)"));
			ComponentRenderer.render(getApp(), inst, x0, y0);

		} else {
			ctx.setFillStyle(hilightColour);
			ctx.fillRect(currentHoleX, currentHoleY, 1, 1);
		}
	}

	@Override
	public void start() {
		getApp().setCursor(Cursor.CROSSHAIR);
	}

	@Override
	public void end() {
		getApp().setCursor(Cursor.DEFAULT);
	}

	@Override
	public void setApp(Stripboard app) {
		super.setApp(app);
		hilightColour = app.getTheme().getHighlightColour().css();
	}

}
