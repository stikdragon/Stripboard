package uk.co.stikman.strip.client;

import java.util.ArrayList;
import java.util.List;

import uk.co.stikman.strip.client.math.Vector2;
import uk.co.stikman.strip.client.math.Vector3;

public abstract class AbstractTool {

	private final Stripboard app;

	public AbstractTool(Stripboard app) {
		this.app = app;
	}

	public void mouseDown(Vector2 pos, int button) {

	}

	public void mouseMove(Vector2 pos) {

	}

	public void mouseUp(Vector2 pos, int button) {

	}

	public void keyPress(char ch) {
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
	
	protected ComponentRenderer getRenderer() {
		return app.getComponentRenderer();
	}


}
