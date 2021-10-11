/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Christopher Asbrock
 * Section: 11am
 * Date: 4/16/20
 * Time: 8:06 PM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: SnakeUtil
 *
 * Description:
 *
 * ****************************************
 */
package main.SnakeGameAssets;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.Collections;
import java.util.List;

/**
 * some utilities that any GUI instance of the snake game will need
 *
 * @author Christopher Asbrock
 */
public class SnakeUtil
{
    /**
     * Will position and add an item to a root,
     *
     * for use by any GUI instance of the snake game, view model, or server
     *
     * @author Christopher Asbrock
     */
    public static void addToGame(Node gameAsset, double posX, double posY)
    {
        gameAsset.setTranslateX(posX);
        gameAsset.setTranslateY(posY);
    }
}