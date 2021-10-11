/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Lawrence Li
 * Section: 8am
 * Date: 4/25/20
 * Time: 10:34 AM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: MVCSnakeController
 *
 * Description:
 *
 * ****************************************
 */
package
        main.SnakeGame;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.Exception.SnakeException;

import java.util.ArrayList;

/**
 * A snake controller that update the view and display messages
 *
 * @author: Christopher Asbrock
 */
public class SnakeGameController {

    /** Menu message */
    protected String menuMessage;

    /** Game message */
    protected String gameMessage;

    /** Player info */
    protected String playerInfo;

    /** Player color */
    protected Color playerColor;

    /** width of the window */
    protected final int WIDTH = 800;

    /** height of the window */
    protected final int HEIGHT = 600;

    /** boolean for data writing */
    protected boolean dataWrite;

    /** boolean to check if the game is still going. False if the game is over (all snakes died or disconnected) */
    protected boolean gameGoing;

    /**a reference to the models item list*/
    private final ArrayList<Circle> ITEM_POSITIONING;

    /**a reference to the models list of snakes and its list of parts*/
    private final ArrayList<Circle>[] SNAKE_PARTS_POSITIONING;

    /**a reference to the scrap array that gets cleared after each update*/
    private final ArrayList<Node> SCRAP_NODES;

    /** MVC Snake View */
    private final SnakeGameView VIEW;

    /** MVC Snake Model */
    private final SnakeGameModel MODEL;

    /** Constructor of controller */
    public SnakeGameController(SnakeGameView view)
    {
        this.VIEW = view;
        this.MODEL = new SnakeGameModel(this);
        this.gameGoing = false;

        this.menuMessage = "";
        this.gameMessage = "";

        this.ITEM_POSITIONING = this.MODEL.getItemListPositions();
        this.SNAKE_PARTS_POSITIONING = this.MODEL.getSnakeListPositions();
        this.SCRAP_NODES = this.MODEL.getScrapNodes();

        this.dataWrite = false;
    }

    public SnakeGameView getVIEW()
    {
        return VIEW;
    }

    public int getNumOfPlayers()
    {
        return this.MODEL.playerCount;
    }

    /**
     * Set the host based on socket information
     * @param port the port
     * @param players max number of players to join the room
     * @param width width of the game window
     * @param height height of the game window
     */
    public void setHost(String port, String players, String width, String height)
    {
        try
        {
            this.MODEL.createNetwork(port, players, width, height);

            this.waitFor(1);

            //after the network is set up, locally join it
            this.setJoin("localhost", port);
        }
        catch (NumberFormatException | SnakeException e)
        {
            this.displayMenuMessage(e.getMessage());
        }
    }

    /**
     * Give some time for the program to start up the network
     * @param seconds number of seconds to wait
     */
    private void waitFor(int seconds)
    {
        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time < (seconds * 1000))
        {
            /*
                give this a moment to start up the network, or it'll will just fly into the connection
                which didnt have the chance to start isn't there

                much rather use the network as a model itself for the host, but little low on time, so
                maybe later
             */
        }
    }

    /**
     * Display menu message
     * @param message the message
     */
    public void displayMenuMessage(String message)
    {
        this.menuMessage = message;
        this.updateView();
    }

    /**
     * Display game message
     * @param message the message
     */
    public void displayGameMessage(String message)
    {
        this.gameMessage = message;
        this.updateView();
    }

    /**
     * method for users to join a server
     * @param host the host ip
     * @param port the host port
     */
    public void setJoin(String host, String port)
    {
        this.MODEL.modelInit(host, port);
    }

    public ArrayList<Circle> getItemPositions()
    {
        return ITEM_POSITIONING;
    }

    public ArrayList<Circle>[] getSnakePostions()
    {
        return SNAKE_PARTS_POSITIONING;
    }

    public ArrayList<Node> getTrash()
    {
        return SCRAP_NODES;
    }

    /**
     * Update the view of the snake if the game is still going, then it will write data for the game and
     * will be reflected on the game window
     */
    public void updateView()
    {
        if (gameGoing)
            this.dataWrite = true;
        Platform.runLater(this.VIEW::updateView);
    }

    /** handle left turn snake */
    public void leftTurn(boolean b)
    {
        if (this.gameGoing)
            this.MODEL.sendDirection(this.MODEL.TURN_LEFT, b);
    }

    /** handle right turn snake */
    public void rightTurn(boolean b)
    {
        if (this.gameGoing)
            this.MODEL.sendDirection(this.MODEL.TURN_RIGHT, b);
    }

    /**
     * Set player info when a player joins. Program will decide the colors of
     * that snake (player) and a number will be assigned
     * @param text the player name
     * @param color the color of the snake (player)
     */
    public void setPlayerInfo(String text, Color color)
    {
        this.playerInfo = text;
        this.playerColor = color;
        this.updateView();
    }
}
