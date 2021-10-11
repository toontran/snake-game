/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Christopher Asbrock
 * Section: 11am
 * Date: 4/30/20
 * Time: 7:36 AM
 *
 * Project: csci205_final_project_sp2020
 * Package: main.SnakeGameAssets.Items
 * Class: Potion
 *
 * Description:
 *
 * ****************************************
 */
package
        main.SnakeGameAssets.Items;

import javafx.scene.paint.Color;

/**
 * Potion inherited from Item
 *
 * @author: Lawrence Li
 * @author: Christopher Asbrock
 * @author: Franco Perinotti
 */
public class Potion extends Item {

    public Potion(int x,  Color color) {
        super(x,color,"Potion");
    }
}