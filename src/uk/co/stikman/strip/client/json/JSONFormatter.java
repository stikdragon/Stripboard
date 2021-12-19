package uk.co.stikman.strip.client.json;

import java.util.Map.Entry;

public class JSONFormatter {

	public String format(JSONObject root) {
		StringBuilder sb = new StringBuilder();
		printValue(sb, root);
		return sb.toString();
	}

	private void printObj(StringBuilder out, JSONObject obj) {
		out.append("{");
		String sep = "";
		for (Entry<String, Object> k : obj.map.entrySet()) {
			out.append(sep);
			sep = ",";
			printValue(out, k.getKey());
			out.append(":");
			printValue(out, k.getValue());
		}

		out.append("}");
	}

	private void printArr(StringBuilder out, JSONArray arr) {
		out.append("[");
		String sep = "";
		for (Object o : arr.lst) {
			out.append(sep);
			sep = ",";
			printValue(out, o);
		}
		out.append("]");
	}

	private void printValue(StringBuilder out, Object value) {
		if (value == null) {
			out.append("null");
		} else if (value instanceof JSONObject) {
			printObj(out, (JSONObject) value);
		} else if (value instanceof JSONArray) {
			printArr(out, (JSONArray) value);
		} else if (value instanceof Integer) {
			int i = ((Integer) value).intValue();
			out.append(i);
		} else if (value instanceof Float) {
			float f = ((Float) value).floatValue();
			out.append(f);
		} else if (value instanceof String) {
			String s = (String) value;
			//
			// need to escape this
			//
			out.append("\"");
			for (char ch : s.toCharArray()) {
				switch (ch) {
				case '"':
					out.append("\\\"");
					break;
				case '\\':
					out.append("\\\\");
					break;
				case '/':
					out.append("\\/");
					break;
				case '\b':
					out.append("\\b");
					break;
				case '\f':
					out.append("\\f");
					break;
				case '\n':
					out.append("\\n");
					break;
				case '\r':
					out.append("\\r");
					break;
				case '\t':
					out.append("\\t");
					break;
				default:
					out.append(ch);
				}
			}
			out.append("\"");
		} else
			throw new RuntimeException("Unexpected type: " + value.getClass().getSimpleName());
	}

}
