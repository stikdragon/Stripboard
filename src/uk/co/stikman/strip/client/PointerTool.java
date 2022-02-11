package uk.co.stikman.strip.client;

import java.util.ArrayList;
import java.util.List;

import uk.co.stikman.strip.client.math.Vector2;
import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.model.Board;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.HitResult;
import uk.co.stikman.strip.client.model.HitResultType;
import uk.co.stikman.strip.client.model.Hole;
import uk.co.stikman.strip.client.model.PinInstance;

public class PointerTool extends AbstractTool {

	private int			currentHoleX;
	private int			currentHoleY;
	private String		hilightColour;
	private Object		hover					= null;
	private Object		mouseDownItem;
	private DragGhost	ghost					= null;
	private Vector2		mouseDownCursorPosition	= new Vector2();
	private Vector2		mouseDownObjectPosition	= new Vector2();
	private Vector2		tv						= new Vector2();

	public PointerTool(Stripboard app) {
		super(app);
		hilightColour = app.getTheme().getHighlightColour().css();
	}

	@Override
	public void mouseDown(Vector2 pos, int button) {

		ghost = null;
		List<HitResult> lst = new ArrayList<>();
		getApp().getBoard().findThingsUnder(pos, lst, 0.1f);
		Object o = null;
		if (!lst.isEmpty()) {
			//
			// work out what we want to drag. if there's a pin here that
			// belongs to a stretchy component, then we pick that, otherwise
			// we're dragging the entire component
			//
			for (HitResult hr : lst) {
				if (hr.getType() == HitResultType.PIN_INSTANCE) {
					PinInstance pi = (PinInstance) hr.getObject();
					if (pi.getComponentInstance().getComponent().isStretchy()) {
						o = pi;
						break;
					}
				}
			}

			if (o == null) { // no stretchy pin, drag comp
				for (HitResult hr : lst) {
					if (hr.getType() == HitResultType.PIN_INSTANCE) {
						PinInstance pi = (PinInstance) hr.getObject();
						o = pi.getComponentInstance();
					}
				}
			}

			if (o == null) { // no pin at all
				for (HitResult hr : lst)
					if (hr.getType() == HitResultType.COMPONENT_INSTANCE)
						o = hr.getObject();
			}
		}

		if (o != null) {
			mouseDownItem = o;
			mouseDownCursorPosition.set(pos);

			if (o instanceof PinInstance) {
				ghost = new DragGhost(this, o, GhostType.PIN, pos, Vector2.ZERO);
				mouseDownObjectPosition.set(((PinInstance) o).getPosition());
			} else if (o instanceof ComponentInstance) {
				Vector2 dv = new Vector2(((ComponentInstance) o).getPin(0).getPosition()).sub(pos);
				ghost = new DragGhost(this, o, GhostType.COMPONENT, pos, dv);
				mouseDownObjectPosition.set(((ComponentInstance) o).getPin(0).getPosition());
			}
		}
	}

	@Override
	public void mouseMove(Vector2 pos) {
		currentHoleX = (int) pos.x;
		currentHoleY = (int) pos.y;

		if (mouseDownItem != null) {
			//
			// drag operation taking place
			//
			ghost.setCurrentPosition(pos);
		} else {
			//
			// see what's near this
			//
			List<HitResult> lst = new ArrayList<>();
			getApp().getBoard().findThingsUnder(pos, lst, 0.1f);
			if (!lst.isEmpty()) {
				hover = lst.get(0).getObject();
			} else {
				hover = null;
			}
		}
	}

	@Override
	public void mouseUp(Vector2 pos, int button) {
		//
		// if there was a drag op happening then apply the changes to the component
		//
		if (ghost != null) {
			Vector2i finalpos = new Vector2i(ghost.getCurrentPosition().add(ghost.getOffset(), new Vector2()));
			switch (ghost.getType()) {
			case COMPONENT:
				ComponentInstance ci = (ComponentInstance) ghost.getObject();
				if (ci.getComponent().isStretchy()) {
					//
					// need to move all pins 
					//
					Vector2i delta = new Vector2i(finalpos).sub(ci.getPin(0).getPosition());
					for (PinInstance p : ci.getPins()) {
						p.setPosition(p.getPosition().add(delta));
					}

				} else {
					//
					// just update the anchor pin
					//
					ci.getPin(0).setPosition(finalpos);
					ci.updatePinPositions();
				}
				break;
			case PIN:
				//
				// must be in a stretchy mode
				//
				PinInstance pi = (PinInstance) ghost.getObject();
				pi.setPosition(finalpos);
				break;
			}
			getApp().getBoard().updatePinPositions();
			getApp().invalidate();
		}
		mouseDownItem = null;
		ghost = null;

	}

	@Override
	public void render() {
		RenderIntf ctx = getApp().getRenderer();
		if (getApp().getBoard().isValidCoord(currentHoleX, currentHoleY)) {
			ctx.setFillStyle(hilightColour);
			ctx.fillRect(currentHoleX, currentHoleY, 1, 1);
		}

		if (hover != null) {

			//
			// highlight the component with a border
			//
			ComponentInstance ci = null;
			if (hover instanceof PinInstance)
				ci = ((PinInstance) hover).getComponentInstance();
			else if (hover instanceof ComponentInstance)
				ci = (ComponentInstance) hover;

			//
			// draw a border
			//
			if (ci != null) {
				Vector2i v = ci.getPin(0).getPosition();
				getRenderer().render(getApp(), ci, v.x, v.y, RenderState.OUTLINE);
			}
		}

		if (ghost != null) {
			ghost.render(getApp().getRenderer());
		}
	}

	@Override
	public void keyPress(char ch) {
		if (ch == 'x') {
			Board brd = getApp().getBoard();
			if (brd.isValidCoord(currentHoleX, currentHoleY)) {
				Hole hole = getApp().getBoard().getHole(currentHoleX, currentHoleY);
				hole.setBroken(!hole.isBroken());
				getApp().invalidate();
			}
		} else if (ch == 'z') {
			renameHover();
		} else if (ch == 'v') {
			toggleViews();
		} else if (ch == 'r') {
			rotate();
		}
	}

	private void rotate() {
		if (hover == null)
			return;
		ComponentInstance ci = getComponent();
		if (ci.getComponent().isStretchy() && ci.getComponent().getPins().size() == 2) {
			ci.setRotation(ci.getRotation() + 2);
		} else if (!ci.getComponent().isStretchy()) {
			ci.setRotation(ci.getRotation() + 1);
		}
		getApp().invalidate();
	}

	private ComponentInstance getComponent() {
		ComponentInstance ci = null;
		if (hover instanceof PinInstance)
			ci = ((PinInstance) hover).getComponentInstance();
		else if (hover instanceof ComponentInstance)
			ci = (ComponentInstance) hover;
		return ci;
	}

	private void toggleViews() {
		ToggleViewDlg dlg = new ToggleViewDlg(getApp());
	}

	private void renameHover() {
		if (hover == null)
			return;
		ComponentInstance ci = null;
		if (hover instanceof PinInstance) {
			ci = ((PinInstance) hover).getComponentInstance();
		} else if (hover instanceof ComponentInstance) {
			ci = (ComponentInstance) hover;
		}
		
		RenameComponentDlg dlg = new RenameComponentDlg(getApp(), ci);

	}

	@Override
	protected void fillActionList(List<ToolUIHint> lst) {
		super.fillActionList(lst);
		lst.add(new ToolUIHint("E", "Add component"));
		lst.add(new ToolUIHint("DEL", "Delete"));
		lst.add(new ToolUIHint("X", "Break/Unbreak"));
		lst.add(new ToolUIHint("R", "Rotate"));
		lst.add(new ToolUIHint("W", "Add wire"));
		lst.add(new ToolUIHint("Z", "Edit name"));
		lst.add(new ToolUIHint("V", "Toggle views"));
	}

}
