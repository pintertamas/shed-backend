package hu.bme.aut.shed.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("Could not find user with the given username!");
    }
}
