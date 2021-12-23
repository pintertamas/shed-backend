package hu.bme.aut.shed.exception;

public class LobbyIsFullException extends Exception{
    public LobbyIsFullException() {
        super("The lobby is full, no more players can join.");
    }
}
