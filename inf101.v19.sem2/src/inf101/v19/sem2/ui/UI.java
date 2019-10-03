package inf101.v19.sem2.ui;

import inf101.v19.sem2.examplegame.Game;
import inf101.v19.sem2.interfaces.IUserInterface;

import java.io.File;

import inf101.v19.sem2.interfaces.IUIGame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;

/**
 * Controls a grid cell by modulating the visibility of the layers corresponding to the cell.  
 * @author yngve
 *
 */
class GridCellController {
	private Rectangle gridCell;
	private Circle pegWhite;
	private Circle pegRed;

	GridCellController(Rectangle gridCell, Circle pegWhite, Circle pegRed) {
		this.gridCell = gridCell;
		this.pegWhite = pegWhite;
		this.pegRed = pegRed;
	}

	public Rectangle getGridCellRect() {
		return this.gridCell;
	}

	public void setState(GridCellStates state) {
		switch (state) {
		case PEG_WHITE:
			this.pegRed.setOpacity(0);
			this.pegWhite.setOpacity(1);
			break;
		case PEG_RED:
			this.pegRed.setOpacity(1);
			this.pegWhite.setOpacity(0);
			break;
		case CLEAR:
			this.pegRed.setOpacity(0);
			this.pegWhite.setOpacity(0);
			break;
		}
	}
}

/**
 * Example implementation of a UI. This is completely decoupled from the game and can thus be controlled from the outside.
 * The controller logic can written separately as a layer "gluing" together: 1) the game model (the basic implementation),
 * and 2) the user interface. OR the controller logic can be written directly into the game implementation.
 * There are no restrictions on how to do this but ideally -- the UI should be called from ONE file (ex Game.java)
 * rather than from multiple smaller files (ex Ship.java, Player.java) etc.
 * @author yngve
 *
 */
public class UI implements IUserInterface {
	private Stage stage;

	private Pane layerGame;
	private Label labelStatus;

	private Group layerShips;
	private Group layerGrid;
	private Group layerPegsWhite;
	private Group layerPegsRed;

	private Button button;
	
	private Rectangle shipRectInteractive;

	private GridCellController[][] gridCellControllers;

	private IUIGame game;

	private Label labelP;


	UI(Stage stage) {
		this.stage = stage;
		this.initLayers();
	}
	
	public void writeStatus(String text) {
		this.labelStatus.setText(text);
	}

	private void initLayers() {
		Stage stage = this.stage;
		stage.setTitle("Battleships");

		VBox root = new VBox();

		Pane layerGame = new Pane();

//		layerGame.setStyle("-fx-background-color: aqua");
		layerGame.prefHeightProperty().bind(stage.heightProperty());
		layerGame.prefWidthProperty().bind(stage.widthProperty());
		
		layerGame.getStyleClass().add("game-background");

		Group layerGrid = new Group();
		Group layerShips = new Group();
		Group layerShipsInteractive = new Group();
		Group layerPegsRed = new Group();
		Group layerPegsWhite = new Group();

		HBox top = new HBox();
		button = new Button("Ready");



		labelP = new Label();
		labelP.setText("Place your ships. Press 'r' to rotate your ship.");

		top.getChildren().addAll(button, labelP);

		root.getChildren().addAll(top, layerGame);

		layerGame.getChildren().addAll(layerGrid, layerShips, layerShipsInteractive, layerPegsRed,layerPegsWhite);

		this.layerGame = layerGame;
		this.layerGrid = layerGrid;
		this.layerShips = layerShips;
		// For placing ships
		Group layerShipsInteractive1 = layerShipsInteractive;
		this.layerPegsRed = layerPegsRed;
		this.layerPegsWhite = layerPegsWhite;
		
		this.shipRectInteractive = this.initShipRect();
		layerShipsInteractive1.getChildren().add(this.shipRectInteractive);
		this.shipRectInteractive.setOpacity(0);
		
		Scene theScene = new Scene(root, 800, 800);
		stage.setScene(theScene);
		
        System.out.println(new java.io.File("").getAbsolutePath());

        File f = new File("style.css");

        theScene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

		this.stage.setOnShown(e -> this.tryInitBoard());
		this.stage.show();
		
		// Routing key events to game
		// this uses lambda, dont worry about anything
		// e -> ... is OK!
		theScene.setOnKeyPressed(e -> this.game.onKeyPressed(e));
		theScene.setOnKeyReleased(e -> this.game.onKeyReleased(e));
		theScene.setOnKeyTyped(e -> this.game.onKeyTyped(e));
	}
	public Button getButton(){
		return button;
	}

	public void setLabelText(String text) {
		labelP.setText(text);
	}
	private void tryInitBoard() {
		if(this.numRows == -1) return;
		
		this.initBoard();
	}
	
	private int numRows = -1;
	private int numCols = -1;
	
	public void init(int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
		
		this.tryInitBoard();
	}

	private void initBoard() {
		int numVCells = numRows + 1;
		int numHCells = numCols + 1;

		this.gridCellControllers = new GridCellController[numCols][numRows];

		double wTotal = this.layerGame.getPrefWidth();
		double hTotal = this.layerGame.getPrefHeight();

		// Inner / outer aspect ratios can be used to get a better "fit" of the board,
//		double aspectRatioInner = numHCells / numVCells;
		double aspectRatioOuter = (wTotal  / hTotal);

		double offsetX, offsetY;
		double gridWidth, gridHeight;

		// Simple fit, could be optimized
		// further if aspect ratios "match"
		// but now it only handles 2 cases
		if (aspectRatioOuter < 0) {
			// Fit to width
			offsetX = 0;
			gridWidth = wTotal;
			gridHeight = gridWidth / aspectRatioOuter;
			offsetY = (hTotal - gridHeight) / 2;
		} else {
			// Fit to height
			offsetY = 0;
			gridHeight = hTotal;
			gridWidth = gridHeight * aspectRatioOuter;
			offsetX = (wTotal - gridWidth) / 2;
		}

		double cellSize = gridWidth / (numHCells + 1);
		assert cellSize == gridHeight / numVCells;

		// Init column labels
		for (int col = 1; col < numHCells; col++) {
			double x = offsetX + col * cellSize;
			double y = offsetY;
			this.initLabel((x), y, cellSize, Integer.toString(col));
		}

		// Init row labels
		String[] rowLabels = UIOptions.ROW_LABELS;
		for (int row = 1; row < numVCells; row++) {
			double x = offsetX;
			double y = offsetY + row * cellSize;
			this.initLabel(x, y, cellSize, rowLabels[row - 1]);
		}

		// Init cells (grid cell + pegs)
		for (int col = 1; col < numHCells; col++) {
			for (int row = 1; row < numVCells; row++) {
				double x = offsetX + col * cellSize;
				double y = offsetY + row * cellSize;
				this.initCell(row - 1, col - 1, x, y, cellSize);
//				this.initLabel(x, y, cellSize, "t");
			}
		}
	}

	private void initLabel(double x, double y, double cellSize, String text) {
		Rectangle background = new Rectangle();
		background.setWidth(cellSize);
		background.setHeight(cellSize);
		background.relocate(x, y);
		
		Image image = new Image("images//sharks.png");
		ImagePattern imagePattern = new ImagePattern(image);
		background.setFill(imagePattern);

	
		DropShadow shadow = new DropShadow();
		shadow.setWidth(1);
		shadow.setHeight(1);
		shadow.setOffsetX(1);
		shadow.setOffsetY(1);
		shadow.setColor(Color.BURLYWOOD);

		InnerShadow is = new InnerShadow();
		is.setWidth(2);
		is.setHeight(2);
		is.setOffsetX(2);
		is.setOffsetY(2);
		is.setRadius(10);
		is.setInput(shadow);
		background.setEffect(is);

		background.setArcHeight(44);
		background.setArcWidth(44);
		
		background.setOpacity(0.6);
		
		Label l = new Label();
		l.resizeRelocate(x, y, cellSize, cellSize);
		l.setText(text);
		l.setTextFill(Paint.valueOf("white"));

		l.setAlignment(Pos.CENTER);
		l.setPrefSize(cellSize, cellSize);

		l.setFont(new Font(UIOptions.LABEL_FONT_SIZE));

		// No fucking mouse events for this guy, k
		l.setMouseTransparent(true);
		this.layerGrid.getChildren().addAll(background, l);
	}
	
	private void initCell(int row, int col, double x, double y, double cellSize) {
		Rectangle gridCell = new Rectangle(x, y, cellSize, cellSize);

		gridCell.setOnMouseEntered(e -> {
			gridCell.setScaleX(UIOptions.PEG_SCALE_HOVER);
			gridCell.setScaleY(UIOptions.PEG_SCALE_HOVER);
			this.onMouseEnter(col, row, e);
			
		});
		gridCell.setOnMouseExited(e -> {
			gridCell.setScaleX(UIOptions.PEG_SCALE);
			gridCell.setScaleY(UIOptions.PEG_SCALE);
			this.onMouseLeave(col, row, e);
		});
		gridCell.setOnMouseClicked(e -> this.onMouseClick(col, row, e));

		gridCell.setScaleX(UIOptions.PEG_SCALE);
		gridCell.setScaleY(UIOptions.PEG_SCALE);

		gridCell.setFill(Paint.valueOf("lightgray"));

		DropShadow shadow = new DropShadow();
		shadow.setWidth(1);
		shadow.setHeight(1);
		shadow.setOffsetX(1);
		shadow.setOffsetY(1);
		shadow.setColor(Color.BURLYWOOD);

		InnerShadow is = new InnerShadow();
		is.setWidth(2);
		is.setHeight(2);
		is.setOffsetX(2);
		is.setOffsetY(2);
		is.setRadius(10);
		is.setInput(shadow);
		gridCell.setEffect(is);

		gridCell.setArcHeight(10);
		gridCell.setArcWidth(10);

		double scaleR = 0.5 * (1.0 - UIOptions.PEG_SCALE);

		Circle pegWhite = new Circle();
		pegWhite.setRadius(cellSize * (0.5 - scaleR));
		pegWhite.resizeRelocate(x + cellSize * scaleR, y + cellSize * scaleR, cellSize, cellSize);
		pegWhite.setFill(Color.WHITE);

		Circle pegRed = new Circle();
		pegRed.setRadius(cellSize * (0.5 - scaleR));
		pegRed.resizeRelocate(x + cellSize * scaleR, y + cellSize * scaleR, cellSize, cellSize);
		pegRed.setFill(Color.CRIMSON);

		pegWhite.scaleXProperty().bind(gridCell.scaleXProperty());
		pegWhite.scaleYProperty().bind(gridCell.scaleYProperty());

		pegRed.scaleXProperty().bind(gridCell.scaleXProperty());
		pegRed.scaleYProperty().bind(gridCell.scaleYProperty());

		// Mouse should only be tracked by the actual cells lol
		pegWhite.setMouseTransparent(true);
		pegRed.setMouseTransparent(true);

		pegWhite.setEffect(is);
		pegRed.setEffect(is);

		this.layerGrid.getChildren().add(gridCell);
		this.layerPegsWhite.getChildren().add(pegWhite);
		this.layerPegsRed.getChildren().add(pegRed);

		// Hide pegs
		pegWhite.setOpacity(0);
		pegRed.setOpacity(0);

		this.setController(col, row, gridCell, pegWhite, pegRed);
	}

	private void setController(int x, int y, Rectangle gridCell, Circle pegWhite, Circle pegRed) {
		this.gridCellControllers[x][y] = new GridCellController(gridCell, pegWhite, pegRed);
	}

	private GridCellController getCellController(int x, int y) {
		return this.gridCellControllers[x][y];
	}

	public void setCellState(int x, int y, GridCellStates state) {
		this.getCellController(x, y).setState(state);
	}

	private Rectangle getCellRect(int x, int y) {
		return this.getCellController(x, y).getGridCellRect();
	}
	
	private void placeShipRect(int x0, int y0, int x1, int y1, Rectangle rect) {
		Rectangle start = this.getCellRect(x0, y0), end = this.getCellRect(x1, y1);

		double inset = UIOptions.SHIP_INSET;

		double sx0 = start.getX() + inset;
		double sx1 = end.getX() + end.getWidth() - inset;
		double sy0 = start.getY() + inset;
		double sy1 = end.getY() + end.getHeight() - inset;
		
		rect.setWidth(sx1 - sx0);
		rect.setHeight(sy1 - sy0);
		rect.setX(sx0);
		rect.setY(sy0);
	}
	
	public void setGame(IUIGame game) {
		this.game = game;
	}
	
	private void onMouseClick(int col, int row, MouseEvent e) {
		this.game.onMouseClick(col, row, e);
	}
	
	private void onMouseEnter(int col, int row, MouseEvent e) {
		this.game.onMouseEnter(col, row, e);
	}
	
	private void onMouseLeave(int col, int row, MouseEvent e) {

		this.game.onMouseLeave(col, row, e);
	}

	@Override
	public void placeShipInteractive(int x0, int y0, int x1, int y1) {
		this.shipRectInteractive.setOpacity(1);
		this.placeShipRect(x0, y0, x1, y1, this.shipRectInteractive);
	}

	@Override
	public void placeShipFinal(int x0, int y0, int x1, int y1) {
		this.hideShipInteractive();
		
		Rectangle shipRect = this.initShipRect();
		this.placeShipRect(x0, y0, x1, y1, shipRect);
		this.layerShips.getChildren().add(shipRect);
	}

	@Override
	public void hideShipInteractive() {
		this.shipRectInteractive.setOpacity(0);
	}

	@Override
	public void clearShips() {
		this.layerShips.getChildren().clear();
	}
	
	private Rectangle initShipRect() {
		Rectangle shipRect = new Rectangle();
		
		shipRect.setStroke(Paint.valueOf(UIOptions.SHIP_STROKE));
		shipRect.setStrokeWidth(2);
		shipRect.setStrokeType(StrokeType.INSIDE);
		
		DropShadow shadow = new DropShadow();
		shadow.setWidth(1);
		shadow.setHeight(1);
		shadow.setOffsetX(1);
		shadow.setOffsetY(1);
		shadow.setColor(Color.SILVER);

		InnerShadow is = new InnerShadow();
		is.setWidth(2);
		is.setHeight(2);
		is.setOffsetX(2);
		is.setOffsetY(2);
		is.setRadius(10);
		is.setInput(shadow);
		shipRect.setEffect(is);

		shipRect.setArcHeight(UIOptions.SHIP_ROUNDING);
		shipRect.setArcWidth(UIOptions.SHIP_ROUNDING);
		
		shipRect.setFill(Paint.valueOf(UIOptions.SHIP_FILL));
		shipRect.setMouseTransparent(true);
		return shipRect;
	}




}
