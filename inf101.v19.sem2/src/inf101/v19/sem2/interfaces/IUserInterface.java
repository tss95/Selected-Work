package inf101.v19.sem2.interfaces;

import inf101.v19.sem2.ui.GridCellStates;
import javafx.scene.control.Button;

/**
 * Interface for the UI. Structured so that it can be called
 * "from the outside", typically not a good pattern BUT
 * in this case students will be able to use it "from" their
 * own logic.
 * @author yngve
 *
 */
public interface IUserInterface {
			
	/**
	 * Initializes the interface to fit a board of the given size
	 * @param numRows - number of rows on the board
	 * @param numCols - number of columns on the board
	 */
	void init(int numRows, int numCols);
	
	/**
	 * Clears away all ships (but not the interactive ship)
	 */
	void clearShips();
	
	/**
	 * Hides the interactive ship
	 */
	void hideShipInteractive();
	
	/**
	 * Places the "interactive" ship from one grid position (x0, y0) to another (x1, y1).
	 * This should be used for interactively placing the ships.
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 */
	void placeShipInteractive(int x0, int y0, int x1, int y1);
	
	/**
	 * Places a ship from one grid position (x0, y0), to another (x1, y1).
	 * NOTE: This method does not validate the position whatsoever, this is 
	 * up to the game implementer to do.
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 */
	void placeShipFinal(int x0, int y0, int x1, int y1);

	/**
	 * Sets the state of a cell, given 0-indexed grid coordinates
	 * @param x
	 * @param y
	 * @param state
	 */
	void setCellState(int x, int y, GridCellStates state);

	/**
	 * Sets the game of the UI. This is necessary for mouse and keyboard events to be routed
	 * and handled in the game implementation.
	 * @param game
	 */
	void setGame(IUIGame game);
	
	/**
	 * Writes some text to the status label of the interface.
	 * @param text - the text to display
	 */
	void writeStatus(String text);

	Button getButton();

	void setLabelText(String text);
}
