package hu.bme.aut.shed.storage;

import hu.bme.aut.shed.model.Game;

import java.util.HashMap;
import java.util.Map;

public class GameStorage {
    private static Map<String, Game> games;
    private static GameStorage instance;

    private GameStorage() {
        games = new HashMap<>();
    }

    public static synchronized GameStorage getInstance() {
        if (instance == null) {
            instance = new GameStorage();
        }
        return instance;
    }

    public Game getGameByID(String gameID) {
        return games.get(gameID);
    }

    public Map<String, Game> getGames() {
        return games;
    }

    public void addGame(Game game) {
        games.put(game.getId(), game);
    }

    public void saveGame(Game game) {
        games.put(game.getId(), game);
    }
}
