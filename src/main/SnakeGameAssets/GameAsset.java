/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Christopher Asbrock
 * Section: 11am
 * Date: 4/16/20
 * Time: 8:17 PM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: GameAsset
 *
 * Description:
 *
 * ****************************************
 */
package main.SnakeGameAssets;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * An abstract class that contains some variables every game asset will need
 * and an update to update the objects x, y, and rotations per tick
 *
 * @author Christopher Asbrock
 */
public abstract class GameAsset extends Circle
{
    /**the x velocity this object is moving in, 0 if stationary*/
    private double velocityX;
    /**the y velocity this object is moving in, 0 if stationary*/
    private double velocityY;

    private double speed;

    private boolean noLongerActive;

    /**
     * constructor, sets up a circle node that every object in the game will be based on
     *
     * @author Christopher Asbrock
     */
    public GameAsset(int size, Color color)
    {
        super(size, size, size, color);
        this.speed = 3;
        this.noLongerActive = false;
    }

    /**
     * swaps the asset to inactive so it can be removed from the board
     */
    public void deactivate()
    {
        this.noLongerActive = true;
    }

    /**
     * sets the x and y values in the obejct velocity
     *
     * @author Christopher Asbrock
     *
     * @param velocityX - a double representing the objects x velocity
     * @param velocityY - a double representing the objects y velocity
     */
    public void setVelocity(double velocityX, double velocityY)
    {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * run by the games animation timer 60 times a second to update the object position
     * based on its velocity
     *
     * @author Christopher Asbrock
     */
    public void updateAsset()
    {
        this.setTranslateX(this.getTranslateX() + velocityX);
        this.setTranslateY(this.getTranslateY() + velocityY);
    }

    /**
     * adjusts the velocity to be directed based on rotation
     *
     * @author Christopher Asbrock
     *
     * @param direction - RIGHT or LEFT
     */
    public void rotate(int direction)
    {
        this.setRotate(this.getRotate() + 3 * direction);
        this.setVelocity(Math.cos(Math.toRadians(getRotate())) * speed,Math.sin(Math.toRadians(getRotate())) * speed );
    }

    /**
     * A check for collisions with other Nodes in the scene
     *
     * @param otherAsset - the other game asset to look for bounds with
     * @return - true if they are colliding with one another, false otherwise
     */
    public boolean checkForCollision(Node otherAsset)
    {
//        return this.getBoundsInParent().intersects(otherAsset.getBoundsInParent());
        return (Math.abs(this.getTranslateX() - otherAsset.getTranslateX()) < otherAsset.getBoundsInParent().getWidth())
                && (Math.abs(this.getTranslateY() - otherAsset.getTranslateY()) < otherAsset.getBoundsInParent().getHeight());
        //return Shape.intersect(this, (Shape) otherAsset).getBoundsInParent().getWidth() > 0;
    }

    public boolean isNoLongerActive()
    {
        return noLongerActive;
    }
}