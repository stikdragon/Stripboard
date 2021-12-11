package uk.co.stikman.strip.client;

import uk.co.stikman.strip.client.model.Component;

public class VertCacheKey {
	private Component	component;
	private String		extra;

	public VertCacheKey(Component component) {
		this.component = component;
		this.extra = null;
	}

	public VertCacheKey(Component component, String extra) {
		super();
		this.component = component;
		this.extra = extra;
	}

	public VertCacheKey set(Component component) {
		this.component = component;
		this.extra = null;
		return this;
	}

	public VertCacheKey set(Component component, String extra) {
		this.component = component;
		this.extra = extra;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((component == null) ? 0 : component.hashCode());
		result = prime * result + ((extra == null) ? 0 : extra.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VertCacheKey other = (VertCacheKey) obj;
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		if (extra == null) {
			if (other.extra != null)
				return false;
		} else if (!extra.equals(other.extra))
			return false;
		return true;
	}

}