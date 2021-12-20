package uk.co.stikman.strip.client;

import java.util.function.Consumer;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.LayoutPanel;

public abstract class StripDialog extends DialogBox {
	private Stripboard	app;
	private String		caption;
	private LayoutPanel	lp;
	private FlowPanel	btns;

	public StripDialog(Stripboard app, String caption, int width, int height) {
		this.app = app;
		this.caption = caption;
		setText(caption);
		setGlassEnabled(true);
		setModal(true);

		LayoutPanel outer = new LayoutPanel();
		lp = new LayoutPanel();
		outer.add(lp);
		outer.setWidgetLeftRight(lp, 0, Unit.PX, 0, Unit.PX);
		outer.setWidgetTopBottom(lp, 0, Unit.PX, 30, Unit.PX);

		btns = createButtonPanel();
		outer.add(btns);
		outer.setWidgetLeftRight(btns, 0, Unit.PX, 0, Unit.PX);
		outer.setWidgetBottomHeight(btns, 0, Unit.PX, 30, Unit.PX);
		outer.setWidgetVerticalPosition(btns, Alignment.STRETCH);

		outer.setPixelSize(width, height);

		setWidget(outer);
		center();
	}

	private FlowPanel createButtonPanel() {
		FlowPanel fp = new FlowPanel();
		return fp;
	}

	public final LayoutPanel getLayoutPanel() {
		return lp;
	}

	public void addButton(final String id, String caption, Consumer<String> onClick) {
		Button btn = new Button();
		btns.add(btn);
		btn.setText(caption);
		btn.addClickHandler(x -> onClick.accept(id));
	}

	public final Stripboard getApp() {
		return app;
	}
	
	@Override
	public void hide() {
		super.hide();
		getApp().afterDialog(this);
	}

}
