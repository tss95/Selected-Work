package inf101.v19.sem2.ship;

import inf101.v19.sem2.examplegame.Game;
import inf101.v19.sem2.interfaces.IShip;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Submarine implements IShip {

    private Game game;

    private int length = 3;

    private int x0;
    private int x1;
    private int y0;
    private int y1;

    private String boatDirection = "Vertical";

    private boolean isDestroyed = false;
    private boolean initialized = false;
    private boolean isPlaced = false;

    private List<List<Integer>> gridCovered = new ArrayList<>();

    public void gridCovered(){
        gridCovered.clear();
        if (getBoatDirection() == "Vertical") {
            for (int i = 0; i < getLength(); i++) {
                ArrayList<Integer> cell = new ArrayList<>();
                cell.add(x0);
                cell.add(y0+i);
                gridCovered.add(cell);
            }
        }
        if (getBoatDirection() == "Horizontal") {
            for (int i = 0; i < getLength(); i++) {
                ArrayList<Integer> cell = new ArrayList<>();
                cell.add(x0+i);
                cell.add(y0);
                gridCovered.add(cell);
            }
        }
    }

    public List getGridCovered(){return gridCovered;}

    public void initShip(int x0, int y0) {
        setX0(x0);
        setY0(y0);
        setX1(x0);
        setY1(y0 + getLengthLessOne());
        setBoatDirection("Vertical");
        gridCovered();
        setInitialized(true);
    }

    @Override
    public void setInitialized(boolean initialized) {this.initialized = initialized;}
    public boolean getInitialized() {return initialized;}

    @Override
    public void setBoatDirection(String dir) {this.boatDirection = dir;}
    public String getBoatDirection(){return boatDirection;}

    @Override
    public void setIsPlaced(boolean isPlaced) {this.isPlaced = isPlaced;}
    public boolean isPlaced(){return isPlaced;}

    public int getHealth() {
        int health = 3;
        return health;}

    public String getShipName() {
        String shipName = "Submarine";
        return shipName;}

    public void DestroyShip() {this.isDestroyed = true;}
    public boolean isDestroyed() {return isDestroyed;}

    public int getLengthLessOne() {return length - 1;}
    public int getLength() {return length;}

    public int getX0() {return x0;}
    public int getX1() {return x1;}
    public int getY0() {return y0;}
    public int getY1() {return y1;}

    public void setX0(int x0){this.x0 = x0;}
    public void setX1(int x1){this.x1 = x1;}
    public void setY0(int y0){this.y0 = y0;}
    public void setY1(int y1){this.y1 = y1;}

    public void rotateShip() {
        if (boatDirection == "Vertical") {
            setX1(getX1() + getLengthLessOne());
            setY1(getY1() - getLengthLessOne());
            boatDirection = "Horizontal";
            gridCovered();
            return;
        }
        if (boatDirection == "Horizontal") {
            setX1(getX1() - getLengthLessOne());
            setY1(getY1() + getLengthLessOne());
            boatDirection = "Vertical";
            gridCovered();
            return;
        }
    }

    @Override
    public Iterator<IShip> iterator() {
        return null;
    }
}
