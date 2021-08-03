package uk.co.stikman.strip.client.util;

import java.util.ArrayList;
import java.util.List;

public class Section {
	private String			name;
	private List<IniPair>	pairs	= new ArrayList<>();

	public Section(String name) {
		super();
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	public final List<IniPair> getPairs() {
		return pairs;
	}

	public boolean containsKey(String k) {
		IniPair x = find(k);
		return x != null;
	}

	private IniPair find(String k) {
		return pairs.stream().filter(a -> a.getName().equals(k)).findFirst().orElse(null);
	}

	public void add(String key, String val) {
		pairs.add(new IniPair(key, val));
	}

	public String get(String key) {
		IniPair x = find(key);
		if (x == null)
			return null;
		return x.getValue();
	}

}
