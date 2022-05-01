package hu.bme.aut.shed.model;

public enum Shape {
    CLUBS("Clubs"),
    DIAMONDS("Diamonds"),
    HEART("Heart"),
    SPADES("Spades");

    private final String name;

    Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Shape fromName(String name) throws IllegalArgumentException {
        switch (name) {
            case "Clubs":
                return Shape.CLUBS;

            case "Diamonds":
                return Shape.DIAMONDS;

            case "Heart":
                return Shape.HEART;

            case "Spades":
                return Shape.SPADES;

            default:
                throw new IllegalArgumentException("PlayerCardState [" + name
                        + "] not supported.");
        }
    }
}
