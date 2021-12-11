package uk.co.stikman.strip.client.gfx;

import uk.co.stikman.strip.client.ComponentRenderer;
import uk.co.stikman.strip.client.RenderIntf;
import uk.co.stikman.strip.client.RenderState;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.PinInstance;

public class WireVismod implements ComponentVisualModel {


	private ComponentRenderer owner;

	public WireVismod(ComponentRenderer owner) {
		this.owner = owner;
	}

	
	@Override
	public float[] outline(ComponentInstance comp) {
		PinInstance p1 = comp.getPin(0);
		PinInstance p2 = comp.getPin(1);
		return null;
		//Vector2 a = new Vector2(p1.getPosition();

	}

	@Override
	public void render(RenderIntf ctx, ComponentInstance comp, int x0, int y0, RenderState state) {
		PinInstance p1 = comp.getPin(0);
		PinInstance p2 = comp.getPin(1);

		if (!(x0 == p2.getPosition().x && y0 == p2.getPosition().y)) {
			ctx.drawPin(p1.getPosition().x, p1.getPosition().y, state);
			ctx.drawPin(p2.getPosition().x, p2.getPosition().y, state);
			ctx.drawWire(p1.getPosition(), p2.getPosition());
		}
	}
}
