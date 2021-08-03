package uk.co.stikman.strip.client;

import uk.co.stikman.strip.client.util.Colour;

public class AppTheme {

	private Colour	boardBaseColour		= new Colour("AD8837");
	private Colour	copperColour		= new Colour("AA5234");
	private Colour	holeColour			= new Colour("3D3D3D");
	private Colour	brokenHoleColour	= new Colour("FFFFFF7C");
	private Colour	pinColour			= new Colour("50EE52");
	private Colour	highlightColour		= new Colour("55FF557C");
	private Colour	leadColour			= new Colour("202020");
	private Colour	componentFill		= new Colour("000000A0");

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

}