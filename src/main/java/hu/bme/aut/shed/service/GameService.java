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
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//TODO //Refactor this

@Service
@AllArgsConstructor
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public Game getGameById(Long Id) throws GameNotFoundException {
        Optional<Game> game = gameRepository.findById(Id);
        if (game.isEmpty()) {
            throw new GameNotFoundException();
        }
        return game.get();
    }
    public Game getGameByState(GameStatus state) throws GameNotFoundException {
        Optional<Game> game = gameRepository.findByStatus(state);
        if (game.isEmpty()) {
            throw new GameNotFoundException();
        }
        return game.get();
    }

    public Game createGame(int numberOfCards, int numberOfDecks) throws UserNotFoundException {
        String url = "http://names.drycodes.com/1?nameOptions=funnyWords";
        RestTemplate restTemplate = new RestTemplate();
        Object[] name = restTemplate.getForObject(url,Object[].class);

        Game game = new Game(numberOfCards, numberOfDecks, Arrays.stream(name).findFirst().toString());
        return gameRepository.save(game);
    }

    public Game initGame(Game game) {
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
        return game;
    }

    public Game startGame(Long id) throws GameNotFoundException, UserNotFoundException {
        //if (GameStorage.getInstance().getGameByID(game.getId()) == null) throw new GameNotFoundException();
        //if (gameRepository.findById(game.getId()).isEmpty()) throw new GameNotFoundException();
        //if (game.getPlayers().isEmpty() || game.getPlayers().peek() == null) throw new GameNotFoundException();
        Game gameById = getGameById(id);
        Game startedGame = initGame(gameById);
        //GameStorage.getInstance().addGame(game);
        gameRepository.save(startedGame);
        return startedGame;
    }

    public Game action(ActionRequest action) throws GameNotFoundException {
        Optional<Game> game = gameRepository.findById(action.getGameId());
        if(game.isEmpty()) throw new GameNotFoundException();
        return game.get();
    }

}
