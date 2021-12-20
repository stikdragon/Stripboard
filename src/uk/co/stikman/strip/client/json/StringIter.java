package uk.co.stikman.strip.client.json;

public class StringIter {

	private char[]	src;
	private int		pos	= -1;
	private int		len;

	public StringIter(String input) {
		this.src = input.toCharArray();
		this.len = input.length();
	}

	/**
	 * returns <code>0</code> when at end of stream
	 * 
	 * @return
	 */
	public char next() {
		if (pos >= len)
			return 0;
		return src[++pos];
	}

	public boolean hasNext() {
		return pos < len - 1;
	}

	public char peek() {
		if (pos >= len)
			return 0;
		return src[pos + 1];
	}

	public void rewind() {
		if (pos > -1)
			--pos;
	}

	public void expect(char ch) {
		char c = next();
		if (c != ch)
			throw new JSONException("Expected [" + ch + "] but found [" + c + "]");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int j = 200;
		for (int i = pos+1; pos < len; ++i) {
			sb.append(src[i]);
			if (--j <= 0) {
				sb.append("...");
				break;
			}
		}
		return sb.toString();
	}

	public int getPosition() {
		return pos + 1;
	}

}
