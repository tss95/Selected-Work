package inf101.v19.sem2.interfaces;

import inf101.v19.sem2.examplegame.Game;

import java.util.List;

public interface IPc extends IActor {

    /**
     * Assigns the AI ships their own places on the grid and stores that information in the ship classes.
     */
    void placeShips();

    /**
     * Fires a shot on the grid. If the AI has not recently hit a ship or has recently sunk a ship
     * then it will shoot on the grid randomly. It does not fire on the entire grid however, only every second
     * cell. In that way it requires a lot less shots in order to find all the ships as each ship is at least two
     * cells long.
     * If there is a recent shot however, then the AI shoots at a cell determined by findNextGoodMove().
     * There are more comments in the method for more information.
     */
    void fire();

    // Gets whether or not the AI has recently hit something.
    boolean getRecentlyHit();

    // Sets recently hit
    void setRecentlyHit(boolean recentlyHit);

    /**
     * Initiates the AI class.
     * @param name of the AI
     * @param game class
     * @param player class
     */
    void init(String name, Game game, IActor player);

    // Sets shots since last hit.
    void setShotsSinceLastHit(int shotsSinceLastHit);

    // Gets nr of shots since last hit.
    int getShotsSinceLastHit();

    /**
     * Sinks the ship given the AI's hit streak.
     * @param hitStreak amount of hits on the current sequence of shots.
     */
    void sinkByLength(int hitStreak);

    // Sets the hit streak.
    void setHitStreak(int hitStreak);

    // Gets the hit streak
    int getHitStreak();

    // Returns the length of the longest ship alive.
    int longestShipAlive();

    /**
     * Primarily used for testing, this determines if the shot from the player is a hit or a miss.
     * @param gridX x coordinate
     * @param gridY y coordinate
     * @return true if the shot is a hit, false otherwise.
     */
    boolean hitOrMiss(int gridX, int gridY);

    /**
     * This uses information given by player.handleDamage(int x, int y) to determine information about the current
     * sequence of attacks. It handles the information, such as if the AI has found an end, if the AI has sunk a ship,
     * hit patterns, etc., so that findNextGoodMove() can make an good decision.
     * There are a lot of comments on this method in the Computer class.
     */
    void infoHandler();

    /**
     * Uses the information gained from infoHandler() to determine a good next move.
     * @return a tuple describing a place on the grid for the next shot.
     */
    List findNextGoodMove();

    /**
     * Resets the information that infoHandler() and player.handleDamage(int x, int y) has found after a ship has been sunk.
     */
    void sinkShip();

    // Returns the current ship class.
    IShip getCurrentShip();

    /**
     * Displays information about the AI in the console. Used for debugging.
     */
    void status();
}
