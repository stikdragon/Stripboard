package uk.co.stikman.strip.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import uk.co.stikman.strip.client.model.ComponentInstance;

public class RenameComponentDlg extends StripDialog {

	private ComponentInstance instance;

	public RenameComponentDlg(Stripboard app, ComponentInstance ci) {
		super(app, "Rename component", 400, 200);
		this.instance = ci;

		VerticalPanel vp = new VerticalPanel();
		Label lbl = new Label("Rename component [" + ci.getName() + "]:");
		TextBox txt = new TextBox();
		txt.setText(ci.getName());
	}

}
