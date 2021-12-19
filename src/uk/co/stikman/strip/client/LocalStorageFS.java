package uk.co.stikman.strip.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.storage.client.Storage;

import uk.co.stikman.strip.client.json.JSONObject;
import uk.co.stikman.strip.client.json.JSONParser;

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

	@Override
	public List<String> fetchNames() {
		List<String> res = new ArrayList<>();
		for (int i = 0; i < storage.getLength(); ++i) {
			String k = storage.key(i);
			if (k.startsWith(prefix + "."))
				res.add(k.substring(prefix.length() + 1));
		}
		return res;
	}

	@Override
	public JSONObject fetch(String name) {
		return new JSONParser(storage.getItem(prefix + "." + name)).parse();
	}

}
