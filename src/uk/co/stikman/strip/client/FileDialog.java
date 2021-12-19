package uk.co.stikman.strip.client;

import java.util.function.Consumer;

import uk.co.stikman.strip.client.FileDialog.Mode;

public class FileDialog extends StripDialog {
	public enum Mode {
		SAVE,
		LOAD
	}

	private Consumer<String>	onOk;
	private Mode				mode;

	public FileDialog(Stripboard app, Mode mode, String caption, String filename) {
		super(app, caption, 400, 440);
		this.mode = mode;
		addButton("save", "Save", this::btnSave);
		addButton("cancel", "Cancel", this::btnCancel);
	}

	private void btnSave(String id) {
		onOk.accept("filename");
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
