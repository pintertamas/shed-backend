package hu.bme.aut.shed.model;

public enum GameStatus {
    NEW("New"),
    IN_PROGRESS("In_Progress"),
    FINISHED("Finished");

    private final String shortName;

    GameStatus(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static GameStatus fromShortName(String shortName) {
        switch (shortName) {
            case "New":
                return GameStatus.NEW;

            case "In_Progress":
                return GameStatus.IN_PROGRESS;

            case "Finished":
                return GameStatus.FINISHED;
            default:
                throw new IllegalArgumentException("ShortName [" + shortName
                        + "] not supported.");
        }
    }

}
