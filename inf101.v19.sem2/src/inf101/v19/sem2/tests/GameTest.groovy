package inf101.v19.sem2.tests

import com.sun.glass.events.KeyEvent
import inf101.v19.sem2.examplegame.Game
import inf101.v19.sem2.interfaces.IUIGame
import inf101.v19.sem2.interfaces.IUserInterface
import inf101.v19.sem2.ui.UI
import javafx.stage.Stage
import org.junit.jupiter.api.Test

import java.security.Key

class GameTest  {

    // Tests if a player ship is placed correctly
    @Test
    void placeAPlayerShip() {
        IUIGame game = new Game(12, 12)
        game.ShipNr().initShip(0,0)
        game.placeAShip()
        assert(0 < game.getPlayerShipCoverage().size())
    }

    @Test
    // Tests if a shot on an empty cell is a miss
    void FireTestMiss() {
        IUIGame game = new Game(12, 12)
        assert(!game.getPc().hitOrMiss(0,0))
    }

    @Test
    // Tests if a shot on an occupied cell is a hit
    void FireTestHit() {
        IUIGame game = new Game(12, 12)
        game.setGamePhase(1)
        game.ShipNr().initShip(0,0)
        game.placeAShip()
        game.getPc().beginAttackPhase()
        assert(game.getPc().hitOrMiss(0,0))
    }

    @Test
    // Tests if you can place a ship at an occupied cell. Then tests if you place a ship on an unoccupied cell.
    void IllegalPlacementTest() {
        IUIGame game = new Game(12, 12)
        game.ShipNr().initShip(0,0)
        game.placeAShip()
        game.ShipNr().initShip(0,0)
        assert(!game.canPlace())
        game.ShipNr().initShip(1,0)
        assert(game.canPlace())
    }

    @Test
    // Tests if the rotation of a ship is handled correctly
    void RotateShipTest() {
        IUIGame game = new Game(12, 12)
        game.ShipNr().initShip(0,0)
        game.ShipNr().rotateShip()
        game.placeAShip()
        game.setShipNumber(0)
        assert(game.ShipNr().getY1() == 0 && game.ShipNr().getX1() != 0)
    }
    @Test
    // Tests if two rotations of a ship returns it to the original position
    void RotateShipTwiceTest() {
        IUIGame game = new Game(12, 12)
        game.ShipNr().initShip(0,0)
        game.ShipNr().rotateShip()
        game.ShipNr().rotateShip()
        game.placeAShip()
        game.setShipNumber(0)
        assert(game.ShipNr().getY1() != 0 && game.ShipNr().getX1() == 0)
    }

    @Test
    // Tests if given no information the AI does not make any conclusions. Recently hit is the first and most significant
    // boolean value for the AI.
    void AINoInfoTest() {
        IUIGame game = new Game(12, 12)
        game.ShipNr().initShip(0,0)
        game.placeAShip()
        game.getPc().infoHandler()
        assert(!game.getPc().getRecentlyHit())
    }

    @Test
    // Tests the update method in game. This method handles translating the coordinates of the ship when it is moved.
    void updateShipTest() {
        IUIGame game = new Game(12, 12)
        game.ShipNr().initShip(0,0)
        game.updateShip(0,1)
        game.placeAShip()
        game.setShipNumber(0)
        assert(game.ShipNr().getY0() == 1)
    }
}
