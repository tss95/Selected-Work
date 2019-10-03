package inf101.v19.sem2.examplegame;

import inf101.v19.sem2.actors.Computer;
import inf101.v19.sem2.actors.Player;
import inf101.v19.sem2.interfaces.*;
import inf101.v19.sem2.ship.*;
import inf101.v19.sem2.ui.GridCellStates;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

// See IUIGame for Javadoc
public class Game implements IUIGame {


    public IUserInterface ui;
    private IPlayer player;
    private IPc pc;

    private int numRows;
    private int numCols;
    private int shipNumber = 0;

    private Carrier carrier;
    private Battleship battleship;
    private Cruiser cruiser;
    private Submarine submarine;
    private Destroyer destroyer;

    private Carrier pc_carrier;
    private Battleship pc_battleship;
    private Cruiser pc_cruiser;
    private Submarine pc_submarine;
    private Destroyer pc_destroyer;


    private List<List> playerShipCoverage = new ArrayList<>();
    private List<List> pcShipCoverage = new ArrayList<>();

    private int gamePhase = 0;

    public List getPlayerShipCoverage(){return playerShipCoverage;}

    public void setPlayerShipCoverage() {
        List<List<Integer>> temp = new ArrayList<>(ShipNr().getGridCovered());
        playerShipCoverage.addAll(temp);
    }

    public List getPcShipCoverage(){return pcShipCoverage;}

    public void setPcShipCoverage() {
        List<List<Integer>> temp = new ArrayList<>(ShipNr().getGridCovered());
        pcShipCoverage.addAll(temp);
    }

    public Game(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.carrier = new Carrier();
        this.battleship = new Battleship();
        this.cruiser = new Cruiser();
        this.submarine = new Submarine();
        this.destroyer = new Destroyer();
        this.pc_carrier = new Carrier();
        this.pc_battleship = new Battleship();
        this.pc_cruiser = new Cruiser();
        this.pc_submarine = new Submarine();
        this.pc_destroyer = new Destroyer();
        this.player = new Player();
        this.pc = new Computer();
        player.initiate("Tord", this, pc);
        pc.init("pc", this, player);
    }

    public int getNumRows(){return numRows;}

    public int getNumCols(){return numCols;}

    @Override
    public void setUI(IUserInterface ui) {
        this.ui = ui;

        ui.setGame(this);
        ui.init(this.numRows, this.numCols);
        createButton();
    }

    @Override
    public void onMouseClick(int gridX, int gridY, MouseEvent e) {
        System.out.println(e);
        if (gamePhase == 0) {
            if (shipNumber < 5) {
                if (canPlace()) {
                    placePlayerShipUI(gridX, gridY);
                    placeAShip();
                }
            }
        }
        if (gamePhase == 2) {
            if (!player.getHasFired()){
                System.out.println("You attacked cell: (" + gridX + ", " + gridY + ")");
                shootAt(gridX, gridY);
            }
        }
    }

    public void placePlayerShipUI(int gridX, int gridY) {
        this.ui.placeShipFinal(gridX, gridY, ShipNr().getX1(), ShipNr().getY1());
    }

    public void placeAShip() {
        ShipNr().setIsPlaced(true);
        ShipNr().gridCovered();
        if (gamePhase == 0) {
            setPlayerShipCoverage();
        }
        if (gamePhase == 1) {
            setPcShipCoverage();
        }
        shipNumber++;
    }

    public void shootAt(int x, int y) {
        pc.handleDamage(x, y);
        player.setHasFired(true);
        placePins();
    }

    @Override
    public void onMouseEnter(int gridX, int gridY, MouseEvent e) {
        if (gamePhase == 0) {
            if (shipNumber < 5) {
                if (!ShipNr().getInitialized()) {
                    ShipNr().initShip(gridX, gridY);
                    ShipNr().gridCovered();
                    this.ui.placeShipInteractive(gridX, gridY, ShipNr().getX1(), ShipNr().getY1());
                } else {
                    updateShip(gridX, gridY);
                    ShipNr().gridCovered();
                    this.ui.placeShipInteractive(gridX, gridY, ShipNr().getX1(), ShipNr().getY1());
                }
            }
        }
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        String key = e.getText();
        key.trim().toLowerCase();
        int nr = key.hashCode();
        System.out.println(nr);
        if (gamePhase == 0) {
            if (shipNumber < 5) {
                if (nr == 114) {
                    ShipNr().rotateShip();
                    this.ui.placeShipInteractive(ShipNr().getX0(), ShipNr().getY0(), ShipNr().getX1(), ShipNr().getY1());
                    return;

                }
            }
        }
    }


    @Override
    public void onKeyReleased(KeyEvent e) {}

    @Override
    public void onKeyTyped(KeyEvent e) {}

    @Override
    public void onMouseLeave(int gridX, int gridY, MouseEvent e) {
    }

    public void updateShip(int x, int y) {
        int diffX = x - ShipNr().getX0();
        int diffY = y - ShipNr().getY0();
        ShipNr().setX0(ShipNr().getX0() + diffX);
        ShipNr().setX1(ShipNr().getX1() + diffX);
        ShipNr().setY0(ShipNr().getY0() + diffY);
        ShipNr().setY1(ShipNr().getY1() + diffY);
        return;
    }

    public IShip ShipNr() {
        if (getGamePhase() == 0 || getGamePhase() == 2 || getGamePhase() == 5) {
            switch (shipNumber) {
                case 0:
                    return carrier;
                case 1:
                    return battleship;
                case 2:
                    return cruiser;
                case 3:
                    return submarine;
                case 4:
                    return destroyer;
                default:
                    throw new IndexOutOfBoundsException("ShipNr too big");
            }
        }
        if (getGamePhase() == 1 || getGamePhase() == 3)  {
            switch (shipNumber) {
                case 0:
                    return pc_carrier;
                case 1:
                    return pc_battleship;
                case 2:
                    return pc_cruiser;
                case 3:
                    return pc_submarine;
                case 4:
                    return pc_destroyer;
                default:
                    throw new IndexOutOfBoundsException("ShipNr too big");
            }
        }
        throw new IndexOutOfBoundsException("ShipNr() should not be called at this time");
    }

    public boolean canPlace() {
        List oneShipCover = ShipNr().getGridCovered();
        if (gamePhase == 0) {
            for (int x = 0; x < oneShipCover.size(); x++) {
                if (playerShipCoverage.contains(oneShipCover.get(x))) {
                    System.out.println("Cant place ship here");
                    return false;
                }
            }
        }
        else {
            for (int x = 0; x < oneShipCover.size(); x++) {
                if (pcShipCoverage.contains(oneShipCover.get(x))) {
                    return false;
                }
            }
        }
        return true;
    }


    public void setGamePhase(int gamePhase) {
        this.gamePhase = gamePhase;
    }

    public int getGamePhase() {
        return gamePhase;
    }
    public boolean areAllPlaced(){
        return carrier.isPlaced() && battleship.isPlaced() && submarine.isPlaced() && cruiser.isPlaced() && destroyer.isPlaced();
    }
    public String getGamePhaseName() {
        switch (getGamePhase()) {
            case 0:
                return "player ship placement";
            case 1:
                return "computer ship placement";
            case 2:
                return "player attacks computer";
            case 3:
                return "computer attacks computer";
            case 5:
                return "Testing computer AI";
            default:
                throw new IndexOutOfBoundsException("Game phase error");
        }
    }
    public void setShipNumber(int shipNumber){this.shipNumber = shipNumber;}

    public int getShipNumber(){return shipNumber;}

    public void placePins() {
        if (gamePhase == 2) {
            for (int i = 0; i < player.getShotsFired_misses().size(); i++) {
                int x = (int) ((List) player.getShotsFired_misses().get(i)).get(0);
                int y = (int) ((List) player.getShotsFired_misses().get(i)).get(1);
                this.ui.setCellState(x, y, GridCellStates.PEG_WHITE);
            }
            for (int i = 0; i < player.getShotsFired_hits().size(); i++) {
                int x = (int) ((List) player.getShotsFired_hits().get(i)).get(0);
                int y = (int) ((List) player.getShotsFired_hits().get(i)).get(1);
                this.ui.setCellState(x, y, GridCellStates.PEG_RED);
            }
        }
        if (gamePhase == 3 || gamePhase == 5) {
            for (int i = 0; i < pc.getShotsFired_misses().size(); i++) {
                int x = (int) ((List) pc.getShotsFired_misses().get(i)).get(0);
                int y = (int) ((List) pc.getShotsFired_misses().get(i)).get(1);
                this.ui.setCellState(x, y, GridCellStates.PEG_WHITE);
            }
            for (int i = 0; i < pc.getShotsFired_hits().size(); i++) {
                int x = (int) ((List) pc.getShotsFired_hits().get(i)).get(0);
                int y = (int) ((List) pc.getShotsFired_hits().get(i)).get(1);
                this.ui.setCellState(x, y, GridCellStates.PEG_RED);
            }
        }
    }

    public void clearPins() {
        for (int x = 0; x < numCols; x++) {
            for (int y = 0; y < numRows; y++) {
                this.ui.setCellState(x,y,GridCellStates.CLEAR);
            }
        }
    }

    public void placeShips(){
        setShipNumber(0);
        while (getShipNumber() < 5) {
            this.ui.placeShipFinal(ShipNr().getX0(), ShipNr().getY0(), ShipNr().getX1(), ShipNr().getY1());
            setShipNumber(getShipNumber() + 1);
        }
    }

    public boolean checkWinCondition() {
        if (player.getShotsFired_hits().size() == pc.getShipCoverage().size()) {
            player.setHasWon(true);
            winner();
            return true;
        }
        if (pc.getShotsFired_hits().size() == player.getShipCoverage().size()) {
            pc.setHasWon(true);
            winner();
            return true;
        } else {
            return false;
        }
    }


    public void createButton() {
        ui.getButton().setOnAction(e -> {
            if (getGamePhase() == 0) {
                if (areAllPlaced()) {
                    setGamePhase(1);
                    ui.setLabelText("Opponent has placed the ships. Press 'ready' again to begin attack phase.");
                    System.out.println(getGamePhaseName());
                    this.ui.clearShips();
                    setShipNumber(0);
                    System.out.println("From game: " + getShipNumber());
                    pc.placeShips();
                    this.ui.clearShips();
                    return;
                } else {
                    System.out.println("Nothing happened");
                }
            }

            if (gamePhase == 1 || gamePhase == 3) {
                this.ui.clearShips();
                //Set to 5 if you want to test the AI. 2 otherwise
                setGamePhase(2);
                player.setHasFired(false);
                ui.setLabelText("Select a cell to attack opponent, then press ready");
                clearPins();
                placePins();
                player.beginAttackPhase();
                pc.beginAttackPhase();
                checkWinCondition();
                return;
            }
            if (gamePhase == 2) {
                pc.setHasFired(false);
                this.ui.clearShips();
                clearPins();
                placeShips();
                setGamePhase(3);
                ui.setLabelText("The computer attacked you, press ready to continue.");
                placePins();
                pc.fire();
                checkWinCondition();
                return;
            }
            // Used for testing the AI
            if (gamePhase == 5) {
                pc.setHasFired(false);
                this.ui.clearShips();
                clearPins();
                placeShips();
                setGamePhase(5);
                ui.setLabelText("The computer is attacking you");
                placePins();
                pc.fire();
                checkWinCondition();
                return;
            }
        });
    }
    public void winner(){
        if (player.getHasWon()){
            placePins();
            setGamePhase(3);
            placeShips();
            setGamePhase(4);
            ui.setLabelText("You have won!!!");
        }
        if (pc.getHasWon()){
            placePins();
            setGamePhase(2);
            placeShips();
            setGamePhase(4);
            ui.setLabelText("The computer has sunk your last ship. Long live the AI overlords. You lost.");
        }
    }

    public IPc getPc() {return pc;}

}