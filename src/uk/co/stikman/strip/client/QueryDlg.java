package uk.co.stikman.strip.client;

import java.util.function.Consumer;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;

public class QueryDlg extends StripDialog {

	private Label lbl;

	public QueryDlg(Stripboard app, String caption, String msg) {
		super(app, caption, 500, 300);
		lbl = new Label(msg);
		getLayoutPanel().add(lbl);
		getLayoutPanel().setWidgetLeftRight(lbl, 0, Unit.PX, 0, Unit.PX);
		getLayoutPanel().setWidgetTopBottom(lbl, 0, Unit.PX, 0, Unit.PX);
	}

	public void addButton(final String id, String caption, Consumer<String> onClick) {
		super.addButton(id, caption, x -> {
			hide();
			onClick.accept(x);
		});
	}

}
