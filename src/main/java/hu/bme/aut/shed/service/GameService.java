package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.GameStatus;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.dto.ActionRequest;
import hu.bme.aut.shed.repository.GameRepository;
import hu.bme.aut.shed.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserRepository userRepository;

    public Game createGame(String username, int numberOfCards, int numberOfDecks) throws UserNotFoundException {
        //if (userRepository.findUserByUsername(username) == null) throw new UserNotFoundException();
        Player player = new Player(username);
        return new Game(player, numberOfCards, numberOfDecks);
    }

    public Game connectPlayer(Player newPlayer, String gameId) throws GameNotFoundException, UserNotFoundException, LobbyIsFullException {
        if (newPlayer == null) throw new UserNotFoundException();

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

    public Game connectToRandomGame(Player newPlayer) throws GameNotFoundException, UserNotFoundException, LobbyIsFullException {
        if (newPlayer == null) throw new UserNotFoundException();
        Game game = gameRepository.findAllByStatusEquals(GameStatus.NEW).stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst().orElseThrow(GameNotFoundException::new);
        game = connectPlayer(newPlayer, game.getGameId());
        return game;
    }

    public Game startGame(Game game) throws GameNotFoundException, UserNotFoundException {
        if (gameRepository.findGameByGameId(game.getGameId()) == null) throw new GameNotFoundException();
        if (game.getPlayers().isEmpty() || game.getPlayers().peek() == null) throw new GameNotFoundException();
        game.initGame();
        gameRepository.save(game);
        return game;
    }

    public Game action(ActionRequest action) throws GameNotFoundException {
        Game game = gameRepository.findGameByGameId(action.getGameId());
        // do the action if valid
        return game;
    }

}
