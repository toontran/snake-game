package network;

import javafx.scene.shape.Circle;
import main.SnakeGameAssets.Snake;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test in game asset to check for collisions
 *
 * @author: Christopher Asbrock
 */
class GameAssetTest {

    /** Check for collision */
    @Test
    void checkForCollision()
    {
        Snake snake = new Snake();
        snake.setTranslateX(50);
        snake.setTranslateY(50);

        Circle testNode = new Circle(15,15,15);
        testNode.setTranslateX(0);
        testNode.setTranslateY(0);

        assertFalse(snake.checkForCollision(testNode));

        testNode.setTranslateX(50);
        testNode.setTranslateY(50);
        assertTrue(snake.checkForCollision(testNode));

        //a bit overkill here, or not
        //pretty much scans the entire area around the node and makes sure if the player
        //is anywhere in a zone of 30 spaces that it is considered colliding with this object

        for (int i = 0; i < 30; i++)
        {
            testNode.setTranslateX(50 + i);
            testNode.setTranslateY(50 + i);
            assertTrue(snake.checkForCollision(testNode));

            testNode.setTranslateX(50 + i);
            testNode.setTranslateY(50 - i);
            assertTrue(snake.checkForCollision(testNode));

            testNode.setTranslateX(50 - i);
            testNode.setTranslateY(50 + i);
            assertTrue(snake.checkForCollision(testNode));

            testNode.setTranslateX(50 - i);
            testNode.setTranslateY(50 - i);
            assertTrue(snake.checkForCollision(testNode));
        }
    }
}