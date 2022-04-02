package hu.bme.aut.shed.model;

public enum GameStatus {
    NEW("N"),
    IN_PROGRESS("P"),
    FINISHED("F");

    private String shortName;

    private GameStatus(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static GameStatus fromShortName(String shortName) {
        switch (shortName) {
            case "N":
                return GameStatus.NEW;

            case "P":
                return GameStatus.IN_PROGRESS;

            case "F":
                return GameStatus.FINISHED;
            default:
                throw new IllegalArgumentException("ShortName [" + shortName
                        + "] not supported.");
        }
    }

}
