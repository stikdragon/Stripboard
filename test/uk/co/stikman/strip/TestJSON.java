package uk.co.stikman.strip;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import uk.co.stikman.strip.client.json.JSONObject;
import uk.co.stikman.strip.client.json.JSONParser;

public class TestJSON {
	@Test
	public void testBasicJSON() throws IOException {
		try (InputStream is = TestJSON.class.getResourceAsStream("test1.json")) {
			JSONParser json = new JSONParser(IOUtils.toString(is));
			JSONObject obj = json.parse();
			System.out.println(obj.toString());
		}

	}
}
