package uk.co.stikman.strip.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Ini {

	private boolean			allowDuplicates	= false;
	private boolean			caseSensitive	= true;
	private List<Section>	sections		= new ArrayList<>();

	public Ini(boolean allowDuplicates, boolean caseSensitive) {
		super();
		this.allowDuplicates = allowDuplicates;
		this.caseSensitive = caseSensitive;
	}

	public void parse(String source) {
		int linecount = 0;
		Section section = null;
		for (String line_ : source.split("\\r?\\n")) {
			try {
				++linecount;
				String line = line_.trim();
				if (line.startsWith("#"))
					continue;
				if (line.isEmpty())
					continue;

				if (line.startsWith("[")) {
					if (!line.endsWith("]"))
						throw new RuntimeException("Invalid section name, missing ]");

					section = new Section(line.substring(1, line.length() - 1));
					if (!allowDuplicates && findSection(section.getName()) != null)
						throw new IllegalArgumentException("Section [" + section.getName() + "] is already declared");
					sections.add(section);
				} else {
					int n = line.indexOf('=');
					if (n == -1)
						throw new IllegalArgumentException("Invalid pair: [" + line + "]");
					String left = line.substring(0, n );
					String right = line.substring(n+1);
					if (!allowDuplicates && section.containsKey(left))
						throw new RuntimeException("Section [" + section.getName() + "] already contains key [" + left + "]");
					section.add(left, right);
				}
				
			} catch (Exception e) {
				throw new RuntimeException("Could not read INI file because of error on line [" + linecount + "]: " + e.getMessage(), e);
			}
		}
	}

	public Section findSection(String name) {
		return sections.stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
	}

	public Section getSection(String name) {
		Section s = findSection(name);
		if (s == null)
			throw new NoSuchElementException("Section [" + name + "] does not exist");
		return s;
	}

	public final boolean isAllowDuplicates() {
		return allowDuplicates;
	}

	public final boolean isCaseSensitive() {
		return caseSensitive;
	}

	public List<Section> getSections() {
		return sections;
	}

}
