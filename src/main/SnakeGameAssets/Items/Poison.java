/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Christopher Asbrock
 * Section: 11am
 * Date: 4/30/20
 * Time: 7:35 AM
 *
 * Project: csci205_final_project_sp2020
 * Package: main.SnakeGameAssets
 * Class: Poison
 *
 * Description:
 *
 * ****************************************
 */
package
        main.SnakeGameAssets.Items;

import javafx.scene.paint.Color;

/**
 * Poison inherited from Item
 *
 * @author: Lawrence Li
 * @author: Christopher Asbrock
 * @author: Franco Perinotti
 */
public class Poison extends Item {

    public Poison(int x, Color color) {
        super(x,color,"Poison");
    }
}