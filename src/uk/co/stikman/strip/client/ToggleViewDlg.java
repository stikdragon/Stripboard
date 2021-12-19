package uk.co.stikman.strip.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ToggleViewDlg extends StripDialog {

	public ToggleViewDlg(Stripboard app) {
		super(app, "Toggle Views", 140, 300);

		VerticalPanel vp = new VerticalPanel();

		getLayoutPanel().add(vp);
		getLayoutPanel().setWidgetLeftRight(vp, 0, Unit.PX, 0, Unit.PX);
		getLayoutPanel().setWidgetTopBottom(vp, 0, Unit.PX, 0, Unit.PX);

		addLine(vp, "Bodies", "1", ViewStates.BODY);
		addLine(vp, "Pins", "2", ViewStates.PIN);
		addLine(vp, "Component Names", "3", ViewStates.COMPONENT_NAME);
		addLine(vp, "Pin Names", "4", ViewStates.PIN_NAME);

		Button btnClose = new Button("Close");
		vp.add(btnClose);
		btnClose.addClickHandler(x -> hide());
	}

	private void addLine(VerticalPanel vp, String name, String key, String vsname) {
		boolean state = getApp().getViewStates().isOn(vsname);
		FlowPanel fp = new FlowPanel();
		fp.addStyleName("viewstaterow");
		CheckBox chk = new CheckBox(name);
		chk.setValue(state);
		fp.add(new Label(key));
		fp.add(chk);
		chk.addClickHandler(x -> {
			getApp().getViewStates().toggle(vsname);
			getApp().render();
		});
		vp.add(fp);
	}

}
