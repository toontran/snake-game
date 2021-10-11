/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Christopher Asbrock
 * Section: 11am
 * Date: 4/29/20
 * Time: 6:20 AM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: SnakeMenu
 *
 * Description:
 *
 * ****************************************
 */
package main.SnakeGameAssets;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import main.SnakeGame.SnakeGameController;

/** The main menu of the snake called from the snake view */
public class SnakeMenu extends GridPane
{
    /** The join button */
    private Button clientButton;
    /** The host button */
    private Button hostButton;
    /** The host text */
    private TextField hostText;
    /** The port text */
    private TextField portText;
    /** The host port field */
    private TextField hostPortField;
    /** The number of player field */
    private TextField numOfPlayerField;
    /** The setting height of the window field */
    private TextField setHeightField;
    /** The setting width of the window field */
    private TextField setWidthField;
    /** Message display field */
    private TextField displayMessage;

    /**
     * initializes and add the menu items to the grid menu
     * general layout (so the grid coordinates are not as confusing to look at)
     *
     *  TOP: join game menu, with host and port field
     *  MIDDLE: host game menu, with port, number of players, width and height fields
     *  BOTTOM: a message display, for errors or updates
     *
     * @author: Christopher Asbrock
     */
    public SnakeMenu()
    {
        super();
        setDisplayProperties();
        initMenusFields();
        setUpJoinSection();
        setUpHostSection();

        this.add(this.displayMessage, 1, 13,4,1);
    }

    /**
     * Initialization of the menu fields
     */
    private void initMenusFields()
    {
        this.hostText = new TextField();
        this.portText = new TextField();
        this.hostPortField = new TextField();
        this.numOfPlayerField = new TextField();
        this.setHeightField = new TextField();
        this.setWidthField = new TextField();
        this.clientButton = new Button();
        this.hostButton = new Button();
        this.displayMessage = new TextField();

        this.setUpDefaultAttributes();
        this.setDefaultValues("1", "800", "600");
    }

    /**
     * Setting up the default attributes for the menu
     */
    private void setUpDefaultAttributes()
    {
        this.clientButton.setText("JOIN");
        this.hostButton.setText("HOST");

        this.displayMessage.setMouseTransparent(true);
        this.displayMessage.setEditable(false);
        this.displayMessage.setStyle("-fx-background-color: #c8c8c8; -fx-text-fill: red");
        this.displayMessage.setAlignment(Pos.CENTER);
    }

    /**
     * Setting up the default values for the field
     * @param players the number of players
     * @param width the width of game window
     * @param height the height of game window
     */
    private void setDefaultValues(String players, String width, String height)
    {
        this.numOfPlayerField.setText(players);
        this.setWidthField.setText(width);
        this.setHeightField.setText(height);
    }

    /**
     * Set the display properties for every fields and buttons and message boxes
     */
    private void setDisplayProperties()
    {
        this.setPrefSize(400,300);
        //this.startMenu.setGridLinesVisible(true);
        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
    }

    /**
     * Setting up the host section of the menu
     */
    private void setUpHostSection()
    {
        this.add(new Label("HOST GAME"), 2, 6,3,1);
        this.add(new Label("PORT:"), 1,7);
        this.add(this.hostPortField, 2,7);
        this.add(this.hostButton,3,7,1,2);
        this.add(new Label("PLAYERS:"), 1,8);
        this.add(this.numOfPlayerField,2,8);

        //no time to set this one up, but it is set up so just taking the options out will not effect the game

        //this.add(new Label("PANE HEIGHT:"), 1,9);
        //this.add(this.setHeightField,2,9);
        //this.add(new Label("PANE WIDTH:"), 1,10);
        //this.add(this.setWidthField,2,10);
    }

    /**
     * Setting up the join section of the menu
     */
    private void setUpJoinSection()
    {
        this.add(new Label("JOIN GAME"), 2, 1,3,1);
        this.add(new Label("HOST:"), 1,2);
        this.add(this.hostText, 2,2);
        this.add(new Label("PORT:"), 1,3);
        this.add(this.portText,2,3);
        this.add(this.clientButton,3,2,1,2);
    }

    /**
     * Deactivate the menu if the game starts
     */
    public void deactivateMenu()
    {
        this.getChildren().forEach((node)->node.setDisable(true));
        this.displayMessage.setDisable(false);
    }

    /**
     * Activate the menu
     */
    public void activateMenu()
    {
        this.getChildren().forEach((node)->node.setDisable(false));
    }

    /**
     * Handling mouse click events on the join button
     * @param event the event handler
     */
    public void onClientButtonClick(EventHandler<ActionEvent> event)
    {
        this.clientButton.setOnAction(event);
    }

    /**
     * Handling mouse click events on the host button
     * @param event the event handler
     */
    public void onHostButtonClick(EventHandler<ActionEvent> event)
    {
        this.hostButton.setOnAction(event);
    }

    /**
     * Handling the start action of host via the controller
     * @param controller the snake game controller
     */
    public void hostStartAction(SnakeGameController controller)
    {
        controller.setHost(this.hostPortField.getText(),
                this.numOfPlayerField.getText(),
                this.setWidthField.getText(),
                this.setHeightField.getText());
    }

    /**
     * Handling the join action of players via the controller
     * @param controller the snake game controller
     */
    public void joinStartAction(SnakeGameController controller)
    {
        controller.setJoin(this.hostText.getText(), this.portText.getText());
    }

    public TextField getDisplayMessage()
    {
        return displayMessage;
    }
}