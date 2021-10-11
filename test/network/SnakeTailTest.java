package network;

import main.SnakeGameAssets.Snake;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A snake tail test to test the snake can rotate and move properly
 *
 * @author: Christopher Asbrock
 */
class SnakeTailTest {

    /** Update the snake tail test */
    @Test
    void updateAsset()
    {
        Snake snake = new Snake();
        snake.setTranslateX(50);
        snake.setTranslateY(60);
        snake.setRotate(70);

        snake.addTail();
        //once to set up the previous
        snake.getSnakeTails().get(0).updateAsset();

        assertEquals(50, snake.getSnakeTails().get(0).getPrevX());
        assertEquals(60, snake.getSnakeTails().get(0).getPrevY());
        assertEquals(70, snake.getSnakeTails().get(0).getPrevAngle());


        //again to set the previous
        snake.getSnakeTails().get(0).updateAsset();

        assertEquals(50, snake.getSnakeTails().get(0).getTranslateX());
        assertEquals(60, snake.getSnakeTails().get(0).getTranslateY());
        assertEquals(70, snake.getSnakeTails().get(0).getRotate());
    }
}