package uk.co.stikman.strip.client;

import java.util.ArrayList;
import java.util.List;

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
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

import uk.co.stikman.strip.client.math.Matrix3;
import uk.co.stikman.strip.client.math.Vector2;
import uk.co.stikman.strip.client.math.Vector2i;
import uk.co.stikman.strip.client.math.Vector3;
import uk.co.stikman.strip.client.model.Board;
import uk.co.stikman.strip.client.model.Component;
import uk.co.stikman.strip.client.model.ComponentInstance;
import uk.co.stikman.strip.client.model.ComponentLibrary;
import uk.co.stikman.strip.client.model.Hole;
import uk.co.stikman.strip.client.model.PinInstance;
import uk.co.stikman.strip.client.util.Util;

public class Stripboard implements EntryPoint {
	public static final float	PI					= 3.14159f;
	public static final float	PI2					= PI * 2;

	private static final int	TOPSIZE				= 64;
	private static final int	LEFTSIZE			= 220;
	private static final float	COPPER_GAP			= 0.15f;
	private static final float	HOLE_SIZE			= 0.2f;
	public static final float	EXPAND_AMOUNT		= 0.4f;

	private Canvas				cnv;
	private Board				board;
	private AppTheme			theme				= new AppTheme();
	private Matrix3				view				= new Matrix3();
	private ComponentLibrary	library;
	private AbstractTool		currentTool;
	private Vector2				tmpv				= new Vector2();
	private Matrix3				inverseView;
	private RenderIntf			renderer;
	private boolean				invalid;
	private ToolPanel			toolPanel;
	private ComponentRenderer	componentRenderer	= new ComponentRenderer(this);
	private Canvas				boardCanvas;
	private AppFileSystem fileSystem;

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

		HorizontalPanel hp = new HorizontalPanel();
		hp.setCellVerticalAlignment(hp, HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		root.add(hp);
		HTML title = new HTML("<h2>Stripboard Editor</h2>");
		title.setStyleName("instructions");
		hp.add(title);
		hp.add(createMenu());
		root.setWidgetLeftRight(hp, 0, Unit.PX, 0, Unit.PX);
		root.setWidgetTopHeight(hp, 0, Unit.PX, TOPSIZE, Unit.PX);

		toolPanel = new ToolPanel();
		root.add(toolPanel);
		root.setWidgetLeftWidth(toolPanel, 0, Unit.PX, LEFTSIZE, Unit.PX);
		root.setWidgetTopBottom(toolPanel, TOPSIZE, Unit.PX, 0, Unit.PX);

		fileSystem = new LocalStorageFS();
		
		Window.addResizeHandler(this::resize);
		Scheduler.get().scheduleDeferred(() -> {
			resize(null);
			run(null);
		});
	}

	private void mnuNew() {
		if (board != null) {
			if (board.isModified()) {
				// TODO: show save dialog
			}
		}
		board = new Board(64, 64);
		drawBoard(board);
	}

	private void mnuAbout() {
		AboutDialog dlg = new AboutDialog(this);
		dlg.show();
	}
	private void mnuSave() {
		if (board.getFilename() != null) {
			fileSystem.save(board.getFilename(), board.toJSON());
		} else {
			//
			// pick filename
			//
			SaveFileDialog dlg  = new SaveFileDialog(this, "Save", null);
			dlg.show();
		}
	}

	private Widget createMenu() {

		MenuBar mnuFile = new MenuBar(true);
		mnuFile.addItem("New", this::mnuNew);
		mnuFile.addItem("Save", this::mnuSave);
		mnuFile.addItem("Save As...", () -> {
		});

		MenuBar mnuEdit = new MenuBar(true);
		mnuEdit.addItem("Undo", () -> {
		});
		mnuEdit.addItem("Redo", () -> {
		});
		mnuEdit.addItem("Select All", () -> {
		});

		MenuBar mnuHelp = new MenuBar(true);
		mnuHelp.addItem("Help...", () -> {
		});
		mnuHelp.addItem("About...", this::mnuAbout);

		// Make a new menu bar, adding a few cascading menus to it.
		MenuBar menu = new MenuBar();
		menu.addItem("File", mnuFile);
		menu.addItem("Edit", mnuEdit);
		menu.addItem("Help", mnuHelp);

		return menu;
	}

	private void run(ClickEvent ev) {
		library = new ComponentLibrary();
		library.loadFrom(StripboardResources.INSTANCE.library().getText());
		renderer = new RenderIntf(view, this, cnv.getContext2d());
		setTool(new PointerTool(this));
		view.makeIdentity();
		view.scale(16.0f);
		updateView();
		mnuNew();
		cnv.setFocus(true);
		drawBoard(board);
	}

	private void keyPress(KeyPressEvent ev) {
		try {
			if ('i' == ev.getCharCode() || 'e' == ev.getCharCode()) {
				if (!(currentTool instanceof PointerTool))
					setTool(new PointerTool(this));
				addComponent();
				return;
			}

			if ('w' == ev.getCharCode()) {
				setTool(new WiringTool(this, library.get("Wire")));
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
		if (ev.getNativeKeyCode() == 27 || ev.getNativeKeyCode() == 81) { // esc/Q
			setTool(new PointerTool(this));
		}
		if (invalid)
			render();
	}

	private void mouseDown(MouseDownEvent ev) {
		if (currentTool == null)
			return;
		currentTool.mouseDown(transformMouse(ev), ev.getNativeButton());
		render();
	}

	private Vector2 transformMouse(MouseEvent<?> ev) {
		Vector2 v = new Vector2(ev.getX(), ev.getY());
		Vector2 u = inverseView.multiply(v, tmpv);
		return u;
	}

	private void mouseMove(MouseMoveEvent ev) {
		if (currentTool == null)
			return;
		currentTool.mouseMove(transformMouse(ev));
		render();
	}

	private void mouseUp(MouseUpEvent ev) {
		if (currentTool == null)
			return;
		currentTool.mouseUp(transformMouse(ev), ev.getNativeButton());
		render();
	}

	private void render() {
		invalid = false;
		drawBoard(board);
		if (currentTool == null)
			return;
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
			if (c.getName().equals("Wire")) // wires are a special case
				continue;
			Label l = new Label(c.getName());
			l.addClickHandler(event -> {
				dlg.hide();
				cnv.setFocus(true);
				setTool(new PlaceComponentTool(this, c));
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

	private void setTool(AbstractTool tool) {
		if (currentTool != null)
			currentTool.end();
		this.currentTool = tool;
		tool.start();
		updateToolPanel();
		invalidate();
	}

	public void updateToolPanel() {
		toolPanel.updateFrom(currentTool);
	}

	private void resize(ResizeEvent ev) {
		cnv.setCoordinateSpaceWidth(cnv.getOffsetWidth());
		cnv.setCoordinateSpaceHeight(cnv.getOffsetHeight());
	}

	private void updateView() {
		inverseView = view.inverse();
	}

	private void drawBoard(Board brd) {
		Canvas cbrd = getBoardCanvas(brd);
		cnv.getContext2d().drawImage(cbrd.getCanvasElement(), 0, 0);
		List<Hole> errorholes = new ArrayList<>();
		for (int y = 0; y < brd.getHeight(); ++y) {
			for (int x = 0; x < brd.getWidth(); ++x) {
				Hole hole = brd.getHole(x, y);
				if (hole.isBroken())
					renderer.drawBreak(x, y);
				if (hole.getPins().size() > 0)
					errorholes.add(hole);
			}
		}

		for (ComponentInstance comp : board.getComponents()) {
			PinInstance p = comp.getPin(0);
			getComponentRenderer().render(this, comp, p.getPosition().x, p.getPosition().y, RenderState.NORMAL);
		}

		for (Hole h : errorholes)
			renderer.drawCircle(h.getX() + 0.5f, h.getY() + 0.5f, 0.6f, null, theme.getErrorColour().css());

		//
		// render selected items
		//
	}

	/**
	 * generate a second canvas with the stripboard graphic on, since that's
	 * slow to render. regenerate on board size change (or zoom?)
	 * 
	 * @param brd
	 * @return
	 */
	private Canvas getBoardCanvas(Board brd) {
		if (boardCanvas == null) {
			String copper = theme.getCopperColour().css();
			String base = theme.getBoardBaseColour().css();
			String holecolour = theme.getHoleColour().css();

			boardCanvas = Canvas.createIfSupported();
			Vector2 v = new Vector2(brd.getWidth(), brd.getHeight());
			Vector2 v2 = view.multiply(v, new Vector2());

			boardCanvas.setPixelSize((int) v2.x, (int) v2.y);
			boardCanvas.setCoordinateSpaceWidth((int) v2.x);
			boardCanvas.setCoordinateSpaceHeight((int) v2.y);

			Context2d ctx = boardCanvas.getContext2d();
			float w = boardCanvas.getOffsetWidth();
			float h = boardCanvas.getOffsetHeight();
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
					ctx.beginPath();
					CanvasUtil.circle(ctx, view, x + 0.5f, y + 0.5f, HOLE_SIZE);
					ctx.fill();
				}
			}

		}

		return boardCanvas;
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

	public final ComponentRenderer getComponentRenderer() {
		return componentRenderer;
	}
}
