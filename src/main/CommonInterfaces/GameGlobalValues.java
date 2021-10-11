package main.CommonInterfaces;

/**
 * global values and indexes commonly used throughout the game
 * can be implemented by any class that needs them
 *
 * @author Christopher Asbrock
 */
public interface GameGlobalValues
{
    /**Common index position representing the x position*/
    int X_POSITION = 0;

    /**Common index position representing the y position*/
    int Y_POSITION = 1;

    /**Common index position representing the rotate position*/
    int ROTATION = 2;

    /**Common index position representing the item type*/
    int ITEM_TYPE = 2;

    /**Common index position representing the snake*/
    int SNAKE = 1;

    /**Common index position representing the items*/
    int ITEM = 0;

    /**case representing a food item*/
    int FOOD = 0;

    /**case representing a potion item*/
    int POTION = 1;

    /**case representing a poison item*/
    int POISON = 2;

    /**the size of an item*/
        int ITEM_SIZE = 15;

    /**the size of a snake piece*/
    int SNAKE_PIECE_SIZE = 15;

    /**case value for player one*/
    int PLAYER_ONE = 0;

    /**case value for player 2*/
    int PLAYER_TWO = 1;

    /**case value for player 3*/
    int PLAYER_THREE = 2;

    /**case value for player 4*/
    int PLAYER_FOUR = 3;

    /**the max items allowed to spawn in game*/
    int MAX_ITEMS = 30;

    /**the max pieces each snake can hold*/
    int MAX_SNAKE_PIECES = 2000;

    /**the max amount of players allowed*/
    int MAX_PLAYERS = 4;

    /**the thickness of all walls in game*/
    int WALL_THICKNESS = 30;

    /**int multiplier for a right turn*/
    int RIGHT = 1;

    /**int multiplier for a left turn*/
    int LEFT = -1;

}
