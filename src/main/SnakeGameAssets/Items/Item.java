/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Lawrence Li
 * Section: 8am
 * Date: 4/16/20
 * Time: 12:01 PM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: Item
 *
 * Description:
 * Item class for the snake game
 * ****************************************
 */
package
        main.SnakeGameAssets.Items;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import main.SnakeGameAssets.GameAsset;

/**
 * A class that represent the items in the snake game
 *
 *
 * @author: Lawrence Li
 * @author: Christopher Asbrock
 * @author: Franco Perinotti
 */
public abstract class Item extends GameAsset {

    /** Location as 2D coordinates */
    Point2D location;

    /** Name of the item */
    String name;

    public Item(int x, Color color, String name) {
        super(x,color);
        this.location = location;
        this.name = name;
    }

    public Point2D getLocation() { return location; }
    public String getName() { return name; }
}



