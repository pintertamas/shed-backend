package hu.bme.aut.shed.model;

public enum PlayerCardState {
    HAND("Hand"),
    VISIBLE("Visible"),
    INVISIBLE("Invisible");

    private final String name;

    PlayerCardState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static PlayerCardState fromName(String name) throws IllegalArgumentException {
        switch (name) {
            case "Hand":
                return PlayerCardState.HAND;

            case "Visible":
                return PlayerCardState.VISIBLE;

            case "Invisible":
                return PlayerCardState.INVISIBLE;

            default:
                throw new IllegalArgumentException("PlayerCardState [" + name
                        + "] not supported.");
        }
    }
}
