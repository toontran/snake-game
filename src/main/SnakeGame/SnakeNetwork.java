package main.SnakeGame;

import javafx.scene.paint.Color;
import main.CommonInterfaces.GameGlobalValues;
import main.CommonInterfaces.Protocol;
import main.SnakeGameAssets.*;
import main.SnakeGameAssets.Items.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * the main view for the user,
 *
 * Shows a game of snake happening in real time,
 * updates movements 60 times a second
 * player can change the angle of the snake by using their arrow keys
 *
 * @author Christopher Asbrock
 * @author Franco Perinotti
 */
public class SnakeNetwork implements Protocol, GameGlobalValues
{
    /**base length for the snake, it will start this size and will not be able to go less then this*/
    private final int INIT_SNAKE_SIZE = 15;
    /**Length of snake added for potion*/
    private final int POTION_LENGTH = 50;
    /**Length of snake added for food*/
    private final int FOOD_LENGTH = 5;
    /**Length of snake deleted for poison*/
    private final int POISON_LENGTH = 100;

    /** The socket list */
    private Socket[] socket;
    /** The server */
    private ServerSocket server;

    /** Scanner in for the network information */
    private Scanner [] networkIn;
    /** PrintStream out for the network information */
    private PrintStream [] networkOut;

    /** Boolean to check if a game is on */
    private boolean gameIsOn = false;

    /**fixed width of the window*/
    private int WIDTH;
    /**fixed height of the window*/
    private int HEIGHT;

    /**random number generator*/
    private Random randomNumGen;

    /**a list containing all items currently in the pane*/
    private ArrayList<GameAsset> listOfItems;

    /**the player character*/
    private GameAsset[] player;
    /**trigger for a right turn*/
    private boolean[] turnRight;
    /**trigger for a left turn*/
    private boolean[] turnLeft;

    /**Number of player*/
    private int numOfPlayer;
    /**The number representing the winner*/
    private int winner;

    /**
     * a trash collector that collects all assets removed from the scene to be removed
     * from their list at the end of the update
     * */
    private ArrayList<GameAsset> inactiveFoodNodes;

    /** Construction of the network */
    public SnakeNetwork()
    {
        this.winner = -1;
    }

    /**
     * initializes lists of items and the pane to a certain size
     */
    public void init(int port, int players, int width, int height) throws IOException
    {
        this.numOfPlayer = players;
        this.WIDTH = width;
        this.HEIGHT = height;

        initGameVariables();
        initAndPlaceSnakes();
        initAndConnectNetwork(port);

        this.start();
    }

    /**
     * The initialization and connection to the network
     * @param port the port
     * @throws IOException if the read/write run into problems
     */
    private void initAndConnectNetwork(int port) throws IOException
    {
        server = new ServerSocket(port);

        for (int i = 0; i < this.numOfPlayer; i++)
            this.setUpNetworkConnection(i);
    }

    /**
     * Initialization and placement of all snakes
     */
    private void initAndPlaceSnakes()
    {
        for (int i = 0; i < this.numOfPlayer; i++)
        {
            this.player[i] = new Snake();
            this.player[i].setVelocity(0, 0);
            this.player[i].setRotate(90 * i + 45);
            for (int j = 0; j < INIT_SNAKE_SIZE; j++)
                ((Snake) this.player[i]).addTail();

            SnakeUtil.addToGame(this.player[i],
                    WIDTH / 2.0 + 50 * Math.cos(Math.toRadians(90 * i + 45)),
                    HEIGHT / 2.0 + 50 * Math.sin(Math.toRadians(90 * i + 45)));
        }
    }

    /**
     * Initialization of game instance variables that will be used in the running of the game
     */
    private void initGameVariables()
    {
        this.randomNumGen = new Random();
        this.listOfItems = new ArrayList<>();
        this.inactiveFoodNodes = new ArrayList<>();
        this.player = new GameAsset[this.numOfPlayer];
        this.socket = new Socket[this.numOfPlayer];
        this.networkIn = new Scanner[this.numOfPlayer];
        this.networkOut = new PrintStream[this.numOfPlayer];
        this.turnLeft = new boolean[this.numOfPlayer];
        this.turnRight = new boolean[this.numOfPlayer];
    }

    /**
     * Set up the network connection for the client (player)
     * @param player the player representation
     */
    private void setUpNetworkConnection(int player)
    {
        try
        {
            System.out.println("Waiting on Player " + (player + 1));
            socket[player] = server.accept();
            System.out.println("Player " + (player + 1) + " connected");

            networkOut[player] = new PrintStream(socket[player].getOutputStream());
            networkIn[player] = new Scanner(socket[player].getInputStream());

            this.networkOut[player].println(PLAYER_INFO + " " + (player + 1));
            new Thread(() -> networkListener(player)).start();

            if (player == this.numOfPlayer - 1)
            {
                this.gameIsOn = true;
                this.pushNetwork(START_GAME, String.valueOf(this.numOfPlayer));
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * The network listener for the player
     * @param player the player representation
     */
    private void networkListener(int player)
    {
        while (this.networkIn[player].hasNextLine())
        {
            String input = this.networkIn[player].nextLine();
            String protocol = getProtocol(input);
            String message = getMessage(protocol, input);

            switch (protocol) {
                case TURN_LEFT:
                    this.turnLeft[player] = message.equalsIgnoreCase("true");
                    break;
                case TURN_RIGHT:
                    this.turnRight[player] = message.equalsIgnoreCase("true");
                    break;
            }
        }

        //if the player array is not null then they dced
        if (this.player != null)
            this.player[player] = null;
        try
        {
            this.networkIn[player].close();
            this.networkOut[player].close();
            this.socket[player].close();
        }
        catch (IOException e)
        {
            //required by socket.close();
        }

        System.out.println("NetWork Done, Player " + (player + 1));
    }

    /**
     * the main driver that updates the screen 60 times a second.
     * food actions will only happen when player is alive
     */
    private void updateDriver()
    {
        if (gameIsOn)
        {
            if (player != null)
            {
                foodPlacer();
                itemCleanUp();
                handlePlayer();
            }

            sendNetworkInfo();

        }
    }

    /**
     * Send the network info
     */
    private void sendNetworkInfo()
    {
        StringBuilder allInfo = new StringBuilder();
        buildItemStringForNetwork(allInfo);
        buildSnakeStringForNetwork(allInfo);
        pushNetwork(DATA, allInfo.toString());
    }

    /**
     * Build the snake string information for the network
     * @param allInfo the information as string builder. Populate the information to be ready to send
     */
    private void buildSnakeStringForNetwork(StringBuilder allInfo)
    {
        for (GameAsset gameAsset : this.player)
        {
            allInfo.append("%");

            if (gameAsset != null)
                for (int j = 0; j < ((Snake) gameAsset).getSnakeTails().size(); j++)
                    allInfo.append(((Snake) gameAsset).getSnakeTails().get(j).getTranslateX())
                            .append(",").append(((Snake) gameAsset).getSnakeTails().get(j).getTranslateY())
                            .append(",").append(((Snake) gameAsset).getSnakeTails().get(j).getRotate())
                            .append((((Snake) gameAsset).getSnakeTails().size() - 1 != j) ? ";" : "");
            else
                allInfo.append("null");
        }
    }

    /**
     * Build the item string information for the network
     * @param allInfo the information as string builder. Populate the information to be ready to send
     */
    private void buildItemStringForNetwork(StringBuilder allInfo)
    {
        for (int i = 0; i < this.listOfItems.size(); i++)
        {
            allInfo.append(this.listOfItems.get(i).getTranslateX()).append(",")
                    .append(this.listOfItems.get(i).getTranslateY()).append(",");
            allInfo.append(getItemType(i));
            allInfo.append((i != this.listOfItems.size() - 1) ? ";" : "");
        }
    }

    /**
     * Get the type of an item
     * @param i the index of the list
     * @return the type as class name
     */
    private int getItemType(int i)
    {
        if (this.listOfItems.get(i) instanceof Poison)
            return POISON;
        else if (this.listOfItems.get(i) instanceof Potion)
            return POTION;
        else
            return FOOD;
    }

    /**
     * Push all information to the network
     * @param protocol the protocol interface for messages
     * @param info the information to push to network
     */
    private void pushNetwork(String protocol, String info)
    {
        for (int i = 0; i < this.numOfPlayer; i++)
            this.networkOut[i].println(protocol + " " + info);
    }

    /**
     * handles the players status and interaction with other nodes
     */
    private void handlePlayer()
    {
        for (int i = 0; i < this.player.length; i++)
        {
            if (this.player[i] != null)
            {
                handlePlayerTurning(i);
                handlePlayerItemCollision(i);
                handlePlayerOtherPlayerCollision(i);
                handlePlayerWallCollision(i);
                handlePlayerSelfCollision(i);
            }
        }

        updatePlayer();
    }

    /**
     * handle if a snake collided with another snake's body
     * @param thisPlayer the player representation
     */
    private void handlePlayerOtherPlayerCollision(int thisPlayer)
    {
        for (int i = 0 ; i < this.player.length; i++)
            if (this.player[i] != null && this.player[thisPlayer] != null)
                for (GameAsset tail : ((Snake) this.player[i]).getSnakeTails())
                    if (i != thisPlayer && this.player[thisPlayer].checkForCollision(tail))
                    {
                        this.pushNetwork(MESSAGE,"Player " + (thisPlayer +1) + " Collided With Player " + (i + 1));
                        System.out.println("Hit Player " + (i + 1));
                        this.player[thisPlayer].deactivate();
                    }
    }

    /**
     * handle if a snake is collided with itself
     * @param i the index of the player representation
     */
    private void handlePlayerSelfCollision(int i)
    {
        int j = 0;
        for (SnakeTail tail : ((Snake) this.player[i]).getSnakeTails()) {
            if (j++ < 100)
                continue;       // First several SnakeTails always collide with the head

            if (this.player[i].checkForCollision(tail))
            {
                this.pushNetwork(MESSAGE,"Player " + (i+1) + " Collided With Their Own Tail");
                System.out.printf("Collided with tail number %d\n", tail.id);
                player[i].deactivate();
            }
        }
    }

    /**
     * handle if a snake is collided with the wall
     * @param i the index of the player representation
     */
    private void handlePlayerWallCollision(int i)
    {
        assert this.player[i] != null;
        if (this.player[i].getTranslateX() < WALL_THICKNESS || this.player[i].getTranslateX() > this.WIDTH - WALL_THICKNESS * 2)
        {
            this.pushNetwork(MESSAGE,"Player " + (i+1) + " Hit A Wall");
            System.out.println("PLayer" + (i + 1) + "Hit X Wall");
            player[i].deactivate();
        }

        if (this.player[i].getTranslateY() < WALL_THICKNESS || this.player[i].getTranslateY() > this.HEIGHT - WALL_THICKNESS * 2)
        {
            this.pushNetwork(MESSAGE,"Player " + (i+1) + " Hit A Wall");
            System.out.println("Player" + (i+1) + "Hit Y Wall");
            player[i].deactivate();
        }
    }

    /**
     * handle if a snake is collided with an item (the snake eats something)
     * @param i the index of the player representation
     */
    private void handlePlayerItemCollision(int i)
    {
        for (GameAsset item : this.listOfItems)
            if (this.player[i] != null && this.player[i].checkForCollision(item))
            {
                handleItemCollision(item, (Snake) this.player[i]);
                this.inactiveFoodNodes.add(item);
            }
    }

    /**
     * handle if a snake is turning direction
     * @param i the index of the player representation
     */
    private void handlePlayerTurning(int i)
    {
        if (this.turnRight[i])
            this.player[i].rotate(RIGHT);
        else if (this.turnLeft[i])
            this.player[i].rotate(LEFT);
    }

    /**
     * updates the player based on whether they are active or not,
     * if they are it will update them and their pieces,
     * if not it will remove them from the game
     */
    private void updatePlayer()
    {
        for (int i = 0; i < this.player.length; i++)
        {
            if (this.player[i] != null)
                if (this.player[i].isNoLongerActive())
                {
                    ((Snake) this.player[i]).getSnakeTails().clear();
                    this.player[i] = null;
                }
                else
                    this.player[i].updateAsset();
        }
    }

    /**
     * when colliding with an Item this determines the instance type and adds or removes pieces of the snake
     * accordingly
     * 
     * @param item - the idem being collided with
     */
    private void handleItemCollision(GameAsset item, Snake currPlayer)
    {
        if (item instanceof Potion)
            for (int i = 0; i < this.POTION_LENGTH; i++)
                currPlayer.addTail();
        else if (item instanceof Poison)
            for (int i = 0; i < this.POISON_LENGTH; i++)
                currPlayer.removeTail(this.INIT_SNAKE_SIZE);
        else
            for (int i = 0; i < this.FOOD_LENGTH; i++)
                currPlayer.addTail();

    }

    /**
     * Will remove any inactive items from the list
     */
    private void itemCleanUp()
    {
        listOfItems.removeAll(inactiveFoodNodes);
        inactiveFoodNodes.clear();
    }

    /**
     * called per tick, creates a randomized number, if the value is under a specific amount it will
     * create a food item randomly on the field
     */
    private void foodPlacer()
    {
        if (this.listOfItems.size() < MAX_ITEMS)
        {
            int randomInt = randomNumGen.nextInt(2000);
            if (randomInt < 25)
            {
                Item newItem;
                switch (randomInt)
                {
                    case POTION:
                        newItem = new Potion(ITEM_SIZE, Color.GOLD);
                        break;
                    case POISON:
                        newItem = new Poison(ITEM_SIZE, Color.GREEN);
                        break;
                    default:
                        newItem = new Food(ITEM_SIZE, Color.BLUE);
                }

                this.listOfItems.add(newItem);
                SnakeUtil.addToGame(newItem,
                        WALL_THICKNESS * 2 + (this.randomNumGen.nextInt(WIDTH- 150)),
                        WALL_THICKNESS * 2 + (this.randomNumGen.nextInt(HEIGHT- 150)));
            }
        }
    }

    /**
     * start method
     * just started the tick timer for now, but there might be something else
     * that needs to be started later.
     */
    public void start()
    {
        this.tickTimer();
    }

    /**
     * the final check for players, if everyone is null the game is over, it will null the whole array
     */
    public void checkPlayersEndGame()
    {
        int count = 0;
        this.winner = -1;

        for (int i = 0; i < this.numOfPlayer; i++)
            if (this.player[i] == null)
                count++;
            else
                this.winner = i;

        if (this.numOfPlayer > 1)
        {
            if (count == this.numOfPlayer - 1)
                this.player = null;
        }
        else if (count == this.numOfPlayer)
            this.player = null;
    }

    /**
     * The in-game ticking timer of the snake game
     */
    private void tickTimer()
    {
        long now = System.currentTimeMillis();

        System.out.println("start");

        while (player != null)
        {
            if ((System.currentTimeMillis() - now) > 30) {
                now = System.currentTimeMillis();

                updateDriver();
            }

            this.checkPlayersEndGame();
        }

        if (this.winner == -1)
            this.pushNetwork(END_GAME, "Game Over");
        else
            this.pushNetwork(END_GAME, "Winner is Player " + (this.winner + 1) + "!!!");

        System.out.println("GameOver");
    }
}

