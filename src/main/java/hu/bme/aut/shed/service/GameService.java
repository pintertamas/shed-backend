package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.GameStatus;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.repository.GameRepository;
import hu.bme.aut.shed.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class GameService {

    @Autowired
    GameRepository gameRepository;

    public Game createGame(String username, int amountOfCards, int amountOfDecks) {
        Player player = new Player(username, amountOfCards);
        return new Game(player, amountOfCards, amountOfDecks);
    }

    public Game connectPlayer(Player newPlayer, String gameId) throws GameNotFoundException, LobbyIsFullException {
        if (gameRepository.findGameByGameId(gameId) == null) {
            throw new GameNotFoundException();
        }
        Game game = gameRepository.findGameByGameId(gameId);

        if (game.getPlayers().size() >= game.getMaxPlayers()) {
            throw new LobbyIsFullException();
        }

        game.getPlayers().add(newPlayer);
        gameRepository.save(game);
        return game;
    }

    public Game connectToRandomGame(Player newPlayer) throws GameNotFoundException, LobbyIsFullException {
        Game game = gameRepository.findAllByStatusEquals(GameStatus.NEW).stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst().orElseThrow(GameNotFoundException::new);
        game = connectPlayer(newPlayer, game.getGameId());
        return game;
    }

    public Game startGame(Game game, Player player) throws GameNotFoundException {
        if (gameRepository.findGameByGameId(game.getGameId()) == null) throw new GameNotFoundException();
        if (game.getPlayers().isEmpty() || game.getPlayers().peek() == null) throw new GameNotFoundException();
        if (!Objects.equals(game.getPlayers().peek(), player)) {
            return game;
        }
        game.initGame();
        gameRepository.save(game);
        return game;
    }

}
