package inf101.v19.sem2.interfaces;

import java.util.ArrayList;
import java.util.List;

public interface IShip extends Iterable<IShip>{

    /**
     * Initializes the ship
     * @param x0 x coordinate of the uppermost or leftmost part of the ship.
     * @param y0 y coordinate of the uppermost or leftmost part of the ship.
     */
    void initShip(int x0, int y0);

    // Returns the name of the ship
    String getShipName();

    // Returns true if the ship is placed, false otherwise
    boolean isPlaced();

    // Returns the length of the ship minus one.
    int getLengthLessOne();

    // Returns the length of the ship.
    int getLength();

    // Getters for the top and bottom of the ship.
    int getX1();
    int getY1();
    int getX0();
    int getY0();

    // Sets isPlaced.
    void setIsPlaced(boolean isPlaced);

    /**
     * Rotates the ship to horinzontal or vertical, depending on its previous orientation.
     */
    void rotateShip();

    /**
     * Updates the grid coverage of the ship and assigns this information to the gridCovered list in the class.
     */
    void gridCovered();

    // Gets the gridCovered list.
    List getGridCovered();

    // Returns true if the ship is initialized, false ow.
    boolean getInitialized();
    // Sets initialized.
    void setInitialized(boolean initialized);

    // Returns the orientation of the ship in string type.
    String getBoatDirection();

    // Sets the orientation of the boat.
    void setBoatDirection(String dir);

    // Setters of the coordinates of the top and bottom of the ship
    void setX0(int x0);
    void setX1(int x1);
    void setY0(int y0);
    void setY1(int y1);
}
