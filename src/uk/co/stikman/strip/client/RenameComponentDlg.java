package uk.co.stikman.strip.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import uk.co.stikman.strip.client.model.ComponentInstance;

public class RenameComponentDlg extends StripDialog {

	private ComponentInstance instance;
	private TextBox txt;

	public RenameComponentDlg(Stripboard app, ComponentInstance ci) {
		super(app, "Rename component", 400, 200);
		this.instance = ci;

		VerticalPanel vp = new VerticalPanel();
		Label lbl = new Label("Rename component [" + ci.getName() + "]:");
		txt = new TextBox();
		txt.setText(ci.getName());
		vp.add(lbl);
		vp.add(txt);
		getLayoutPanel().add(vp);
		getLayoutPanel().setWidgetLeftRight(vp, 0, Unit.PX, 0, Unit.PX);
		getLayoutPanel().setWidgetTopBottom(vp, 0, Unit.PX, 0, Unit.PX);
		
		addButton("ok", "OK", this::btnOK);
		addButton("cancel", "Cancel", this::btnCancel);
	}
	
	private void btnOK(String id) {
		instance.setName(txt.getText());
		hide();
	}
	private void btnCancel(String id) {
		hide();
	}

}
