package uk.co.stikman.strip.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector3;
import uk.co.stikman.strip.client.model.Board;
import uk.co.stikman.strip.client.model.Component;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.ComponentLibrary;
import uk.co.stikman.strip.client.model.Hole;
import uk.co.stikman.strip.client.model.PinInstance;
import uk.co.stikman.strip.client.util.Util;

public class Stripboard implements EntryPoint {
	public static final float	PI			= 3.14159f;
	public static final float	PI2			= PI * 2;

	private static final int	TOPSIZE		= 64;
	private static final int	LEFTSIZE	= 220;
	private static final float	COPPER_GAP	= 0.15f;
	private static final float	HOLE_SIZE	= 0.2f;

	private Canvas				cnv;
	private Board				board;
	private AppTheme			theme		= new AppTheme();
	private Matrix3				view		= new Matrix3();
	private ComponentLibrary	library;
	private CursorTool			currentTool;
	private Vector3				tmpv		= new Vector3();
	private Matrix3				inverseView;
	private RenderIntf			renderer;
	private boolean				invalid;
	private ToolPanel toolPanel;

	public void onModuleLoad() {
		RootLayoutPanel root = RootLayoutPanel.get();

		cnv = Canvas.createIfSupported();
		root.add(cnv);
		root.setWidgetLeftRight(cnv, LEFTSIZE, Unit.PX, 0, Unit.PX);
		root.setWidgetTopBottom(cnv, TOPSIZE, Unit.PX, 0, Unit.PX);
		cnv.addKeyPressHandler(this::keyPress);
		cnv.addKeyDownHandler(this::keyDown);
		cnv.addMouseDownHandler(this::mouseDown);
		cnv.addMouseMoveHandler(this::mouseMove);
		cnv.addMouseUpHandler(this::mouseUp);

		HTML html = new HTML();
		String s = Document.get().getElementById("instructions").getInnerHTML();
		html.setHTML(s);
		html.setStyleName("instructions");
		root.add(html);
		root.setWidgetLeftRight(html, 0, Unit.PX, 0, Unit.PX);
		root.setWidgetTopHeight(html, 0, Unit.PX, TOPSIZE, Unit.PX);

		toolPanel = new ToolPanel();
		root.add(toolPanel);
		root.setWidgetLeftWidth(toolPanel, 0, Unit.PX, LEFTSIZE, Unit.PX);
		root.setWidgetTopBottom(toolPanel, TOPSIZE, Unit.PX, 0, Unit.PX);

		Window.addResizeHandler(this::resize);
		Scheduler.get().scheduleDeferred(() -> {
			resize(null);
			run(null);
		});
	}

	private void run(ClickEvent ev) {
		library = new ComponentLibrary();
		library.loadFrom(StripboardResources.INSTANCE.library().getText());
		renderer = new RenderIntf(view, this, cnv.getContext2d());
		setTool(new PointerTool());
		view.makeIdentity();
		view.scale(16.0f);
		updateView();
		board = new Board(64, 64);
		cnv.setFocus(true);
		drawBoard(board);
	}

	private void keyPress(KeyPressEvent ev) {
		try {
			if ('i' == ev.getCharCode()) {
				if (!(currentTool instanceof PointerTool))
					setTool(new PointerTool());
				addComponent();
				return;
			}

			//
			// otherwise pass on to the current tool
			//
			currentTool.keyPress(ev.getCharCode());
		} finally {
			if (invalid)
				render();
		}
	}

	private void keyDown(KeyDownEvent ev) {
		Util.log("key " + ev.getNativeKeyCode());
		if (ev.getNativeKeyCode() == 27) { // esc
			setTool(new PointerTool());
		}
		if (invalid)
			render();
	}

	private void mouseDown(MouseDownEvent ev) {
		currentTool.mouseDown(transformMouse(ev), ev.getNativeButton());
		render();
	}

	private Vector3 transformMouse(MouseEvent<?> ev) {
		Vector3 v = new Vector3(ev.getX(), ev.getY(), 1);
		Vector3 u = inverseView.multiply(v, tmpv);
		return u;
	}

	private void mouseMove(MouseMoveEvent ev) {
		currentTool.mouseMove(transformMouse(ev));
		render();
	}

	private void mouseUp(MouseUpEvent ev) {
		currentTool.mouseUp(transformMouse(ev), ev.getNativeButton());
		render();
	}

	private void render() {
		invalid = false;
		drawBoard(board);
		currentTool.render();
	}

	private void addComponent() {
		DialogBox dlg = new DialogBox(true, true);

		LayoutPanel lp = new LayoutPanel();
		ScrollPanel sp = new ScrollPanel();
		lp.add(sp);
		lp.setWidgetLeftRight(sp, 0, Unit.PX, 0, Unit.PX);
		lp.setWidgetTopBottom(sp, 0, Unit.PX, 0, Unit.PX);

		VerticalPanel vp = new VerticalPanel();
		sp.setWidget(vp);

		for (Component c : library.getComponents()) {
			Label l = new Label(c.getName());
			l.addClickHandler(event -> {
				dlg.hide();
				cnv.setFocus(true);
				setTool(new PlaceComponentCursor(c));
			});
			l.addStyleName("sample");
			vp.add(l);
		}

		dlg.setText("Components");
		dlg.setGlassEnabled(true);
		dlg.setWidget(lp);
		lp.setSize("500px", "300px");
		dlg.show();
		dlg.center();
	}

	private void setTool(CursorTool tool) {
		if (currentTool != null)
			currentTool.end();
		this.currentTool = tool;
		tool.setApp(this);
		tool.start();
		toolPanel.updateFrom(tool);
		invalidate();
	}

	private void resize(ResizeEvent ev) {
		cnv.setCoordinateSpaceWidth(cnv.getOffsetWidth());
		cnv.setCoordinateSpaceHeight(cnv.getOffsetHeight());
	}

	private void updateView() {
		inverseView = view.inverse();
	}

	private void drawBoard(Board brd) {
		String copper = theme.getCopperColour().css();
		String base = theme.getBoardBaseColour().css();
		String holecolour = theme.getHoleColour().css();


		Context2d ctx = cnv.getContext2d();
		float w = cnv.getOffsetWidth();
		float h = cnv.getOffsetHeight();
		ctx.clearRect(0, 0, w, h);

		ctx.setFillStyle(base);
		CanvasUtil.fillRect(ctx, view, 0, 0, brd.getWidth(), brd.getHeight());

		for (int y = 0; y < brd.getHeight(); ++y) {
			ctx.setFillStyle(copper);
			CanvasUtil.fillRect(ctx, view, 0, y + COPPER_GAP, brd.getWidth(), 1.0f - COPPER_GAP * 2.0f);
		}

		ctx.setFillStyle(holecolour);
		for (int y = 0; y < brd.getHeight(); ++y) {
			for (int x = 0; x < brd.getWidth(); ++x) {
				Hole hole = brd.getHole(x, y);

				ctx.beginPath();
				CanvasUtil.circle(ctx, view, x + 0.5f, y + 0.5f, HOLE_SIZE);
				ctx.fill();

				if (hole.isBroken()) 
					renderer.drawBreak(x, y);
			}
		}

		for (ComponentInstance comp : board.getComponents()) {
			PinInstance p = comp.getPin(0);
			ComponentRenderer.render(this, comp, p.getPosition().x, p.getPosition().y);
		}

	}

	public RenderIntf getRenderer() {
		return renderer;
	}

	public void setCursor(Cursor x) {
		cnv.getElement().getStyle().setCursor(x);
	}

	public final AppTheme getTheme() {
		return theme;
	}

	public Board getBoard() {
		return board;
	}

	public void invalidate() {
		this.invalid = true;
	}
}
