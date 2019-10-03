package inf101.v19.sem2.interfaces;

import inf101.v19.sem2.ui.GridCellStates;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Class handeling the main phases of the game as well as placement of ships and pins.
 */
public interface IUIGame {

	/**
	 * Called whenever a grid cell is clicked
	 *
	 * @param gridX x-coordinate on the grid (starting at 0)
	 * @param gridY y-coordinate on the grid (starting at 0)
	 * @param e     the mouse event
	 */
	void onMouseClick(int gridX, int gridY, MouseEvent e);

	/**
	 * Called whenever the mouse enters a grid cell
	 *
	 * @param gridX x-coordinate on the grid (starting at 0)
	 * @param gridY y-coordinate on the grid (starting at 0)
	 * @param e     the mouse event
	 */
	void onMouseEnter(int gridX, int gridY, MouseEvent e);


	/**
	 * Called whenever the mouse leaves a grid cell
	 * @param gridX x-coordinate on the grid (starting at 0)
	 * @param gridY y-coordinate on the grid (starting at 0)
	 * @param e the mouse event
	 */
	//void onMouseLeave(int gridX, int gridY, MouseEvent e);


	/**
	 * Called whenever a key is pressed DOWN. Protip: use .getText() to get the char.
	 *
	 * @param e
	 */
	void onKeyPressed(KeyEvent e);

	/**
	 * Called whenever the mouse leaves a grid cell
	 * @param gridX x-coordinate on the grid (starting at 0)
	 * @param gridY y-coordinate on the grid (starting at 0)
	 * @param e the mouse event
	 */
	void onMouseLeave(int gridX, int gridY, MouseEvent e);

	/**
	 * Called whenever a key is RELEASED. Protip: use .getText() to get the char.
	 * @param e
	 */
	void onKeyReleased(KeyEvent e);

	/**
	 * Called whenever a key is typed (i.e pressed AND released). Protip: use .getCharacter() to see which char
	 * was typed.
	 * @param e
	 */
	void onKeyTyped(KeyEvent e);

	/**
	 * Sets the UI of the game, the game should then initialize the UI board etc
	 * @param ui
	 */
	void setUI(IUserInterface ui);

	// Returns the list of coordinates that the players' ships cover.
	List getPlayerShipCoverage();

	// Sets the list of player ship coverage
	void setPlayerShipCoverage();

	// Return the list of coordinates that the PCs' ships cover
	List getPcShipCoverage();

	// Sets the list of coordiantes that the PC's ships cover
	void setPcShipCoverage();

	/**
	 * Handles the UI side of placing a ship
	 * @param gridX x coorinate
	 * @param gridY y coordinate
	 */
	void placePlayerShipUI(int gridX, int gridY);

	/**
	 * Registers the placement of a ship in the ships respective class.
	 */
	void placeAShip();

	/**
	 * Handles a shot from a player onto a place on the grid
	 * @param x coordinate
	 * @param y coordinate
	 */
	void shootAt(int x, int y);

	/**
	 * Updates the ships position when initiated but not yet placed.
	 * @param x coorinate
	 * @param y coordinate
	 */
	void updateShip(int x, int y);

	/**
	 * Iterates through the ship classes and returns the current one.
	 * @return
	 */
	IShip ShipNr();

	/**
	 * Determines if the ship can be placed at the current location and direction.
	 * @return
	 */
	boolean canPlace();

	// Setter for the game phase.
	void setGamePhase(int gamePhase);

	// Getter for the game phase
	int getGamePhase();

	/**
	 * Determines if all the ships are placed in the phase.
	 * @return true if they are all placed, false otherwise.
	 */
	boolean areAllPlaced();

	// Returns the game phase name.
	String getGamePhaseName();

	// Sets the ship number
	void setShipNumber(int shipNumber);

	// Gets the ship number
	int getShipNumber();

	/**
	 * Places all the relevant pins on the UI depending on the game phase.
	 */
	void placePins();

	/**
	 * Clears all the pins off the UI.
	 */
	void clearPins();

	/**
	 * Places all the ships on the UI depending on the game phase
	 */
	void placeShips();

	/**
	 * Checks if any of the actors have won the game
	 * @return true of someone has won, false otherwise
	 */
	boolean checkWinCondition();

	/**
	 * Assigns functionality to the button on the UI depending on the game phase.
	 */
	void createButton();

	/**
	 * Extension of the checkWinCondition(). This method does the 'win' sequence.
	 */
	void winner();

	// Returns the Computer class.
	IPc getPc();




}
