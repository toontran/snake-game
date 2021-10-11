package main;

import main.SnakeGameAssets.Snake;
import main.SnakeGameAssets.SnakeTail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * A test in game asset
 *
 * @author: Christopher Asbrock
 */
class GameAssetTest
{
    /** The epsilon of comparison double */
    private static final double EPSILON = 1.0E-12;

    /** The snake asset to be tested */
    private Snake testAsset;

    @BeforeEach
    void setUp()
    {
        this.testAsset = new Snake();
    }

    /** Test rotate left */
    @Test
    void rotateLEFT()
    {
        this.cordPrintOut();
        this.testAsset.setVelocity(1,0);

        assertEquals(this.testAsset.getTranslateX(), 0, EPSILON);
        this.testAsset.updateAsset();
        assertEquals(this.testAsset.getTranslateX(), 1, EPSILON);
        this.cordPrintOut();
        setUp();
        this.cordPrintOut();
        this.testAsset.setVelocity(2,0);
        this.testAsset.updateAsset();
        assertEquals(this.testAsset.getTranslateX(), 2, EPSILON);
        this.cordPrintOut();
        setUp();
        this.testAsset.setVelocity(1,0);
        for (int i = 0; i < 15; i++)
        {
            this.testAsset.rotate(1);
        }
        this.testAsset.updateAsset();
        assertTrue(this.testAsset.getTranslateX() > 0);
        assertTrue(this.testAsset.getTranslateY() > 0);
        assertEquals(this.testAsset.getTranslateX(),this.testAsset.getTranslateY(), EPSILON );
        this.cordPrintOut();
    }

    /** Test rotate right */
    @Test
    void rotateRight()
    {
        this.testAsset.setVelocity(1,0);
        for (int i = 0; i < 15; i++)
        {
            this.testAsset.rotate(-1);
        }
        this.testAsset.updateAsset();
        assertTrue(this.testAsset.getTranslateX() > 0);
        assertTrue(this.testAsset.getTranslateY() < 0);
        assertNotEquals(this.testAsset.getTranslateX(),this.testAsset.getTranslateY(), EPSILON );
        this.cordPrintOut();

        setUp();
        this.testAsset.setVelocity(0,1);
        this.testAsset.updateAsset();
        assertEquals(this.testAsset.getTranslateY(), 1, EPSILON);
    }

    /** Print the coordinates out */
    private void cordPrintOut()
    {
        System.out.println(this.testAsset.getTranslateX() + " " + this.testAsset.getTranslateY() + " " + this.testAsset.getRotate());
    }
}