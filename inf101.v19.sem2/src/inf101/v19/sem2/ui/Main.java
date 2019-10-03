package inf101.v19.sem2.ui;
 
import inf101.v19.sem2.examplegame.Game;
import inf101.v19.sem2.interfaces.IUIGame;
import inf101.v19.sem2.interfaces.IUserInterface;
import javafx.application.Application;

import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    //private IUIGame game2;
    //Game gameLogic;

    
    @Override
    public void start(Stage primaryStage) {
        IUserInterface ui = new UI(primaryStage);
        primaryStage.setTitle("Battleships");
        IUIGame game = new Game(12, 12);
        game.setUI(ui);
        //Button button = new Button("Next gamePhase");
        //button.setOnAction(e-> gameLogic.createButton());
    }
}
