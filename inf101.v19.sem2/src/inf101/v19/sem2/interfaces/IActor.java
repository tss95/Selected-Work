package inf101.v19.sem2.interfaces;

import inf101.v19.sem2.examplegame.Game;

import java.util.List;

public interface IActor {

   // Gets the ship coverage from Game
    List getShipCoverage();

    // Makes sure all the ship coverage is properly accounted for.
    void beginAttackPhase();

    /**
     * Adds hits in the proper list
     * @param hit coordinate tuple of where the hit took place
     */
    void setShotsFired_hits(List hit);

    // Returns the list of coordinates that are the hits
    List getShotsFired_hits();

    /**
     * Adds misses in the proper list
     * @param miss coordinate tuple of where the hit took place
     */
    void setShotsFired_misses(List miss);

    // Returns the list of misses.
    List getShotsFired_misses();

    /**
     * Gets whether or not the actor has fired this round.
     * @return true if has fired this round, false otherwise
     */
    boolean getHasFired();

    // Sets hasFired
    void setHasFired(boolean hasFired);

    // Sets hasWon
    void setHasWon(boolean hasWon);

    // Gets hasWon
    boolean getHasWon();

    /**
     * Handles the damage for the actors. Determines if there's a hit or a miss, registers the information in their
     * proper lists and places the correct pin on the UI
     * @param gridX x coordinate of the shot
     * @param gridY y coordinate of the shot
     */
    void handleDamage(int gridX, int gridY);
}
