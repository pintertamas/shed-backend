package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.GameStatus;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.dto.ActionRequest;
import hu.bme.aut.shed.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//TODO //Refactor this

@Service
@AllArgsConstructor
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public Game createGame(int numberOfCards, int numberOfDecks) throws UserNotFoundException {
        //if (userRepository.findUserByUsername(username) == null) throw new UserNotFoundException();
        return new Game(numberOfCards, numberOfDecks);
    }

    public Game connectPlayer(Player newPlayer, String gameId) throws GameNotFoundException, UserNotFoundException, LobbyIsFullException {
        if (newPlayer == null) throw new UserNotFoundException();

        //Game game = GameStorage.getInstance().getGames().get(gameId);
        Optional<Game> game = gameRepository.findById(gameId);

        if (game.isEmpty()) {
            throw new GameNotFoundException();
        }

        //List<Player> players = playerRepository...

        /*if (players.size() >= game.get().getMaxPlayers()) {
            throw new LobbyIsFullException();
        }

        game.get().getPlayers().add(newPlayer);*/
        gameRepository.save(game.get());
        return game.get();
    }

    public Game connectToRandomGame(Player newPlayer) throws GameNotFoundException, UserNotFoundException, LobbyIsFullException {
        if (newPlayer == null) throw new UserNotFoundException();
        /*Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst().orElseThrow(GameNotFoundException::new);*/
        Game game = gameRepository.findAll().stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst().orElseThrow(GameNotFoundException::new);
        game.setStatus(GameStatus.IN_PROGRESS);
        //GameStorage.getInstance().addGame(game);
        gameRepository.save(game);
        return game;
    }
    public void initGame(Game game) {
        //game.getDeck().createCards();
        //getDeck().shuffleDeck();
        /*for (Player player : getPlayers()) {
            player.initPlayer(getNumberOfCards());
            for (int i = 0; i < numberOfCards; i++) {
                player.getHiddenCards().add(getDeck().getCards().pop());
                player.getHiddenCards().add(getDeck().getCards().pop());
                player.getHiddenCards().add(getDeck().getCards().pop());
            }
        }*/
        game.setStatus(GameStatus.IN_PROGRESS);
    }

    public Game startGame(Game game) throws GameNotFoundException, UserNotFoundException {
        //if (GameStorage.getInstance().getGameByID(game.getId()) == null) throw new GameNotFoundException();
        //if (gameRepository.findById(game.getId()).isEmpty()) throw new GameNotFoundException();
        //if (game.getPlayers().isEmpty() || game.getPlayers().peek() == null) throw new GameNotFoundException();
        initGame(game);
        //GameStorage.getInstance().addGame(game);
        gameRepository.save(game);
        return game;
    }

    public Game action(ActionRequest action) throws GameNotFoundException {
        Optional<Game> game = gameRepository.findById(action.getGameId());
        if(game.isEmpty()) throw new GameNotFoundException();
        return game.get();
    }

}
