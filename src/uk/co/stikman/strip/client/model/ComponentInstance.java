package uk.co.stikman.strip.client.model;

public class ComponentInstance {
	private final Component	component;
	private String			name;

	public ComponentInstance(Component component) {
		super();
		this.component = component;
	}

	public final Component getComponent() {
		return component;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ComponentInstance [component=" + component + ", name=" + name + "]";
	}
}
