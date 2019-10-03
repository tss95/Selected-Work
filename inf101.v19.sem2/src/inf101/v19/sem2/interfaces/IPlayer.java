package inf101.v19.sem2.interfaces;

import inf101.v19.sem2.examplegame.Game;

public interface IPlayer extends IActor {

    /**
     * Initiates the player class.
     * @param name of the player
     * @param game the game class
     * @param pc the pc class
     */
    void initiate(String name, Game game, IPc pc);
}
