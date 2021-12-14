package uk.co.stikman.strip.client;

import uk.co.stikman.strip.client.util.Colour;

public class AppTheme {

	private Colour	boardBaseColour		= new Colour("AD8837");
	private Colour	copperColour		= new Colour("AA5234");
	private Colour	holeColour			= new Colour("3D3D3D");
	private Colour	brokenHoleColour	= new Colour("FFFFFF7C");
	private Colour	pinColour			= new Colour("50EE527C");
	private Colour	highlightColour		= new Colour("FFFFFF50");
	private Colour	leadColour			= new Colour("404047");
	private Colour	componentFill		= new Colour("00000080");
	private Colour	componentOutline	= new Colour("000000E0");
	private Colour	wireColour			= new Colour("707ff0D1");
	private Colour	ghostColour			= new Colour("FFFFFF7C");
	private Colour	errorColour			= new Colour("FF4040C0");

	public AppTheme() {
		super();
	}

	public Colour getBoardBaseColour() {
		return boardBaseColour;
	}

	public final Colour getCopperColour() {
		return copperColour;
	}

	public final Colour getHoleColour() {
		return holeColour;
	}

	public final Colour getPinColour() {
		return pinColour;
	}

	public final Colour getHighlightColour() {
		return highlightColour;
	}

	public final Colour getLeadColour() {
		return leadColour;
	}

	public final Colour getComponentFill() {
		return componentFill;
	}

	public final Colour getBrokenHoleColour() {
		return brokenHoleColour;
	}

	public Colour getWireColour() {
		return wireColour;
	}

	public final Colour getComponentOutline() {
		return componentOutline;
	}

	public final Colour getGhostColour() {
		return ghostColour;
	}

	public final Colour getErrorColour() {
		return errorColour;
	}

}
