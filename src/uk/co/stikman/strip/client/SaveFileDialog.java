package uk.co.stikman.strip.client;

public class SaveFileDialog extends StripDialog {
	public SaveFileDialog(Stripboard app, String caption, String filename) {
		super(app, caption, 400, 440);
		addButton("save", "Save", this::btnSave);
		addButton("cancel", "Cancel", this::btnCancel);
	}

	private void btnSave(String id) {
		hide();
	}

	private void btnCancel(String id) {
		hide();
	}
}
