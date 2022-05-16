package hu.bme.aut.shed.exception;

public class TableCardNotFoundException extends Exception {
    public TableCardNotFoundException() {
        super("table-card-not-found");
    }
}
