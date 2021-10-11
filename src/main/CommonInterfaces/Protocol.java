package main.CommonInterfaces;

/**
 * protocol shared between the client and the server
 * each will implement this so the protocol is standardized
 *
 * @author: Christopher Asbrock
 */
public interface Protocol
{
    String START_GAME = "startGame";
    String DATA = "data";
    String TURN_LEFT = "turnLeft";
    String TURN_RIGHT = "turnRight";
    String ERROR = "error";
    String END_GAME = "endGame";
    String MESSAGE = "message";
    String PLAYER_INFO = "playerInfo";

    default String getProtocol(String input)
    {
        return input.split(" ")[0];
    }

    default String getMessage(String protocol, String input)
    {
        return input.substring(protocol.length() + 1);
    }
}
