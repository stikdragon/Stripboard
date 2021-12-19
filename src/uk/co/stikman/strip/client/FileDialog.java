package uk.co.stikman.strip.client;

import java.util.List;
import java.util.function.Consumer;

import org.apache.james.mime4j.message.TextBody;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class FileDialog extends StripDialog {
	public enum Mode {
		SAVE,
		LOAD
	}

	private Consumer<String>	onOk;
	private Mode				mode;
	private TextBox				txtName;
	private ListBox				lstNames;

	public FileDialog(Stripboard app, Mode mode, String caption, String filename) {
		super(app, caption, 400, 440);
		this.mode = mode;
		if (mode == Mode.LOAD) {
			addButton("load", "Load", this::btnLoad);
		} else {
			addButton("save", "Save", this::btnSave);
		}
		addButton("cancel", "Cancel", this::btnCancel);

		LayoutPanel pnl = getLayoutPanel();

		lstNames = new ListBox();
		pnl.add(lstNames);
		pnl.setWidgetLeftRight(lstNames, 0, Unit.PX, 0, Unit.PX);
		if (mode == Mode.LOAD) {
			pnl.setWidgetTopBottom(lstNames, 0, Unit.PX, 0, Unit.PX);
		} else {
			pnl.setWidgetTopBottom(lstNames, 0, Unit.PX, 30, Unit.PX);
			txtName = new TextBox();
			pnl.add(txtName);
			pnl.setWidgetLeftRight(txtName, 0, Unit.PX, 0, Unit.PX);
			pnl.setWidgetBottomHeight(txtName, 0, Unit.PX, 30, Unit.PX);
		}
		lstNames.addChangeHandler(this::lstNamesChanged);
		lstNames.setVisibleItemCount(15);

		populateList(lstNames);
	}

	private void lstNamesChanged(ChangeEvent event) {
		if (txtName != null)
			txtName.setText(lstNames.getSelectedValue());
	}

	private void populateList(ListBox lst) {
		AppFileSystem fs = getApp().getFileSystem();
		List<String> names = fs.fetchNames();
		names.forEach(lst::addItem);
	}

	private void btnSave(String id) {
		onOk.accept(txtName.getText());
		hide();
	}

	private void btnLoad(String id) {
		onOk.accept(lstNames.getSelectedValue());
		hide();
	}

	private void btnCancel(String id) {
		hide();
	}

	public void setOnOK(Consumer<String> handler) {
		this.onOk = handler;
	}

	public final Mode getMode() {
		return mode;
	}
}
