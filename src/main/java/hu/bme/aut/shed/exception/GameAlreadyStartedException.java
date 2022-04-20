package hu.bme.aut.shed.exception;

public class GameAlreadyStartedException extends Exception {
    public GameAlreadyStartedException() {
        super("game-already-started");
    }
}