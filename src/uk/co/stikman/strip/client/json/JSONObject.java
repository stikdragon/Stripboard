package uk.co.stikman.strip.client.json;

import java.util.HashMap;
import java.util.Map;

public class JSONObject {
	Map<String, Object> map = new HashMap<>();

	public JSONObject() {
		super();
	}

	public int getInt(String k) {
		checkKey(k);
		Object o = map.get(k);
		if (o instanceof Integer)
			return ((Integer) o).intValue();
		throw new JSONException("[" + k + "] is not an integer");
	}

	public String getString(String k) {
		checkKey(k);
		Object o = map.get(k);
		if (o instanceof String)
			return (String) o;
		throw new JSONException("[" + k + "] is not a string");
	}

	public float getFloat(String k) {
		checkKey(k);
		Object o = map.get(k);
		if (o instanceof Float)
			return ((Float) o).floatValue();
		throw new JSONException("[" + k + "] is not a float");
	}

	public JSONObject getObject(String k) {
		checkKey(k);
		Object o = map.get(k);
		if (o instanceof JSONObject)
			return ((JSONObject) o);
		throw new JSONException("[" + k + "] is not an object");

	}

	public JSONArray getArray(String k) {
		checkKey(k);
		Object o = map.get(k);
		if (o instanceof JSONArray)
			return ((JSONArray) o);
		throw new JSONException("[" + k + "] is not an array");

	}

	public JSONObject put(String k, int val) {
		map.put(k, Integer.valueOf(val));
		return this;
	}

	public JSONObject put(String k, String val) {
		map.put(k, val);
		return this;
	}

	public JSONObject put(String k, float val) {
		map.put(k, Float.valueOf(val));
		return this;
	}

	public JSONObject put(String k, JSONObject val) {
		map.put(k, val);
		return this;
	}

	public JSONObject put(String k, JSONArray val) {
		map.put(k, val);
		return this;
	}

	public boolean contains(String k) {
		return map.containsKey(k);
	}

	private final void checkKey(String k) {
		if (!map.containsKey(k))
			throw new JSONException("[" + k + "] does not exist");
	}

	public String toString() {
		return new JSONFormatter().format(this);
	}

	void put(String k, Object val) {
		map.put(k, val);
	}

	public String optString(String key, String def) {
		if (!map.containsKey(key))
			return def;
		Object o = map.get(key);
		if (o instanceof String)
			return (String) o;
		throw new JSONException("[" + key + "] is not a string");
	}

}
