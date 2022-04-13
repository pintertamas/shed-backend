package hu.bme.aut.shed.exception;

public class GameNotFoundException extends Exception {
    public GameNotFoundException() {
        super("game-not-found");
    }
}
