package hu.bme.aut.shed.model;

public enum TableCardState {
    PICK("Pick"),
    THROW("Throw");

    private final String name;

    TableCardState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static TableCardState fromName(String name) throws IllegalArgumentException {
        switch (name) {
            case "Pick":
                return TableCardState.PICK;

            case "Throw":
                return TableCardState.THROW;

            default:
                throw new IllegalArgumentException("TableCardState [" + name
                        + "] not supported.");
        }
    }
}
