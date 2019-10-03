package inf101.v19.sem2.actors;

import inf101.v19.sem2.examplegame.Game;
import inf101.v19.sem2.interfaces.IPc;
import inf101.v19.sem2.interfaces.IPlayer;

import java.util.ArrayList;
import java.util.List;

public class Player implements IPlayer {

    private Game game;
    private IPc pc;

    private boolean hasFired = false;
    private boolean hasWon = false;

    private List<List<Integer>> shotsFired_hits = new ArrayList<>();
    private List<List<Integer>> shotsFired_misses = new ArrayList<>();
    private List<List<Integer>> player_ship_coverage = new ArrayList<>();

    public void setShotsFired_hits(List hit) {shotsFired_hits.add(hit);}
    public List getShotsFired_hits() {return shotsFired_hits;}
    public void setShotsFired_misses(List miss) {shotsFired_misses.add(miss);}
    public List getShotsFired_misses() {return shotsFired_misses;}

    public void setHasFired(boolean hasFired) {this.hasFired = hasFired;}
    public boolean getHasFired() {return hasFired;}

    public void setHasWon(boolean hasWon) {this.hasWon = hasWon;}
    public boolean getHasWon() {return hasWon;}


    public void initiate(String name, Game game, IPc pc){
        String name1 = name;
        this.game = game;
        this.pc = pc;

    }

    public void beginAttackPhase() {
        player_ship_coverage.addAll(getShipCoverage());
    }

    public void handleDamage(int gridX, int gridY) {
        List<Integer> attackedCell = new ArrayList<>();
        attackedCell.add(gridX);
        attackedCell.add(gridY);
        //What happens if the computer hits.
        if (player_ship_coverage.contains(attackedCell)) {
            player_ship_coverage.remove(player_ship_coverage.indexOf(attackedCell));
            pc.setShotsFired_hits(attackedCell);
            System.out.println("Computer hits at (x,y) (" + gridX + "," + gridY + ")");
            game.placePins();
            pc.setShotsSinceLastHit(0);
            pc.setHasFired(true);
            if (pc.getRecentlyHit()) {
                pc.setHitStreak(pc.getHitStreak() + 1);
                pc.infoHandler();
            }
            else {
                pc.setRecentlyHit(true);
                pc.setHitStreak(1);
                pc.infoHandler();
            }
        }
        //What happpens if the computer misess
        else {
            pc.setShotsFired_misses(attackedCell);
            game.placePins();
            pc.setHasFired(true);
            System.out.println("Computer missed at (x,y) (" + gridX + "," + gridY + ")");
            if (pc.getRecentlyHit()) {
                pc.setShotsSinceLastHit(pc.getShotsSinceLastHit() + 1);
                pc.infoHandler();
            }
        }
    }


    public List getShipCoverage(){return game.getPlayerShipCoverage();}

}
