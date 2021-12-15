package uk.co.stikman.strip.client;

import java.util.ArrayList;
import java.util.List;

import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.math.Vector3;
import uk.co.stikman.strip.client.model.Board;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.HitResult;
import uk.co.stikman.strip.client.model.Hole;
import uk.co.stikman.strip.client.model.PinInstance;
import uk.co.stikman.strip.client.util.Util;

public class PointerTool extends AbstractTool {

	private int		currentHoleX;
	private int		currentHoleY;
	private String	hilightColour;
	private Object	selected	= null;

	public PointerTool(Stripboard app) {
		super(app);
		hilightColour = app.getTheme().getHighlightColour().css();
	}

	@Override
	public void mouseDown(Vector3 pos, int button) {

	}

	@Override
	public void mouseMove(Vector3 pos) {
		currentHoleX = (int) pos.x;
		currentHoleY = (int) pos.y;

		//
		// see what's near this
		//
		List<HitResult> lst = new ArrayList<>();
		getApp().getBoard().findThingsUnder(pos, lst, 0.1f);
		if (!lst.isEmpty())
			selected = lst.get(0).getObject();
		else
			selected = null;
	}

	@Override
	public void mouseUp(Vector3 pos, int button) {
	}

	@Override
	public void render() {
		RenderIntf ctx = getApp().getRenderer();
		if (getApp().getBoard().isValidCoord(currentHoleX, currentHoleY)) {
			ctx.setFillStyle(hilightColour);
			ctx.fillRect(currentHoleX, currentHoleY, 1, 1);
		}

		if (selected != null) {

			//
			// highlight the component with a border
			//
			ComponentInstance ci = null;
			if (selected instanceof PinInstance)
				ci = ((PinInstance) selected).getComponentInstance();
			else if (selected instanceof ComponentInstance)
				ci = (ComponentInstance) selected;

			//
			// draw a border
			//
			if (ci != null) {
				Vector2i v = ci.getPin(0).getPosition();
				getRenderer().render(getApp(), ci, v.x, v.y, RenderState.OUTLINE);
			}
		}

		//		Clipper clipper = new DefaultClipper();
		//		if (selected != null) {
		//			Path p = new Path();
		//			p.add(new LongPoint(0, 0));
		//			p.add(new LongPoint(0, 0));
		//			p.add(new LongPoint(0, 0));
		//			p.add(new LongPoint(0, 0));
		//			
		//			clipper.addPath(p, PolyType.SUBJECT, true);
		//			Paths solution = new Paths();
		//			clipper.execute(ClipType.UNION, solution);
		//			
		//			
		//		}
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
		}
	}

	@Override
	protected void fillActionList(List<ToolUIHint> lst) {
		super.fillActionList(lst);
		lst.add(new ToolUIHint("I/E", "Add component"));
		lst.add(new ToolUIHint("DEL", "Delete"));
		lst.add(new ToolUIHint("X", "Break/Unbreak"));
		lst.add(new ToolUIHint("R", "Rotate"));
		lst.add(new ToolUIHint("W", "Add wire"));
	}

}
