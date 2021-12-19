package uk.co.stikman.strip.client.json;

import java.util.ArrayList;
import java.util.List;

public class JSONArray {

	public List<Object> lst = new ArrayList<>();

	public int getInt(int idx) {
		checkIndex(idx);
		Object o = lst.get(idx);
		if (o instanceof Integer)
			return ((Integer) o).intValue();
		throw new JSONException("[" + idx + "] is not an integer");
	}

	private void checkIndex(int idx) {
		if (idx < 0 || idx >= lst.size())
			throw new IndexOutOfBoundsException("Index out of bounds: " + idx);
	}

	public String getString(int idx) {
		checkIndex(idx);
		Object o = lst.get(idx);
		if (o instanceof String)
			return (String) o;
		throw new JSONException("[" + idx + "] is not a string");
	}

	public float getFloat(int idx) {
		checkIndex(idx);
		Object o = lst.get(idx);
		if (o instanceof Float)
			return ((Float) o).floatValue();
		throw new JSONException("[" + idx + "] is not a float");
	}

	public JSONObject getObject(int idx) {
		checkIndex(idx);
		Object o = lst.get(idx);
		if (o instanceof JSONObject)
			return ((JSONObject) o);
		throw new JSONException("[" + idx + "] is not an object");

	}

	public JSONArray getArray(int idx) {
		checkIndex(idx);
		Object o = lst.get(idx);
		if (o instanceof JSONArray)
			return ((JSONArray) o);
		throw new JSONException("[" + idx + "] is not an array");
	}

	public int size() {
		return lst.size();
	}

	public JSONArray add(int n) {
		lst.add(Integer.valueOf(n));
		return this;
	}

	public JSONArray add(String s) {
		lst.add(s);
		return this;
	}

	public JSONArray add(float f) {
		lst.add(Float.valueOf(f));
		return this;
	}

	public JSONArray add(JSONObject obj) {
		lst.add(obj);
		return this;
	}

	public JSONArray add(JSONArray arr) {
		lst.add(arr);
		return this;
	}

	void addObj(Object val) {
		lst.add(val);
	}

}
