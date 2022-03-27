package hu.bme.aut.shed.exception;

import hu.bme.aut.shed.model.User;

public class UserAlreadyExistsException extends Exception {
    User user;

    public UserAlreadyExistsException(User user) {
        super("User already exists: " + user);
        this.user = user;
    }

    public User getExistingUser() {
        return this.user;
    }
}
