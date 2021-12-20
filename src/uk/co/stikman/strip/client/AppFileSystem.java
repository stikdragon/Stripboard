package uk.co.stikman.strip.client;

import java.util.List;

import uk.co.stikman.strip.client.json.JSONObject;

public interface AppFileSystem {

	void save(String filename, JSONObject root);

	List<String> fetchNames();

	JSONObject fetch(String name);

}
