package uk.co.stikman.strip.client;

import java.util.List;

import uk.co.stikman.strip.client.math.Vector3;
import uk.co.stikman.strip.client.model.Component;
import uk.co.stikman.strip.client.model.ComponentInstance;

public class WiringTool extends PlaceComponentTool {
	private int					currentHoleX;
	private int					currentHoleY;
	private Vector3				downAt;
	private String				hilightColour;
	private ComponentInstance	wire;

	public WiringTool(Component wiretype) {
		super(wiretype);
	}

	@Override
	protected void fillActionList(List<ToolUIHint> lst) {
		lst.add(new ToolUIHint("ESC", "Cancel"));
	}
}
