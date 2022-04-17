package hu.bme.aut.shed.model;

public enum GameStatus {
    NEW("New"),
    IN_PROGRESS("In_Progress"),
    FINISHED("Finished");

    private final String name;

    GameStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static GameStatus fromName(String name) throws IllegalArgumentException {
        switch (name) {
            case "New":
                return GameStatus.NEW;

            case "In_Progress":
                return GameStatus.IN_PROGRESS;

            case "Finished":
                return GameStatus.FINISHED;

            default:
                throw new IllegalArgumentException("GameStatus [" + name
                        + "] not supported.");
        }
    }

}
