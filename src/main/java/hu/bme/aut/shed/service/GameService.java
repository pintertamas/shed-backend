package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.GameStatus;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GameService {

    public Game createGame(String username, int amountOfCards, int amountOfDecks) {
        Player player = new Player(username, amountOfCards);
        return new Game(player, amountOfCards, amountOfDecks);
    }

    public Game connectPlayer(Player newPlayer, String gameId) throws GameNotFoundException, LobbyIsFullException {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new GameNotFoundException();
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);

        if (game.getPlayers().size() >= game.getMaxPlayers()) {
            throw new LobbyIsFullException();
        }

        game.getPlayers().add(newPlayer);
        GameStorage.getInstance().addGame(game);
        return game;
    }

    public Game connectToRandomGame(Player newPlayer) throws GameNotFoundException, LobbyIsFullException {
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst().orElseThrow(GameNotFoundException::new);
        game = connectPlayer(newPlayer, game.getGameId());
        return game;
    }

    public Game startGame(Game game, Player player) throws GameNotFoundException {
        if (GameStorage.getInstance().getGames().get(game.getGameId()) == null) throw new GameNotFoundException();
        if (game.getPlayers().isEmpty() || game.getPlayers().peek() == null) throw new GameNotFoundException();
        if (!game.getPlayers().peek().equals(player)) {
            return game;
        }
        game.initGame();
        GameStorage.getInstance().updateGame(game);
        return game;
    }

}
