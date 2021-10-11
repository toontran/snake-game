/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Lawrence Li
 * Section: 8am
 * Date: 4/25/20
 * Time: 10:01 AM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: MVCSnakeModel
 *
 * Description:
 *
 * ****************************************
 */
package main.SnakeGame;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import main.CommonInterfaces.GameGlobalValues;
import main.CommonInterfaces.Protocol;
import main.Exception.SnakeException;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A MVC snake model that is responsible for the snake and item in the game
 *
 * @author: Christopher Asbrock
 * @author: Tung Tran
 */
public class SnakeGameModel implements Protocol, GameGlobalValues
{
    /** The snake controller */
    private final SnakeGameController CONTROLLER;
    /** The snake shape change variable */
    private final int SNAKE_SHAPE_CHANGE = 100;
    /** The max port value */
    private final int MAX_PORT = 65535;
    /** The min port value */
    private final int MIN_PORT = 1000;
    /** The default snake piece reference, which is the initial head of the snake */
    private final Circle DEFAULT_SNAKE_PIECE_REFERENCE = new Circle(SNAKE_PIECE_SIZE,SNAKE_PIECE_SIZE,SNAKE_PIECE_SIZE);
    /**an arraylist holding the positions of all active items*/
    private final ArrayList<Circle> ALL_ITEM_POSITIONS;
    /**An array of the max players, that holds a reference to each of there ArrayList kept positions*/
    private final ArrayList<Circle>[] ALL_PLAYERS_SNAKE_POSITIONS;
    /**An array list for reference in the view that notes and nodes taken out of game for removal*/
    private final ArrayList<Node> SCRAP_NODES;

    /** The socket */
    private Socket socket;
    /** The network scanner in for input messages from server*/
    private Scanner networkIn;
    /** The network PrintStream out for output messages from client*/
    private PrintStream networkOut;

    /** The height */
    protected int height;
    /** The width */
    protected int width;
    /** The count of how many players in a game */
    protected int playerCount;

    /** A boolean to check if the game is running */
    protected boolean gameRunning;

    /** Constructor of the snake model */
    public SnakeGameModel(SnakeGameController controller)
    {
        this.CONTROLLER = controller;
        this.ALL_ITEM_POSITIONS = new ArrayList<>();
        this.ALL_PLAYERS_SNAKE_POSITIONS = new ArrayList[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS;i++)
            this.ALL_PLAYERS_SNAKE_POSITIONS[i] = new ArrayList<>();

        this.SCRAP_NODES = new ArrayList<>();
    }

    /**
     * The initialization of the model
     * @param host the host ip
     * @param port the host port
     */
    protected void modelInit(String host, String port)
    {
        try
        {
            this.gameRunning = true;

            this.CONTROLLER.displayMenuMessage("Waiting For More Players...");
            this.socket = new Socket(host, Integer.parseInt(port));
            System.out.println("Connected");

            this.networkIn = new Scanner(this.socket.getInputStream());
            this.networkOut = new PrintStream(this.socket.getOutputStream());

            this.startModel();
        }
        catch (IOException e)
        {
            this.CONTROLLER.displayMenuMessage(e.getMessage());
        }
        catch (NumberFormatException e)
        {
            this.CONTROLLER.displayMenuMessage("Port, Players, Width, And Height Can Only Be Integers");
        }
    }

    /**
     * A listener for the networking
     */
    private void listener()
    {
        try
        {
            while (true)
            {
                if (!this.networkIn.hasNextLine())
                    throw new SnakeException("Disconnected From Server");

                String input = this.networkIn.nextLine();
                String protocol = getProtocol(input);
                String message = getMessage(protocol, input);

                switch (protocol)
                {
                    case DATA:
                        this.updateSnake(message);
                        break;
                    case START_GAME:
                        this.CONTROLLER.gameGoing = true;
                        this.playerCount = Integer.parseInt(message);
                        break;
                    case PLAYER_INFO:
                        this.CONTROLLER.setPlayerInfo(
                                "PLAYER " + Integer.valueOf(message) + "  ",
                                this.getColor(Integer.parseInt(message) - 1, false
                                ));
                        break;
                    case MESSAGE:
                        this.CONTROLLER.displayGameMessage(message);
                        break;
                    case END_GAME:
                        throw new SnakeException(message);
                    default:
                        System.out.println("problem");
                }
            }
        }
        catch (SnakeException e)
        {
            this.CONTROLLER.displayGameMessage(e.getMessage());
            System.out.println(e.getMessage());
        }
        finally
        {
            this.close();
        }

    }

    /**
     * Close the model. (snake or item) and will remove them from the game
     */
    private void close()
    {
        try
        {
            this.networkOut.close();
            this.networkIn.close();
            this.socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Model closed down");
    }

    /**
     * Update the snake with the snake info (position of the snake) from server
     * @param snakeInfo the position of the snake, with length information.
     */
    private void updateSnake(String snakeInfo)
    {
        if (!this.CONTROLLER.dataWrite)
        {
            String[] positions = snakeInfo.split("%");

            if (positions.length > 0 && !positions[ITEM].equals(""))
                setItemPositioning(positions[ITEM].split(";"));

            for (int i = 0; i < this.playerCount; i++)
                if (!(positions[SNAKE + i].equalsIgnoreCase("null")
                        || positions[SNAKE + i].equalsIgnoreCase("")))
                    setSnakePositioning(i, positions[SNAKE + i].split(";"));
                else
                    this.ALL_PLAYERS_SNAKE_POSITIONS[i].clear();

            for (int i = 0; i < this.playerCount; i++)
                if (this.ALL_PLAYERS_SNAKE_POSITIONS[i].size() < this.SNAKE_SHAPE_CHANGE)
                    this.snakeShaperSmall(i);
                else
                    this.snakeShaper(i);

            this.CONTROLLER.updateView();
        }
    }

    /**
     * Setting the item positioning in the server
     * @param itemPos the position of items as a list of strings
     */
    private void setItemPositioning(String[] itemPos)
    {
        this.resizeArrayList(itemPos.length, this.ALL_ITEM_POSITIONS);

        for (int i = 0; i < itemPos.length; i++)
        {
            if (this.ALL_ITEM_POSITIONS.get(i) == null)
                this.ALL_ITEM_POSITIONS.set(i, new Circle(ITEM_SIZE, ITEM_SIZE, ITEM_SIZE, Color.RED));

            String[] xAndY = itemPos[i].split(",");
            this.ALL_ITEM_POSITIONS.get(i).setTranslateX(Double.parseDouble(xAndY[X_POSITION]));
            this.ALL_ITEM_POSITIONS.get(i).setTranslateY(Double.parseDouble(xAndY[Y_POSITION]));
            setItemType(i, Integer.parseInt(xAndY[ITEM_TYPE]));
        }
    }

    /**
     * Set the image based on the image from the sprites package
     * @param index the index of that item
     * @param type the item type. [Food, Potion, Poison]
     */
    private void setItemType(int index, int type)
    {
        switch(type)
        {
            case FOOD:
                this.ALL_ITEM_POSITIONS.get(index).setFill(new ImagePattern(new Image("file:src/main/sprites/foodSprite.png")));
                break;
            case POTION:
                this.ALL_ITEM_POSITIONS.get(index).setFill(new ImagePattern(new Image("file:src/main/sprites/potionSprite.png")));
                break;
            case POISON:
                this.ALL_ITEM_POSITIONS.get(index).setFill(new ImagePattern(new Image("file:src/main/sprites/poisonSprite.png")));
                break;
        }
    }

    /**
     * Set the snake positioning
     * @param i the index of the snake
     * @param snakePos the snake position as a list of strings
     */
    private void setSnakePositioning(int i, String[] snakePos)
    {
        this.resizeArrayList(snakePos.length, this.ALL_PLAYERS_SNAKE_POSITIONS[i]);

        boolean colorSwap = true;

        for (int j = 0; j < snakePos.length; j++)
        {
            String[] xYAndRot = snakePos[j].split(",");
            if (this.ALL_PLAYERS_SNAKE_POSITIONS[i].get(j) == null)
                this.ALL_PLAYERS_SNAKE_POSITIONS[i].set(j, new Circle(15, 15, 15, getColor(i, colorSwap)));

            this.ALL_PLAYERS_SNAKE_POSITIONS[i].get(j).setTranslateX(Double.parseDouble(xYAndRot[X_POSITION]));
            this.ALL_PLAYERS_SNAKE_POSITIONS[i].get(j).setTranslateY(Double.parseDouble(xYAndRot[Y_POSITION]));
            this.ALL_PLAYERS_SNAKE_POSITIONS[i].get(j).setRotate(Double.parseDouble(xYAndRot[ROTATION]));

            colorSwap = !colorSwap;
        }
    }

    /**
     * Get the color of the snake. Different players have different snake colors
     * @param player the player as snake
     * @param swap the swap between colors
     * @return return the color of that snake player
     */
    private Color getColor(int player, boolean swap)
    {
        switch (player)
        {
            case PLAYER_ONE: return (swap) ? Color.DARKRED : Color.RED;
            case PLAYER_TWO: return (swap) ?  Color.DARKGREEN : Color.GREEN;
            case PLAYER_THREE: return (swap) ?  Color.DARKBLUE : Color.BLUE;
            case PLAYER_FOUR: return (swap) ?  Color.DARKGOLDENROD : Color.GOLDENROD;
            default:
                return Color.BLACK;
        }
    }

    /**
     * Sizes out the snake pieces to make it more snake like
     * this is for snakes that are less then 100 pieces long
     *
     * Breaks the snake down int 2 partitions and resizes the pieces to make it more snake looking
     *
     * @param snake the snake to be applied
     */
    private void snakeShaperSmall(int snake)
    {
        double normalSize = this.DEFAULT_SNAKE_PIECE_REFERENCE.getScaleX();

        int partitionSize = (this.ALL_PLAYERS_SNAKE_POSITIONS[snake].size() / 2);
        int partitionIndex = 0;

        double count = 0;
        double middleIndexSize = 0;

        for (int i = 0; i < this.ALL_PLAYERS_SNAKE_POSITIONS[snake].size(); i++)
        {
            switch (partitionIndex)
            {
                case 0:
                    if (count < partitionSize * .2)
                        middleIndexSize = this.setSnakePartScale(this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i),
                                (normalSize - (normalSize * (count/ partitionSize))));
                    else
                        this.setSnakePartScale(this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i),
                                middleIndexSize);
                    break;
                case 2:
                    if (count < partitionSize * .4)
                        this.setSnakePartScale(this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i),
                                middleIndexSize);
                    else
                        this.setSnakePartScale(this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i),
                                Math.max((normalSize - (normalSize * (1 - (count / partitionSize)))), middleIndexSize));
                    break;
                case 1:
                    this.setSnakePartScale(this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i),
                            Math.min((normalSize - normalSize * (count / (partitionSize))), middleIndexSize));
                    break;
                default:
                    this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i).setFill(Color.BLACK);
            }

            if (count++ == partitionSize)
            {
                count = 0;
                partitionIndex++;
            }

            if (i == 0)
                setHead(snake);
        }
    }

    /**
     * A shape change for larger snakes
     *
     * breaks the snake down into 9 partitions and sizes each one out to make the snake seem more... snake like
     *
     * @param snake the snake model to apply
     */
    private void snakeShaper(int snake)
    {
        double normalSize = this.DEFAULT_SNAKE_PIECE_REFERENCE.getScaleX();

        int partitionSize = (this.ALL_PLAYERS_SNAKE_POSITIONS[snake].size() / 9);
        int partitionIndex = 0;
        double count = 0;

        double middleIndexSize = 0;
        double middleSectionIndex = 0;
        double lastSectionIndex = 0;

        for (int i = 0; i < this.ALL_PLAYERS_SNAKE_POSITIONS[snake].size(); i++)
        {
            switch (partitionIndex)
            {
                case 0:
                    if (count < (partitionSize * .1))
                        middleIndexSize = this.setSnakePartScale(this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i),
                                (normalSize - (normalSize * (count/ partitionSize))));
                    else
                        this.setSnakePartScale(this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i),
                                middleIndexSize);
                    break;
                case 1:
                        this.setSnakePartScale(this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i),
                                Math.max((normalSize - (normalSize * (1 - (count / partitionSize)))), middleIndexSize));
                    break;
                case 2:
                case 3:
                case 4:
                    this.setSnakePartScale(this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i),
                            normalSize + (normalSize * (.1 * (middleSectionIndex++ / partitionSize))));
                    break;
                case 5:
                    this.setSnakePartScale(this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i),
                            normalSize + (normalSize * (.3 * (1 - count / partitionSize))));
                    break;
                case 6:
                case 7:
                case 8:
                    this.setSnakePartScale(this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i),
                            normalSize - normalSize * (lastSectionIndex++ / (partitionSize * 3)));
                    break;
                default:
                    this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(i).setFill(Color.BLACK);
            }

            if (count++ == partitionSize)
            {
                count = 0;
                partitionIndex++;
            }

            setHead(snake);
        }
    }

    /**
     * sets the head sprite for the snake
     *
     * @param snake - the snake to apply it to
     */
    private void setHead(int snake)
    {
        if (this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(0) != null)
        {
            this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(0).setScaleX(1.3);
            this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(0).setScaleY(1.3);
            this.ALL_PLAYERS_SNAKE_POSITIONS[snake].get(0).setFill(new ImagePattern(new Image("file:src/main/sprites/player" + (snake + 1) + "Head.png")));
        }
    }

    /**
     * Snake the snake part scale. This will make the snake more like a snake. Visual appealing.
     * @param part the parts of a snake
     * @param scale the scale to make
     * @return the scale of a snake parts
     */
    private double setSnakePartScale(Circle part, double scale)
    {
        part.setScaleX(scale);
        part.setScaleY(scale);
        return scale;
    }

    /**
     * Print the message about the server protocols
     * @param protocol the protocol interface string
     * @param onOff the protocol is set to on or off
     */
    public void sendDirection(String protocol, boolean onOff)
    {
        networkOut.println(protocol + " " + onOff);
    }

    /**
     * Resize the array list of circles (snake parts for the snake)
     * @param size the size
     * @param list the list of circles
     */
    public synchronized void resizeArrayList(int size, List<Circle> list)
    {
        if (list.size() < size)
            while (list.size() < size)
                list.add(null);
        else if (list.size() > size)
            while (list.size() > size)
                this.SCRAP_NODES.add(list.remove(0));
    }

    /**
     * Create a network host
     * @param port the port
     * @param players max number of players
     * @param width width of the game window
     * @param height height of the game window
     * @throws NumberFormatException exception for incorrect number format
     * @throws SnakeException the snake exceptions handling the errors
     */
    public void createNetwork(String port,String players,String width,String height) throws NumberFormatException, SnakeException
    {
        this.CONTROLLER.displayMenuMessage("Starting NetWork...");
        int thisPort = Integer.parseInt(port);
        this.playerCount = Integer.parseInt(players);
        this.width = Integer.parseInt(width);
        this.height = Integer.parseInt(height);

        checkPlayerCount();
        checkPort(thisPort);

        this.startHostServer(thisPort);
    }

    /**
     * Check number of players
     * @throws SnakeException if the number of players is invalid (0 snake or more than 4 players)
     */
    private void checkPlayerCount() throws SnakeException
    {
        if (this.playerCount < 1)
            throw new SnakeException("Must Be At Least One Player");
        else if (this.playerCount > 4)
            throw new SnakeException("4 Players Max");
    }

    /**
     * Checking if the port is valid
     * @param thisPort the port number
     * @throws SnakeException if the port is invalid
     */
    private void checkPort(int thisPort) throws SnakeException
    {
        if (thisPort < this.MIN_PORT)
            throw new SnakeException("Port Must Be Greater Than 1000");
        else if (thisPort > this.MAX_PORT)
            throw new SnakeException("Port Max 65535");
    }

    /**
     * Begin to host the server
     * @param thisPort the port number
     */
    private void startHostServer(int thisPort)
    {
        new Thread(() ->
        {
            try
            {
                new SnakeNetwork().init(thisPort, this.playerCount, this.width, this.height);
            }
            catch (IOException e)
            {
                this.CONTROLLER.displayMenuMessage("Server Failed To Start");
                System.exit(-1);
            }
        }).start();
    }

    /**
     * Setting up the model, call the controller as the server is hosted
     */
    public void startModel()
    {
        this.CONTROLLER.displayMenuMessage("Waiting For Other Players");
        this.CONTROLLER.getVIEW().getStartMenu().deactivateMenu();

        //game is about to start we can get rid of the menu now
        this.SCRAP_NODES.addAll(this.CONTROLLER.getVIEW().getStartMenu().getChildren());
        this.SCRAP_NODES.add(this.CONTROLLER.getVIEW().getStartMenu());

        new Thread(this::listener).start();
    }

    public ArrayList<Circle> getItemListPositions()
    {
        return ALL_ITEM_POSITIONS;
    }

    public ArrayList<Circle>[] getSnakeListPositions()
    {
        return ALL_PLAYERS_SNAKE_POSITIONS;
    }

    public ArrayList<Node> getScrapNodes()
    {
        return SCRAP_NODES;
    }

}