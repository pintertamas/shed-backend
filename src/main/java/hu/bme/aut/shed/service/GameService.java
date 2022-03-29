package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.dto.Request.ActionRequest;
import hu.bme.aut.shed.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

//TODO //Refactor this

@Service
@AllArgsConstructor
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CardConfigService cardService;

    public Game getGameById(Long Id) throws GameNotFoundException {
        Optional<Game> game = gameRepository.findById(Id);
        if (game.isEmpty()) {
            throw new GameNotFoundException();
        }
        return game.get();
    }

    public List<Game> getGamesByState(GameStatus state) throws GameNotFoundException {
        Optional<List<Game>> games = gameRepository.findAllByStatus(state);
        if (games.isEmpty()) {
            throw new GameNotFoundException();
        }
        return games.get();
    }

    public Game createGame(int numberOfCards, int numberOfDecks, boolean jokers) throws UserNotFoundException {
        String url = "http://names.drycodes.com/1?nameOptions=funnyWords";
        RestTemplate restTemplate = new RestTemplate();
        Object[] response = restTemplate.getForObject(url, Object[].class);
        Optional<Object> nameResponse;

        Game game = new Game(numberOfCards, numberOfDecks, new UUID(5, 5).toString(), false, jokers);
        gameRepository.save(game);
        ArrayList<CardConfig> cards = cardService.createCards(game);
        game.setDeck(cards);


        if (response != null) {
            nameResponse = Arrays.stream(response).findFirst();
            if (nameResponse.isPresent()) {
                game.setName(nameResponse.get().toString());
                return gameRepository.save(game);
            }
        }
        return gameRepository.save(game);
    }

    public Game initGame(Game game) {
        game.setStatus(GameStatus.IN_PROGRESS);
        return game;
    }

    public Game startGame(Long id) throws GameNotFoundException, UserNotFoundException {
        Game gameById = getGameById(id);
        Game startedGame = initGame(gameById);
        gameRepository.save(startedGame);
        return startedGame;
    }

    public Game action(ActionRequest action) throws GameNotFoundException {
        Optional<Game> game = gameRepository.findById(action.getGameId());
        if (game.isEmpty()) throw new GameNotFoundException();
        return game.get();
    }

}
