package hu.bme.aut.shed.exception;

public class AlreadyConnectedToOtherGameException extends Exception{
    public AlreadyConnectedToOtherGameException() {
        super("already-connected-to-other-game");
    }
}
