package uk.co.stikman.strip.client;

import com.google.gwt.storage.client.Storage;

import uk.co.stikman.strip.client.json.JSONObject;

public class LocalStorageFS implements AppFileSystem {
	private Storage	storage	= null;
	private String	prefix;

	public LocalStorageFS(String prefix) {
		storage = Storage.getLocalStorageIfSupported();
		if (storage == null)
			throw new RuntimeException("No support for local storage");
		this.prefix = prefix;
	}

	@Override
	public void save(String filename, JSONObject root) {
		storage.setItem(prefix + "." + filename, root.toString());
	}

}
