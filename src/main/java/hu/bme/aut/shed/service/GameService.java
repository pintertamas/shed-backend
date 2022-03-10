package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.GameStatus;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.model.dto.ActionRequest;
import hu.bme.aut.shed.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GameService {

    public Game createGame(User user, int numberOfCards, int numberOfDecks) throws UserNotFoundException {
        //if (userRepository.findUserByUsername(username) == null) throw new UserNotFoundException();
        Player player = new Player(user);
        return new Game(player, numberOfCards, numberOfDecks);
    }

    public Game connectPlayer(Player newPlayer, String gameId) throws GameNotFoundException, UserNotFoundException, LobbyIsFullException {
        if (newPlayer == null) throw new UserNotFoundException();

        Game game = GameStorage.getInstance().getGames().get(gameId);

        if (game == null) {
            throw new GameNotFoundException();
        }

        if (game.getPlayers().size() >= game.getMaxPlayers()) {
            throw new LobbyIsFullException();
        }

        game.getPlayers().add(newPlayer);
        GameStorage.getInstance().saveGame(game);
        return game;
    }

    public Game connectToRandomGame(Player newPlayer) throws GameNotFoundException, UserNotFoundException, LobbyIsFullException {
        if (newPlayer == null) throw new UserNotFoundException();
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst().orElseThrow(GameNotFoundException::new);
        game.addPlayer(newPlayer);
        game.setStatus(GameStatus.IN_PROGRESS);
        GameStorage.getInstance().addGame(game);
        return game;
    }

    public Game startGame(Game game) throws GameNotFoundException, UserNotFoundException {
        if (GameStorage.getInstance().getGameByID(game.getGameId()) == null) throw new GameNotFoundException();
        if (game.getPlayers().isEmpty() || game.getPlayers().peek() == null) throw new GameNotFoundException();
        game.initGame();
        GameStorage.getInstance().addGame(game);
        return game;
    }

    public Game action(ActionRequest action) throws GameNotFoundException {
        Game game = GameStorage.getInstance().getGameByID(action.getGameId());
        // do the action if valid
        return game;
    }

}
