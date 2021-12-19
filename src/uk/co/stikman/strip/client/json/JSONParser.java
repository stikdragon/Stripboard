package uk.co.stikman.strip.client.json;

public class JSONParser {

	private StringIter iter;

	public JSONParser(String input) {
		iter = new StringIter(input);
	}

	public JSONObject parse() {
		try {
			return readObject();
		} catch (Exception e) {
			throw new JSONException("Invalid JSON around char [" + iter.getPosition() + "]: " + e.getMessage(), e);
		}
	}

	private JSONObject readObject() {
		iter.expect('{');
		consumeWS();
		JSONObject jo = new JSONObject();
		if (iter.peek() == '}') {// empty object
			iter.next();
			return jo;
		}

		for (;;) {
			consumeWS();
			String k = readString();
			consumeWS();
			iter.expect(':');
			consumeWS();
			Object val = readValue();
			consumeWS();
			jo.put(k, val);

			char ch = iter.next();
			if (ch == ',')
				continue;

			//
			// can only be } at this point
			//
			if (ch != '}')
				throw new JSONException("Expected } or ,");
			break;
		}

		return jo;
	}

	private JSONArray readArray() {
		iter.expect('[');
		consumeWS();
		JSONArray arr = new JSONArray();
		if (iter.peek() == ']') { // empty array
			iter.next();
			return arr;
		}

		for (;;) {
			consumeWS();
			Object val = readValue();
			consumeWS();
			arr.addObj(val);

			char ch = iter.next();
			if (ch == ',')
				continue;

			//
			// can only be ] at this point
			//
			if (ch != ']')
				throw new JSONException("Expected ] or ,");
			break;
		}

		return arr;
	}

	private Object readValue() {
		//
		// see what it starts with
		//
		char ch = iter.peek();
		if (ch == '{')
			return readObject();
		if (ch == '[')
			return readArray();
		if (ch == '"')
			return readString();

		//
		// otherwise it's a number, or true/false/null
		//
		if (ch == 't') {
			readLit("true");
			return Boolean.TRUE;
		}
		if (ch == 'f') {
			readLit("false");
			return Boolean.FALSE;
		}
		if (ch == 'n') {
			readLit("null");
			return null;
		}

		//
		// number, read until something that isn't in [0-9eE-+], then 
		// let java's normal number parser sort it out
		//
		StringBuilder sb = new StringBuilder();
		boolean isint = true;
		for (;;) {
			ch = iter.next();
			if (ch >= '0' && ch <= '9') {
				sb.append(ch);
			} else if (ch == 'e' || ch == 'E' || ch == '-' || ch == '+') {
				sb.append(ch);
				isint = false;
			} else {
				iter.rewind();
				if (isint) {
					long l = Long.parseLong(sb.toString());
					if (l > Integer.MAX_VALUE)
						return Float.valueOf(l);
					return Integer.valueOf((int) l);
				} else {
					return Float.valueOf(sb.toString());
				}
			}
		}
	}

	private void readLit(String lit) {
		for (char ch : lit.toCharArray())
			iter.expect(ch);
	}

	private String readString() {
		StringBuilder sb = new StringBuilder();
		iter.expect('"');
		boolean esc = false;
		for (;;) {
			char ch = iter.next();
			if (esc) {
				switch (ch) {
				case '"':
				case '\\':
				case '/':
					sb.append(ch);
					break;
				case 'b':
					sb.append('\b');
					break;
				case 'f':
					sb.append('\f');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 'r':
					sb.append('\r');
					break;
				case 't':
					sb.append('\t');
					break;
				default:
					throw new JSONException("Unexpected escape: " + ch);
				}
				esc = false;
			} else {
				if (ch == '"') {
					return sb.toString();
				} else if (ch == '\\') {
					esc = true;
				} else {
					sb.append(ch);
				}
			}

		}
	}

	/**
	 * read and discard any whitespace
	 */
	private void consumeWS() {
		for (;;) {
			char ch = iter.next();
			switch (ch) {
			case '\n':
			case '\r':
			case '\t':
			case ' ':
				break;
			default:
				iter.rewind();
				return;
			}
		}
	}
}
