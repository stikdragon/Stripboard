package uk.co.stikman.strip.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;

public class ToolPanel extends LayoutPanel {

	private FlowPanel actions;

	public ToolPanel() {
		super();
		FlowPanel fp = new FlowPanel();
		add(fp);

		fp.add(new Label("Keyboard Shortcuts"));

		actions = new FlowPanel();
		fp.add(actions);

	}

	public void updateFrom(CursorTool tool) {
		actions.clear();
		for (ToolUIHint action : tool.getActions())
			actions.add(new HTML("<div class=\"key\"><span>" + action.getKey() + "</span> " + action.getDesc() + "</div>"));
	}

}
