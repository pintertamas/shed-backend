package hu.bme.aut.shed.exception;

public class CantThrowCardException extends Exception {
    public CantThrowCardException() {
        super("can't-throw-card");
    }
}
