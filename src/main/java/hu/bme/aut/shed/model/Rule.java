package hu.bme.aut.shed.model;

public enum Rule {
    NONE("None"),
    INVISIBLE("Invisible"),
    JOLLY_JOKER("Jolly_Joker"),
    BURNER("Burner"),
    REDUCER("Reducer");

    private final String name;

    Rule(String name){this.name = name;}

    public String getName() {
        return name;
    }

    public static Rule fromName(String name) throws IllegalArgumentException {
        switch (name) {

            case "None":
                return Rule.NONE;

            case "Invisible":
                return Rule.INVISIBLE;

            case "Jolly_Joker":
                return Rule.JOLLY_JOKER;

            case "Burner":
                return Rule.BURNER;

            case "Reducer":
                return Rule.REDUCER;

            default:
                throw new IllegalArgumentException("Rule [" + name
                        + "] not supported.");
        }
    }
}
