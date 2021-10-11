/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Christopher Asbrock
 * Section: 11am
 * Date: 5/2/20
 * Time: 11:37 AM
 *
 * Project: csci205_final_project_sp2020
 * Package: main.SnakeGameAssets
 * Class: SnakePlayerText
 *
 * Description:
 *
 * ****************************************
 */
package
        main.SnakeGameAssets;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The snake player text for displaying in the game window
 *
 * @author: Christopher Asbrock
 */
public class SnakePlayerText extends GridPane
{
    /** The player number label */
    private Label playerNumber;

    /** The color of the player shown as rectangle shape */
    private Rectangle playerColor;

    /** Constructor of the snake player text */
    public SnakePlayerText()
    {
        super();
        this.playerNumber = new Label();
        this.playerColor = new Rectangle(20,20, Color.GRAY);

        this.add(playerNumber, 0,0);
        this.add(playerColor, 1 , 0);
    }

    /**
     * Setting up the player field in the game window
     * @param text the text displaying "Player #"
     * @param color the color that will be displayed
     */
    public void setPlayerField(String text, Color color)
    {
        this.playerNumber.setText(text);
        this.playerColor.setFill(color);
    }
}