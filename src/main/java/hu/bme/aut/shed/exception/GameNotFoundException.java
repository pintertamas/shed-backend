package hu.bme.aut.shed.exception;

public class GameNotFoundException extends Exception {
    public GameNotFoundException() {
        super("Could not find game with the provided ID!");
    }
}
