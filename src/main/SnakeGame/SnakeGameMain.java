package main.SnakeGame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main class that the snake game will run. Will call the view
 *
 * @author: Lawrence Li
 * @author: Christopher Asbrock
 */
public class SnakeGameMain extends Application
{

    /** The view of the snake */
    private final SnakeGameView VIEW = new SnakeGameView();

    /**
     * javafx start method, sets and shows the stage,
     * and handles the user input before and after the scene
     *
     * @param primaryStage - the primary stage
     */
    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setScene(new Scene(this.VIEW.getRoot()));

        this.VIEW.userInputGameGoing(primaryStage);
        this.VIEW.userInputMainMenu();

        primaryStage.show();
    }

    /**
     * main program launch
     *
     * @param args - command line arguments - NOT USED
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}