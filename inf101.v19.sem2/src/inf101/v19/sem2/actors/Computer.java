package inf101.v19.sem2.actors;
import inf101.v19.sem2.examplegame.Game;
import inf101.v19.sem2.interfaces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Computer implements IPc {

    public Game game;
    public IActor player;

    private String name;
    private boolean hasFired = false;
    private boolean hasWon = false;


    private boolean recentlyHit = false;
    private int shotsSinceLastHit = 0;
    private boolean hasHitPattern = false;
    private String hitPattern;
    private boolean foundEndOne = false;
    private int hitStreak = 0;
    private int hitsSinceDirectionSwitch = 0;

    private boolean end_left = false;
    private boolean end_right = false;
    private boolean end_upper = false;
    private boolean end_lower = false;

    private boolean hasSunkCarrier = false;
    private boolean hasSunkBattleship = false;
    private boolean hasSunkCruiser = false;
    private boolean hasSunkSubmarine = false;
    private boolean hasSunkDestroyer = false;


    List<List<Integer>> shotsFired_hits = new ArrayList<>();
    List<List<Integer>> shotsFired_misses = new ArrayList<>();
    List<List<Integer>> pc_ship_coverage = new ArrayList<>();


    public void setHitStreak(int hitStreak) {
        this.hitStreak = hitStreak;
    }

    public int getHitStreak() {
        return hitStreak;
    }

    public void setShotsFired_hits(List hit) {
        shotsFired_hits.add(hit);
    }

    public List getShotsFired_hits() {
        return shotsFired_hits;
    }

    public void setShotsFired_misses(List miss) {
        shotsFired_misses.add(miss);
    }

    public List getShotsFired_misses() {
        return shotsFired_misses;
    }

    public void setHasFired(boolean hasFired) {
        this.hasFired = hasFired;
    }

    public boolean getHasFired() {
        return hasFired;
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public boolean getHasWon() {
        return hasWon;
    }

    public void setRecentlyHit(boolean recentlyHit) {
        this.recentlyHit = recentlyHit;
    }

    public boolean getRecentlyHit() {
        return recentlyHit;
    }

    public void setShotsSinceLastHit(int shotsSinceLastHit) {
        this.shotsSinceLastHit = shotsSinceLastHit;
    }

    public int getShotsSinceLastHit() {
        return shotsSinceLastHit;
    }



    public int longestShipAlive() {
        if (!hasSunkCarrier) {
            return 5;
        }
        else if (!hasSunkBattleship) {
            return 4;
        }
        else if (!hasSunkCruiser) {
            return 3;
        }
        else if (!hasSunkSubmarine) {
            return 3;
        } else {
            return 2;
        }
    }
    public void sinkShip(){
        foundEndOne = false;
        hasHitPattern = false;
        recentlyHit = false;
        hitStreak = 0;
        shotsSinceLastHit = 0;
        hitPattern = "";
        hitsSinceDirectionSwitch = 0;
        end_left = false;
        end_right = false;
        end_upper = false;
        end_lower = false;
    }

    public void sinkByLength(int hitStreak) {
        switch (hitStreak) {
            case 5:
                hasSunkCarrier = true;
                sinkShip();
                System.out.println("Sunk carrier");
                return;
            case 4:
                hasSunkBattleship = true;
                sinkShip();
                System.out.println("Sunk battleship");
                return;
            case 3:
                if (hasSunkCruiser) {
                    hasSunkSubmarine = true;
                    sinkShip();
                    System.out.println("Sunk submarine");
                    return;
                } else {
                    hasSunkCruiser = true;
                    sinkShip();
                    System.out.println("Sunk cruiser");
                    return;
                }
            case 2:
                hasSunkDestroyer = true;
                sinkShip();
                System.out.println("Sunk destroyer");
                return;
        }
    }

    @Override
    public void handleDamage(int gridX, int gridY) {
        List<Integer> attackedCell = new ArrayList<>();
        attackedCell.add(gridX);
        attackedCell.add(gridY);
        if (pc_ship_coverage.contains(attackedCell)) {
            pc_ship_coverage.remove(pc_ship_coverage.indexOf(attackedCell));
            player.setShotsFired_hits(attackedCell);
            System.out.println("It's a confirmed hit");
        } else {
            player.setShotsFired_misses(attackedCell);
            System.out.println("You miss");
        }
        game.checkWinCondition();
    }

    public boolean hitOrMiss(int gridX, int gridY) {
        List<Integer> attackedCell = new ArrayList<>();
        attackedCell.add(gridX);
        attackedCell.add(gridY);
        if (pc_ship_coverage.contains(attackedCell)) {
            return true;
        } else {
            return false;
        }
    }

    public void init(String name, Game game, IActor player) {
        this.name = name;
        this.game = game;
        this.player = player;
    }

    public void beginAttackPhase() {
        pc_ship_coverage.addAll(game.getPcShipCoverage());
    }


    public List getShipCoverage() {
        return pc_ship_coverage;
    }

    public IShip getCurrentShip() {
        return game.ShipNr();
    }

    public int getShipNumber() {
        return game.getShipNumber();
    }


    public void infoHandler() {
        List<List<Integer>> currentShipHit = new ArrayList<>();
        List<List<Integer>> currentMissesSinceLastHit = new ArrayList<>();
        if (recentlyHit == true) {
            for (int i = 0; i < hitStreak; i++) {
                List<Integer> temp = new ArrayList<>();
                temp.clear();
                int x = shotsFired_hits.get(shotsFired_hits.size() - (1 + i)).get(0);
                int y = shotsFired_hits.get(shotsFired_hits.size() - (1 + i)).get(1);
                temp.add(x);
                temp.add(y);
                currentShipHit.add(0, temp);
            }
            for (int i = 0; i < shotsSinceLastHit; i++) {
                List<Integer> temp = new ArrayList<>();
                temp.clear();
                int x = shotsFired_misses.get(shotsFired_misses.size() - (1 + i)).get(0);
                int y = shotsFired_misses.get(shotsFired_misses.size() - (1 + i)).get(1);
                temp.add(x);
                temp.add(y);
                currentMissesSinceLastHit.add(0, temp);
            }

            int first_hitX = (int) ((List) currentShipHit.get(0)).get(0);
            int first_hitY = (int) ((List) currentShipHit.get(0)).get(1);
            int recent_hitX = (int) ((List) currentShipHit.get(currentShipHit.size() - 1)).get(0);
            int recent_hitY = (int) ((List) currentShipHit.get(currentShipHit.size() - 1)).get(1);

            if (!shotsFired_misses.isEmpty()) {
                if (foundEndOne) {
                    int recent_missX = (int) ((List) shotsFired_misses.get(shotsFired_misses.size() - 1)).get(0);
                    int recent_missY = (int) ((List) shotsFired_misses.get(shotsFired_misses.size() - 1)).get(1);

                    if (hitPattern == "Vertical") {
                        if (recent_missY < recent_hitY && recent_hitX == recent_missX) {
                            sinkByLength(hitStreak);
                        }
                        if (hitPattern == "Horizontal" && recent_hitY == recent_missY) {
                            if (recent_missX > recent_hitX) {
                                sinkByLength(hitStreak);
                            }
                        }
                    }
                }
            }


            // Continuing with corners..
            // If we have a hit in the corner and then a single miss or hit then we have a pattern and also we know which side the edge is on.
            if ((first_hitX == 0 && (first_hitY == 0 || first_hitY == game.getNumRows() - 1)) || (first_hitX == game.getNumCols() - 1 && (first_hitY == 0 || first_hitY == game.getNumRows() - 1))) {
                if (!foundEndOne) {
                    if (((hitStreak == 1 && shotsSinceLastHit == 1) || hitStreak == 2)) {
                        if (hitStreak == 1 && shotsSinceLastHit == 1) {
                            System.out.println("Corner shot and a miss. Should have pattern and edge");
                            int missX = (int) ((List) currentMissesSinceLastHit.get(0)).get(0);
                            int missY = (int) ((List) currentMissesSinceLastHit.get(0)).get(1);
                            if (missX != first_hitX && missY == first_hitY) {
                                hasHitPattern = true;
                                hitPattern = "Vertical";
                                // Upper Left and Right
                                if ((first_hitX == 0 && first_hitY == 0) || (first_hitX == game.getNumCols() - 1 && first_hitY == 0)) {
                                    end_upper = true;
                                    foundEndOne = true;
                                    return;
                                }
                                // Lower Right and Left
                                if ((first_hitX == game.getNumCols() - 1 && first_hitY == 0) || (first_hitX == 0 && first_hitY == 0)) {
                                    end_lower = true;
                                    foundEndOne = true;
                                    return;
                                }

                            }
                            if (missY != first_hitY && missX == first_hitX) {
                                hasHitPattern = true;
                                hitPattern = "Horizontal";
                                // Upper and lower left
                                if ((first_hitX == 0 && first_hitY == 0) || (first_hitX == 0 && first_hitY == game.getNumRows() - 1)) {
                                    end_left = true;
                                    foundEndOne = true;
                                    return;
                                }
                                // Upper and lower right
                                if ((first_hitX == game.getNumCols() - 1 && first_hitY == 0) || (first_hitX == game.getNumCols() - 1 && first_hitY == game.getNumRows() - 1)) {
                                    end_right = true;
                                    foundEndOne = true;
                                    return;
                                }
                            }
                        }
                        if (hitStreak == 2) {
                            if (recent_hitX == first_hitX) {
                                if (first_hitY == 0) {
                                    end_upper = true;
                                    foundEndOne = true;
                                }
                                if (first_hitY == game.getNumRows() - 1) {
                                    end_lower = true;
                                    foundEndOne = true;
                                }
                            }
                            if (recent_hitY == first_hitY) {
                                if (first_hitX == 0) {
                                    end_left = true;
                                    foundEndOne = true;
                                }
                                if (first_hitX == game.getNumCols() - 1) {
                                    end_right = true;
                                    foundEndOne = true;
                                }
                            }
                        }
                    }
                }
            }
            // Done dealing with corners
            // If the recent hit is a first hit and an open shot and there's no pattern and there are 2 misses then you know you have an edge.
            // If there are 3 misses since last hit then you have an edge and a pattern.
            // Since an open hit is followed by below -> left -> upper -> right, we know after 3 hits that we have found the left end
            if (hitStreak == 1) {
                // Open shot:
                if ((recent_hitX != 0 || recent_hitX != game.getNumCols() - 1) || (recent_hitY != 0 || recent_hitY != game.getNumRows() - 1)) {
                    if (shotsSinceLastHit == 3) {
                        hasHitPattern = true;
                        hitPattern = "Horizontal";
                        foundEndOne = true;
                        end_left = true;
                    }
                }
            }
            // If you have an open shot and then you miss twice followed by a hit on the third, you know you have a lower edge and a pattern.
            if ((first_hitX != 0 || first_hitX != game.getNumCols() - 1) || (first_hitY != 0 || first_hitY != game.getNumRows() - 1)) {
                if (hitStreak == 2 && !foundEndOne && recent_hitY == first_hitY - 1) {
                    foundEndOne = true;
                    hasHitPattern = true;
                    hitPattern = "Vertical";
                    end_lower = true;
                }
            }
            //if (hitStreak == 2 && foundEndOne) {
            //    hitsSinceDirectionSwitch += 1;

            //}
            // If you have 2 hits then you know your pattern.
            if (hitStreak == 2) {
                if (recent_hitX == first_hitX) {
                    hasHitPattern = true;
                    hitPattern = "Vertical";
                }
                if (recent_hitY == first_hitY) {
                    hasHitPattern = true;
                    hitPattern = "Horizontal";
                }
            }
            // Now to deal with walls.
            // If you have a pattern and you hit a wall perpendicularly, then you know you have found an end.
            if (hasHitPattern == true) {
                if (hitPattern == "Horizontal") {
                    if (recent_hitX == 0) {
                        System.out.println("Hit left wall and has horizontal pattern.");
                        if (foundEndOne) {
                            System.out.println("found second edge and handled correctly");
                            sinkByLength(hitStreak);
                        } else {
                            System.out.println("found first edge");
                            foundEndOne = true;
                            end_left = true;
                        }
                    }
                    if (recent_hitX == game.getNumCols() - 1) {
                        System.out.println("Hit right wall with horizontal pattern.");
                        if (foundEndOne) {
                            System.out.println("Found edge two and handled correctly");
                            sinkByLength(hitStreak);
                        } else {
                            System.out.println("Found first edge");
                            foundEndOne = true;
                            end_right = true;
                        }
                    }
                }
                if (hitPattern == "Vertical") {
                    if (recent_hitY == 0) {
                        System.out.println("Hit upper wall with vertical pattern");
                        if (foundEndOne) {
                            System.out.println("Found edge two and handled correctly");
                            sinkByLength(hitStreak);
                        } else {
                            System.out.println("Found first edge");
                            foundEndOne = true;
                            end_upper = true;
                        }
                    }
                    if (recent_hitY == game.getNumRows() - 1) {
                        System.out.println("Hit lower wall with vertical pattern");
                        if (foundEndOne) {
                            System.out.println("Found edge two and handled correctly");
                            sinkByLength(hitStreak);
                        } else {
                            System.out.println("Found first edge");
                            foundEndOne = true;
                            end_lower = true;
                        }
                    }
                }
            }
            // If you have an initial wall shot, and then have one miss, then you know you have found an end. I know this because I made sure the the AI always does his first
            // attempt at a next shot along the wall.
            if (hitStreak == 2) {
                // Really just checks if the first shot is a wall shot
                if ((first_hitX == 0 && (first_hitY != 0 && first_hitY != game.getNumRows() - 1)) ||
                        (first_hitX == game.getNumCols() - 1 && (first_hitY != 0 && first_hitY != game.getNumRows() - 1)) ||
                        (first_hitY == 0 && (first_hitX != 0 && first_hitX != game.getNumCols() - 1)) ||
                        (first_hitY == game.getNumRows() - 1 && (first_hitX != 0 && first_hitX != game.getNumCols() - 1))) {
                    // Left wall
                    if (first_hitX == 0 && first_hitY == recent_hitY) {
                        if (!foundEndOne) {
                            foundEndOne = true;
                            end_left = true;
                            hitPattern = "Horizontal";
                            hitsSinceDirectionSwitch += 1;
                        }
                    }
                    // Right wall
                    if (first_hitX == game.getNumCols() - 1 && first_hitY == recent_hitY) {
                        if (!foundEndOne) {
                            foundEndOne = true;
                            end_right = true;
                            hitPattern = "Horizontal";
                            hitsSinceDirectionSwitch += 1;
                        }
                    }
                    // Upper wall
                    if (first_hitY == 0 && first_hitX == recent_hitX) {
                        if (!foundEndOne) {
                            foundEndOne = true;
                            end_upper = true;
                            hitPattern = "Vertical";
                            hitsSinceDirectionSwitch += 1;
                        }
                    }
                    // Lower wall
                    if (first_hitY == game.getNumRows() - 1 && first_hitX == recent_hitX) {
                        if (!foundEndOne) {
                            foundEndOne = true;
                            end_lower = true;
                            hitPattern = "Vertical";
                            hitsSinceDirectionSwitch += 1;
                        }
                    }
                }
            }
            // If you have an initial wall shot and then two misses, then you know have an edge, know its direction, and also know your pattern.
            if (hitStreak == 1) {
                if ((first_hitX == 0 && (first_hitY != 0 && first_hitY != game.getNumRows() - 1)) ||
                        (first_hitX == game.getNumCols() - 1 && (first_hitY != 0 && first_hitY != game.getNumRows() - 1)) ||
                        (first_hitY == 0 && (first_hitX != 0 && first_hitX != game.getNumCols())) ||
                        (first_hitY == game.getNumRows() - 1 && (first_hitX != 0 && first_hitX != game.getNumCols() - 1))) {
                    System.out.println("Registered wall shot");
                    //if (shotsSinceLastHit == 1) {
                    //    foundEndOne = true;
                    //}
                    if (shotsSinceLastHit == 2) {
                        if (first_hitX == 0) {
                            hasHitPattern = true;
                            hitPattern = "Vertical";
                            end_lower = true;
                        }
                        if (first_hitX == game.getNumCols() - 1){
                            hasHitPattern = true;
                            hitPattern = "Vertical";
                            end_lower = true;
                        }
                        if (first_hitY == 0) {
                            hasHitPattern = true;
                            hitPattern = "Horizontal";
                            end_left = true;
                        }
                        if (first_hitY == game.getNumRows() - 1) {
                            hasHitPattern = true;
                            hitPattern = "Horizontal";
                            end_left = true;
                        }
                    }
                }
            }
            // AI can also find an edge if it has a pattern and then misses. If you already have an edge from before you know you have sunk a ship.
            if (hasHitPattern) {
                if (foundEndOne && shotsSinceLastHit == 1 && hitsSinceDirectionSwitch == 1) {
                    sinkByLength(hitStreak);
                }
                if (foundEndOne && shotsSinceLastHit == 2) {
                    sinkByLength(hitStreak);
                }
                // Since the AI always goes left with horizontal pattern and no edge, and always down with a vertical pattern and no edge, it can determine which edge it is.
                else {
                    if (shotsSinceLastHit == 1) {
                        if (hitPattern == "Horizontal") {
                            foundEndOne = true;
                            end_left = true;
                        }
                        if (hitPattern == "Vertical") {
                            foundEndOne = true;
                            end_lower = true;
                        }
                    }
                }
                // Dealing with misses already on the board that are not a part of the current "sequence"
                if (hasHitPattern) {
                    List<Integer> oneUp = new ArrayList<>();
                    if (foundEndOne) {
                        if (hitPattern == "Vertical") {
                            oneUp.clear();
                            oneUp.add(recent_hitX);
                            oneUp.add(recent_hitY - 1);
                            if (shotsFired_misses.contains(oneUp)) {
                                sinkByLength(hitStreak);
                            }
                        }
                        if (hitPattern == "Horizontal") {
                            oneUp.clear();
                            oneUp.add(recent_hitX + 1);
                            oneUp.add(recent_hitY);
                            if (shotsFired_misses.contains(oneUp)) {
                                sinkByLength(hitStreak);
                            }
                        }
                    }
                    else {
                        if (hitPattern == "Vertical") {
                            oneUp.clear();
                            oneUp.add(recent_hitX);
                            oneUp.add(recent_hitY + 1);
                            if (shotsFired_misses.contains(oneUp)) {
                                foundEndOne = true;
                                end_lower = true;
                            }
                        }
                        if (hitPattern == "Horizontal") {
                            oneUp.clear();
                            oneUp.add(recent_hitX - 1);
                            oneUp.add(recent_hitY);
                            if (shotsFired_misses.contains(oneUp)) {
                                foundEndOne = true;
                                end_left = true;
                            }
                        }
                    }
                }
            }

            //Now if a hitstreak is equal to the length of the longest ship, then we can assume we have sunk a ship.
            if (hitStreak == longestShipAlive()) {
                System.out.println("Hitstreak is as long as the longest ship");
                sinkByLength(hitStreak);
            }
            // If you find an edge, then switch direction and you miss, you can assume you have sunk a ship of that size.
            // Needed to make a little adjustment requiring the AI to have to know the direction of each side in order to sink.
            if((end_lower && end_upper) || (end_right && end_left)) {
                sinkByLength(hitStreak);
            }
        }
        status();
    }


    public List findNextGoodMove() {
        List <Integer> nextMove = new ArrayList<>();

        List<List<Integer>> currentShipHit = new ArrayList<>();
        List<List<Integer>> currentMissesSinceLastHit = new ArrayList<>();

        if (recentlyHit == true) {
            for (int i = 0; i < hitStreak; i++) {
                List<Integer> temp = new ArrayList<>();
                temp.clear();
                int x = shotsFired_hits.get(shotsFired_hits.size() - (1 + i)).get(0);
                int y = shotsFired_hits.get(shotsFired_hits.size() - (1 + i)).get(1);
                temp.add(x);
                temp.add(y);
                currentShipHit.add(0, temp);
            }
            for (int i = 0; i < shotsSinceLastHit; i++) {
                List<Integer> temp = new ArrayList<>();
                temp.clear();
                int x = shotsFired_misses.get(shotsFired_misses.size() - (1 + i)).get(0);
                int y = shotsFired_misses.get(shotsFired_misses.size() - (1 + i)).get(1);
                temp.add(x);
                temp.add(y);
                currentMissesSinceLastHit.add(0, temp);
            }
        }
        System.out.println(currentShipHit);
        int recent_hitX = (int) ((List) currentShipHit.get(currentShipHit.size() - 1)).get(0);
        int recent_hitY = (int) ((List) currentShipHit.get(currentShipHit.size() - 1)).get(1);
        int first_hitX = (int) ((List) currentShipHit.get(0)).get(0);
        int first_hitY = (int) ((List) currentShipHit.get(0)).get(1);

        System.out.println("First hit: x y " + first_hitX + " " + first_hitY);
        System.out.println("Recent hit x y " + recent_hitX + " " + recent_hitY);
        if (recentlyHit) {
            // If we have a pattern, just follow it essentially. Of course if you have an end, you go the opposite direction from your first hit.
            if (hasHitPattern) {
                if (foundEndOne) {
                    if (hitPattern == "Horizontal") {
                        System.out.println("Current hits since dir switch " + hitsSinceDirectionSwitch);
                        if (end_right) {
                            hitsSinceDirectionSwitch += 1;
                            nextMove.add(first_hitX - (hitsSinceDirectionSwitch));
                            nextMove.add(first_hitY);
                            return nextMove;
                        } else {
                            hitsSinceDirectionSwitch += 1;
                            nextMove.add(first_hitX + (hitsSinceDirectionSwitch));
                            nextMove.add(first_hitY);
                            return nextMove;
                        }
                    }
                    if (hitPattern == "Vertical") {
                        if (end_upper) {
                            hitsSinceDirectionSwitch += 1;
                            nextMove.add(first_hitX);
                            nextMove.add(recent_hitY + 1);
                            return nextMove;
                        } else {
                            if (end_lower && (first_hitY - (hitStreak - 1) == recent_hitY)) {
                                hitsSinceDirectionSwitch += 2;
                                nextMove.add(recent_hitX);
                                nextMove.add(first_hitY - (hitStreak));
                                return nextMove;
                            }
                            else {
                                if (end_lower) {
                                    hitsSinceDirectionSwitch += 1;
                                    nextMove.add(recent_hitX);
                                    nextMove.add(first_hitY - hitsSinceDirectionSwitch);
                                    return nextMove;
                                }
                                hitsSinceDirectionSwitch += 1;
                                nextMove.add(first_hitX);
                                nextMove.add(first_hitY - (hitsSinceDirectionSwitch));
                                return nextMove;
                            }
                        }
                    }
                } else {
                    if (hitPattern == "Horizontal") {
                        nextMove.add(recent_hitX - 1);
                        nextMove.add(recent_hitY);
                        return nextMove;
                    }
                    if (hitPattern == "Vertical") {
                        nextMove.add(recent_hitX);
                        nextMove.add(recent_hitY + 1);
                        return nextMove;
                    }
                }
            } else {
                // If we have only one hit. Important to differentiate between wall shots, corner shots and open shots.
                if (hitStreak == 1) {
                    // Corner shots first:
                    if ((first_hitX == 0 && (first_hitY == 0 || first_hitY == game.getNumRows() - 1)) || (first_hitX == game.getNumCols() - 1 && (first_hitY == 0 || first_hitY == game.getNumRows() - 1))) {
                        // Upper left
                        if (first_hitX == 0 && first_hitY == 0) {
                            nextMove.add(first_hitX);
                            nextMove.add(first_hitY + 1);
                            return nextMove;
                        }
                        // Lower left
                        if (first_hitX == 0 && first_hitY == game.getNumRows() - 1) {
                            nextMove.add(first_hitX);
                            nextMove.add(first_hitY - 1);
                            return nextMove;
                        }
                        // Upper right
                        if (first_hitX == game.getNumCols() - 1 && first_hitY == 0) {
                            nextMove.add(first_hitX);
                            nextMove.add(first_hitY + 1);
                            return nextMove;
                        }
                        // Lower right
                        if (first_hitX == game.getNumCols() - 1 && first_hitY == game.getNumRows() - 1) {
                            nextMove.add(first_hitX);
                            nextMove.add(first_hitY - 1);
                            return nextMove;
                        }
                    }
                    // Wall shots:
                    if ((first_hitX == 0 && (first_hitY != 0 && first_hitY != game.getNumRows() - 1)) ||
                            (first_hitX == game.getNumCols() - 1 && (first_hitY != 0 && first_hitY != game.getNumRows() - 1)) ||
                            (first_hitY == 0 && (first_hitX != 0 && first_hitX != game.getNumCols() - 1)) ||
                            (first_hitY == game.getNumRows() - 1 && (first_hitX != 0 && first_hitX != game.getNumCols() - 1))) {
                        // This depends on how many misses there are. After two misses there will be a pattern and an edge.
                        // Left wall:
                        if (first_hitX == 0) {
                            if (shotsSinceLastHit == 0)  {
                                nextMove.add(first_hitX);
                                nextMove.add(first_hitY + 1);
                                return nextMove;
                            }
                            if (shotsSinceLastHit == 1) {
                                nextMove.add(first_hitX + 1);
                                nextMove.add(first_hitY);
                                return nextMove;
                            }
                        }
                        // Right wall:
                        if (first_hitX == game.getNumCols() - 1) {
                            if (shotsSinceLastHit == 0) {
                                nextMove.add(first_hitX);
                                nextMove.add(first_hitY + 1);
                                return nextMove;
                            }
                            if (shotsSinceLastHit == 1) {
                                nextMove.add(first_hitX - 1);
                                nextMove.add(first_hitY);
                                return nextMove;
                            }
                        }
                        // Upper wall:
                        if (first_hitY == 0) {
                            if (shotsSinceLastHit == 0) {
                                nextMove.add(first_hitX - 1);
                                nextMove.add(first_hitY);
                                return nextMove;
                            }
                            if (shotsSinceLastHit == 1) {
                                nextMove.add(first_hitX);
                                nextMove.add(first_hitY + 1);
                                return nextMove;
                            }
                        }
                        // Lower wall:
                        if (first_hitY == game.getNumCols() - 1) {
                            if (shotsSinceLastHit == 0) {
                                nextMove.add(first_hitX - 1);
                                nextMove.add(first_hitY);
                                return nextMove;
                            }
                            if (shotsSinceLastHit == 1) {
                                nextMove.add(first_hitX);
                                nextMove.add(first_hitY - 1);
                                return nextMove;
                            }
                        }
                    }
                    // And finally open shots:
                    else {
                        if (shotsSinceLastHit == 0) {
                            nextMove.add(first_hitX);
                            nextMove.add(first_hitY + 1);
                            return nextMove;
                        }
                        if (shotsSinceLastHit == 1) {
                            nextMove.add(first_hitX - 1);
                            nextMove.add(first_hitY);
                            return nextMove;
                        }
                        if (shotsSinceLastHit == 2) {
                            nextMove.add(first_hitX);
                            nextMove.add(first_hitY - 1);
                            return nextMove;
                        }
                    }
                }
            }
        }
        else {
            throw new IndexOutOfBoundsException("Something went wrong, no good moves");
        }
        return nextMove;
    }


    public void fire() {
        if (!recentlyHit) {
            List<List<Integer>> wholeGrid = new ArrayList<>();
            for (int x = 0; x < game.getNumCols(); x++) {
                for (int y = 0; y < game.getNumRows(); y++) {
                    List<Integer> temp = new ArrayList<>();
                    temp.clear();
                    temp.add(x);
                    temp.add(y);
                    wholeGrid.add(temp);
                }
            }
            boolean evenColumns = false;
            if (game.getNumCols() % 2 == 0) {
                evenColumns = true;
            }
            List<List<Integer>> everyOther = new ArrayList<>();
            // Works for even numbers of columns
            if (game.getNumCols() % 2 == 0) {
                for (int i = 0; i < wholeGrid.size(); i++) {
                    boolean evenRow;
                    if (wholeGrid.get(i).get(0) % 2 == 0) {
                        evenRow = true;
                    } else {
                        evenRow = false;
                    }
                    if (evenRow) {
                        if (i % 2 == 0) {
                            everyOther.add(wholeGrid.get(i));
                        }
                    } else {
                        if (i % 2 != 0) {
                            everyOther.add(wholeGrid.get(i));
                        }
                    }
                }
            }
            // Works for uneven numbers of columns
            else {
                for (int i = 0; i < wholeGrid.size(); i++) {
                    if (i % 2 == 0) {
                        everyOther.add(wholeGrid.get(i));
                    }
                }
            }
            // Removing previous attempts from the options
            for (int i = 0; i < shotsFired_hits.size(); i++) {
                if (everyOther.contains(shotsFired_hits.get(i))) {
                    everyOther.remove(everyOther.indexOf(shotsFired_hits.get(i)));
                }
            }
            for (int i = 0; i < shotsFired_misses.size(); i++) {
                if (everyOther.contains(shotsFired_misses.get(i))) {
                    everyOther.remove(everyOther.indexOf(shotsFired_misses.get(i)));
                }
            }
            // If nothing has been hit yet. Or a ship has been sunk.
            Random random = new Random();
            int index = random.nextInt(everyOther.size());
            int x = everyOther.get(index).get(0);
            int y = everyOther.get(index).get(1);
            player.handleDamage(x, y);
            return;
        }
        //If there is a hit pattern:
        else {
            List <Integer> nextMove = new ArrayList<>();
            nextMove.addAll(findNextGoodMove());
            int x = nextMove.get(0);
            int y = nextMove.get(1);

            System.out.println("Wants to hit x y :" + x + " " + y);
            player.handleDamage(x,y);
        }
    }

    public void placeShips() {
        System.out.println("From computer: " + game.getShipNumber());
        while (getShipNumber() < 5) {
            System.out.println(getShipNumber());
            Random random = new Random();
            //Generate a random direction.
            int dir = (random.nextInt(2) + 1);
            System.out.println("Directon: " + dir);
            // List of possible placements on the grid.
            List<List<Integer>> possibleMoves = new ArrayList<>();
            // If the ship is oriented Vertically.
            if (dir == 1) {
                for (int x = 0; x < game.getNumCols(); x++) {
                    for (int y = 0; y < game.getNumRows() - getCurrentShip().getLengthLessOne(); y++) {
                        List<Integer> temp = new ArrayList<>();
                        temp.clear();
                        temp.add(x);
                        temp.add(y);
                        possibleMoves.add(temp);
                    }
                }
            }
            // If the ship is oriented horizontally
            if (dir == 2) {
                for (int x = 0; x < game.getNumCols() - getCurrentShip().getLengthLessOne(); x++) {
                    for (int y = 0; y < game.getNumRows(); y++) {
                        List<Integer> temp = new ArrayList<>();
                        temp.clear();
                        temp.add(x);
                        temp.add(y);
                        possibleMoves.add(temp);
                    }
                }
            }

            // List of possible coords generated, not considering previous placements. (Above)
            if (!getShipCoverage().isEmpty()) {
                for (int i = 0; i < getShipCoverage().size(); i++) {
                    if (possibleMoves.contains(getShipCoverage().get(i))) {
                        possibleMoves.remove(possibleMoves.indexOf(getShipCoverage().get(i)));
                    } else {
                        continue;
                    }
                }
            }
            // Possible moves has been altered to only contain valid placements on the grid,
            // including in relation to other ships.
            // Now for the actual placement:
            // Get random x0, x1 from possible Moves list.
            int index = random.nextInt(possibleMoves.size());
            List<Integer> XY = new ArrayList<>(possibleMoves.get(index));
            int x0 = XY.get(0);
            int y0 = XY.get(1);
            getCurrentShip().initShip(x0, y0);
            if (dir == 2) {
                getCurrentShip().rotateShip();
            }
            //Check if it can be placed here

            if (game.canPlace()) {

                game.ui.placeShipFinal(getCurrentShip().getX0(), getCurrentShip().getY0(), getCurrentShip().getX1(), getCurrentShip().getY1());
                System.out.println(getCurrentShip().getShipName() + " was placed at (x0, y0) (x1, y1): ("
                        + getCurrentShip().getX0() + ", " + getCurrentShip().getY0() + ") (" + getCurrentShip().getX1() + ", " + getCurrentShip().getY1() + ")");
                System.out.println(game.ShipNr().getGridCovered());
                game.setPcShipCoverage();
                game.setShipNumber(game.getShipNumber() + 1);
            } else {
                while (!game.canPlace()) {
                    int newIndex = random.nextInt(possibleMoves.size());
                    List<Integer> newXY = new ArrayList<>(possibleMoves.get(newIndex));
                    int newx0 = newXY.get(0);
                    int newy0 = newXY.get(1);
                    getCurrentShip().initShip(newx0, newy0);
                    if (dir == 2) {
                        getCurrentShip().rotateShip();
                    }
                }
                game.ui.placeShipFinal(getCurrentShip().getX0(), getCurrentShip().getY0(), getCurrentShip().getX1(), getCurrentShip().getY1());
                System.out.println(getCurrentShip().getShipName() + " was placed at (x0, y0) (x1, y1): ("
                        + getCurrentShip().getX0() + ", " + getCurrentShip().getY0() + ") ("
                        + getCurrentShip().getX1() + ", " + getCurrentShip().getY1() + ")");
                System.out.println(game.ShipNr().getGridCovered());
                game.ShipNr().gridCovered();
                game.setPcShipCoverage();
                game.setShipNumber(game.getShipNumber() + 1);
            }
        }

        System.out.println(getShipCoverage());
    }

    public void status() {
        System.out.println("Has pattern: " + hasHitPattern);
        System.out.println("Pattern: " + hitPattern);
        System.out.println("Hitstreak: " + hitStreak);
        System.out.println("Found edge one: " + foundEndOne);
        System.out.println("Misses after hit: " + shotsSinceLastHit);
        if (end_left) {
            System.out.println("Has left end");
        }
        if (end_right) {
            System.out.println("Has right end");
        }
        if (end_upper) {
            System.out.println("Has upper end");
        }
        if (end_lower) {
            System.out.println("Has lower end");
        }
    }
}

