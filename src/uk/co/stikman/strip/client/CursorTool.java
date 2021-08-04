package uk.co.stikman.strip.client;

import java.util.ArrayList;
import java.util.List;

import uk.co.stikman.strip.client.math.Vector3;

public abstract class CursorTool {

	private Stripboard app;

	public void mouseDown(Vector3 pos, int button) {

	}

	public void mouseMove(Vector3 pos) {

	}

	public void mouseUp(Vector3 pos, int button) {

	}

	public void keyPress(char ch) {
	}

	public void setApp(Stripboard app) {
		this.app = app;
	}

	public final Stripboard getApp() {
		return app;
	}

	public void render() {

	}

	public void end() {
	}

	public void start() {
	}

	public final List<ToolUIHint> getActions() {
		List<ToolUIHint> lst = new ArrayList<>();
		fillActionList(lst);
		return lst;
	}

	protected void fillActionList(List<ToolUIHint> lst) {
	}

}
