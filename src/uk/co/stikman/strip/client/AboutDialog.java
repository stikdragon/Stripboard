package uk.co.stikman.strip.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.HTML;

public class AboutDialog extends StripDialog {

	interface AboutBundle extends ClientBundle {

		@Source("about.txt")
		TextResource about();

	}

	private AboutBundle bundle = GWT.create(AboutBundle.class);

	public AboutDialog(Stripboard app) {
		super(app, "About", 400, 400);
		addButton("close", "close", this::close);
		getLayoutPanel().add(new HTML(bundle.about().getText()));
		setAutoHideEnabled(true);
	}

	private void close(String id) {
		hide();
	}
}
