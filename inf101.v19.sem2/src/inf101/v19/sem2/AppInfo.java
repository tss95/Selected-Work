package inf101.v19.sem2;

import inf101.v19.sem2.ui.Main;

import java.util.Arrays;
import java.util.List;

public class AppInfo {
	/**
	 * Battleship
	 */
	public static final String APP_NAME = "Battleship";
	/**
	 * The main class, for starting the application
	 */
	public static final Class<?> APP_MAIN_CLASS = inf101.v19.sem2.ui.Main.class; // e.g., inf101.v19.sem2.Main.class
	/**
	 * Your name.
	 */
	public static final String APP_DEVELOPER = "Tord Sture Stangeland";
	/**
	 * A short description.
	 */
	public static final String APP_DESCRIPTION = "Semesteroppgave2. This game uses a single board to play an entire game" +
			"of battleships. The game is played by a single player playing against an decently smart AI.";
	/**
	 * List of extra credits (e.g. for media sources)
	 */
	public static final List<String> APP_EXTRA_CREDITS = Arrays.asList(//
			/* "Graphics by Foo Bar" */
			);
	/**
	 * Help text. Could be used for an in-game help page, perhaps.
	 * <p>
	 * Use <code>\n</code> for new lines, <code>\f<code> between pages (if multi-page).
	 */
	public static final String APP_HELP = "Help:\n" +
			" - In the placement of ships phase, the player can rotate the ship by pressing 'R' on their keyboard. \n" +
			" - After all ships are placed, the player selects the 'Ready' button in order to proceed. \n" +
			" - The following screen is confirms the placement of the ships for the AI, and the player presses 'Ready' again \n" +
			" - The attack phase now begins and is done by selecting a cell on the screen, clicking on it at pressing 'Ready'. \n" +
			" - Then the player will see the attack from the computer and press 'Ready' again. \n" +
			" - The game is completed when one of the players (AI or the player) sinks their opponents fleet.";
}
