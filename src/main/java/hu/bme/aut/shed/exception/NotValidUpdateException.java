package hu.bme.aut.shed.exception;

public class NotValidUpdateException extends Exception{
    public NotValidUpdateException() {
        super("Could not find user with the given ID or the ID not matching in the Body with the given ID");
    }
}
