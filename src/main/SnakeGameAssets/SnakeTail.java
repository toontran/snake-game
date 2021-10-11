/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Tung Tran
 * Section: 8am
 * Date: 4/18/2020
 * Time: 9:59 PM
 *
 * Project: csci205_Final_Project_SP2020
 * Package: main
 * Class: SnakeTail
 *
 * Description:
 *
 * ****************************************
 */
package main.SnakeGameAssets;


import javafx.scene.paint.Color;

/**
 * A snake tail class inherited from GameAsset
 *
 * @author: Christopher Asbrock
 * @author: Tung Tran
 * @author: Lawrence Li
 */
public class SnakeTail extends GameAsset {

    /** For debugging */
    public int id;

    /**
     * The parent tail which represents the next state of the tail,
     *      creating the tail illusion
     */
    private GameAsset parent;

    /** Parent's state */
    private double prevX;
    private double prevY;
    private double prevAngle;

    public SnakeTail(GameAsset parent) {
        super(15,  Color.RED);
        this.parent = parent;
        if (parent != null)
        {
            prevX = parent.getTranslateX();
            prevY = parent.getTranslateY();
            this.setTranslateX(prevX);
            this.setTranslateY(prevY);
        }
    }

    public GameAsset getParentNode() {
        return parent;
    }

    public double getPrevX() {
        return prevX;
    }

    public double getPrevY() {
        return prevY;
    }

    public double getPrevAngle() {
        return prevAngle;
    }

    /**
     * Updates the position of the SnakeTail.
     * After updating the position with current velocity,
     * update the velocity
     */
    @Override
    public void updateAsset() {
        this.setTranslateX(prevX);
        this.setTranslateY(prevY);
        this.setRotate(prevAngle);
        prevX = parent.getTranslateX();
        prevY = parent.getTranslateY();
        prevAngle = parent.getRotate();
    }
}