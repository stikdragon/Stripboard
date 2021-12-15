package uk.co.stikman.strip.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;

import uk.co.stikman.strip.client.math.Vector2;
import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.math.Vector3;
import uk.co.stikman.strip.client.model.Board;
import uk.co.stikman.strip.client.model.Component;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.Hole;
import uk.co.stikman.strip.client.model.PinInstance;
import uk.co.stikman.strip.client.util.Util;

public class PlaceComponentTool extends AbstractTool {
	private final Component		comp;
	private ComponentInstance	inst;
	private int					currentHoleX;
	private int					currentHoleY;
	private Vector3				downAt;
	private String				hilightColour;
	private boolean				placed	= false;
	private boolean				invalid	= false;

	public PlaceComponentTool(Stripboard app, Component comp) {
		super(app);
		this.hilightColour = app.getTheme().getHighlightColour().css();
		this.comp = comp;
		reset();
	}

	private void reset() {
		inst = new ComponentInstance(getApp().getBoard(), comp);
		placed = false;
		invalid = false;
	}

	@Override
	public void mouseDown(Vector3 pos, int button) {
		if (invalid)
			return;
		downAt = new Vector3(pos);
		inst.getPin(0).setPosition(new Vector2i((int) pos.x, (int) pos.y));
		placed = true;

		//
		// update pin positions
		//
		inst.updatePinPositions();
	}

	@Override
	public void mouseMove(Vector3 pos) {
		currentHoleX = (int) pos.x;
		currentHoleY = (int) pos.y;
		if (inst.getComponent().isStretchy()) {
			inst.getPin(1).getPosition().set(currentHoleX, currentHoleY);
		} else {
			inst.getPin(0).setPosition(new Vector2i((int) pos.x, (int) pos.y));
			inst.updatePinPositions();
		}

		invalid = !checkPinPositions();
	}

	/**
	 * check current pin locations against the board, update the error highlights.
	 * return <code>true</code> if they're all free, <code>false</code> otherwise
	 */
	private boolean checkPinPositions() {
		List<ErrorMarker> errors = new ArrayList<>();
		for (PinInstance p : inst.getPins()) {
			Hole h = getApp().getBoard().getHole(p.getPosition());
			if (h.getPin() != null) {
				ErrorMarker em = new ErrorMarker();
				em.setPosition(p.getPosition());
				errors.add(em);
			}
		}

		getApp().setErrorMarkers(errors);
		return errors.isEmpty();
	}

	@Override
	public void mouseUp(Vector3 pos, int button) {
		if (downAt == null)
			return;
		int x0 = (int) downAt.x;
		int y0 = (int) downAt.y;
		downAt = null;

		//
		// check it's not 0 size
		//
		if (comp.isStretchy()) {
			int x1 = inst.getPin(1).getPosition().x;
			int y1 = inst.getPin(1).getPosition().y;
			if (x1 == x0 && y1 == y0) {
				reset();
				return;
			}
		}

		//
		// add component to the board
		//
		getApp().getBoard().placeComponent(inst);
		reset();
	}

	@Override
	public void render() {
		if (downAt == null)
			return;
		RenderIntf ctx = getApp().getRenderer();

		int x0 = currentHoleX;
		int y0 = currentHoleY;

		if (downAt != null) {
			x0 = (int) downAt.x;
			y0 = (int) downAt.y;
		}

		ctx.setFillStyle(hilightColour);
		ctx.fillRect(currentHoleX, currentHoleY, 1, 1);

		RenderState state = placed ? RenderState.GHOST : RenderState.NORMAL;
		if (invalid)
			state = RenderState.ERROR;

		//
		// remove it from the poly cache so it's forced to regenerate each frame
		//
		getApp().getBoard().getPolyCache().remove(inst);

		Vector2i p = inst.getPin(0).getPosition();
		getRenderer().render(getApp(), inst, p.x, p.y, state); // placed determines what state to draw it in

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
	protected void fillActionList(List<ToolUIHint> lst) {
		super.fillActionList(lst);
		lst.add(new ToolUIHint("ESC/Q", "Cancel"));
		lst.add(new ToolUIHint("R", "Rotate"));
	}
}
