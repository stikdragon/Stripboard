package uk.co.stikman.strip.client;

import uk.co.stikman.strip.client.math.Vector3;
import uk.co.stikman.strip.client.model.Board;
import uk.co.stikman.strip.client.model.Hole;

public class PointerTool extends CursorTool {

	private int		currentHoleX;
	private int		currentHoleY;
	private String	hilightColour;

	@Override
	public void mouseDown(Vector3 pos, int button) {

	}

	@Override
	public void mouseMove(Vector3 pos) {
		currentHoleX = (int) pos.x;
		currentHoleY = (int) pos.y;
	}

	@Override
	public void mouseUp(Vector3 pos, int button) {
	}

	@Override
	public void render() {
		RenderIntf ctx = getApp().getRenderer();
		ctx.setFillStyle(hilightColour);
		ctx.fillRect(currentHoleX, currentHoleY, 1, 1);
	}

	@Override
	public void setApp(Stripboard app) {
		super.setApp(app);
		hilightColour = app.getTheme().getHighlightColour().css();
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

}
