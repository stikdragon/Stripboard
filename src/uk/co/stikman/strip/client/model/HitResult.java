package uk.co.stikman.strip.client.model;

public class HitResult {
	private HitResultType	type;

	private Object			object;

	public HitResult(HitResultType type, Object object) {
		super();
		this.type = type;
		this.object = object;
	}

	public final HitResultType getType() {
		return type;
	}

	public final Object getObject() {
		return object;
	}

	@Override
	public String toString() {
		return object.getClass().getSimpleName();
	}
}
