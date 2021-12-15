package uk.co.stikman.strip.client;

import uk.co.stikman.strip.client.json.JSONObject;

public interface AppFileSystem {

	void save(String filename, JSONObject root);

}
