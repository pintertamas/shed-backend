package hu.bme.aut.shed.exception;

public class NoPlayersInTheRoomException extends Exception {
    public NoPlayersInTheRoomException() {
        super("no-players-in-the-room");
    }
}
