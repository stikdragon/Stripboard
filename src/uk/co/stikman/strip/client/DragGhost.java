package uk.co.stikman.strip.client;

import java.util.List;

import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector2;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.ComponentPoly;

public class DragGhost {
	private Object				object;
	private GhostType			type;
	private Vector2				downAt;
	private Vector2				offset	= new Vector2();
	private Vector2				current	= new Vector2();
	private Vector2				tv		= new Vector2();
	private Matrix3				tm		= new Matrix3();
	private List<ComponentPoly>	polys;
	private AppTheme theme;

	public DragGhost(PointerTool tool, Object object, GhostType type, Vector2 downAt, Vector2 offset) {
		super();
		this.theme = tool.getApp().getTheme();
		this.object = object;
		this.type = type;
		this.downAt = downAt;
		this.offset = offset;
		current.set(downAt);

		if (type == GhostType.COMPONENT) {
			ComponentInstance ci = (ComponentInstance) object;
			polys = ci.getComponent().getPolys(ci);
		}
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public GhostType getType() {
		return type;
	}

	public void setType(GhostType type) {
		this.type = type;
	}

	public Vector2 getCurrentPosition() {
		return current;
	}

	public Vector2 getDownAt() {
		return downAt;
	}

	public void setDownAt(Vector2 downAt) {
		this.downAt = downAt;
	}

	public Vector2 getOffset() {
		return offset;
	}

	public void setOffset(Vector2 offset) {
		this.offset.set(offset);
	}

	public void setCurrentPosition(Vector2 pos) {
		current.set(pos);
	}

	public void render(RenderIntf ctx) {
		tv.set(current).add(offset);
		tv.round();
		if (type == GhostType.PIN) {
			ctx.drawCircle(tv.x + 0.5f, tv.y + 0.5f, 0.5f, theme.getGhostColour().css(), theme.getHighlightColour().css());
			ctx.drawCircle(tv.x + 0.5f, tv.y + 0.5f, 0.75f, null, theme.getErrorColour().css());
		} else if (type == GhostType.COMPONENT) {
			tm.makeTranslation(tv);
			for (ComponentPoly p : polys)
				ctx.drawPoly(theme.getGhostColour().css(), theme.getHighlightColour().css(), tm, p);
		}
	}

}
