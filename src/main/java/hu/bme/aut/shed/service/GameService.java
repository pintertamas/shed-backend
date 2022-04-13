package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.dto.Request.ActionRequest;
import hu.bme.aut.shed.repository.GameRepository;

import lombok.AllArgsConstructor;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


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

    public Game getGameByName(String name) throws GameNotFoundException {
        Optional<Game> game = gameRepository.findByName(name);
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


    @Transactional
    public void removePlayerFromList(Game game,Player player) {
        game.getPlayers().remove(player);
        gameRepository.save(game);
    }

    public Game createGame(int numberOfCards, int numberOfDecks, boolean jokers) {
        String url = "http://names.drycodes.com/1?nameOptions=funnyWords";
        RestTemplate restTemplate = new RestTemplate();
        Object[] response;
        Optional<Object> nameResponse;

        Game game = new Game(numberOfCards, numberOfDecks, new UUID(2, 2).toString(), false, jokers);
        gameRepository.save(game);
        ArrayList<CardConfig> cards = cardService.createCards(game);
        game.setDeck(cards);

        boolean nameIsUniq = false;
        int step = 0;
        while(!nameIsUniq && step <= 20){
            response = restTemplate.getForObject(url, Object[].class);
            if (response != null) {
                nameResponse = Arrays.stream(response).findFirst();
                if (nameResponse.isPresent()) {
                    if(gameRepository.findByName(nameResponse.get().toString()).isEmpty()){
                        game.setName(nameResponse.get().toString());
                        nameIsUniq = true;
                    }
                }
            }
            step++;
        }
        return gameRepository.save(game);
    }

    @Transactional
    public Game initGame(Game game) {
        game.setStatus(GameStatus.IN_PROGRESS);
        return gameRepository.save(game);
    }

    public Game startGame(Long id) throws GameNotFoundException, UserNotFoundException {
        Game gameById = getGameById(id);
        return initGame(gameById);
    }

    public Game action(ActionRequest action) throws GameNotFoundException {
        Optional<Game> game = gameRepository.findById(action.getGameId());
        if (game.isEmpty()) throw new GameNotFoundException();
        return game.get();
    }

    @Scheduled(fixedRate = 900000)
    public void deleteGamesScheduler(){
        List<Game> deletedGames = new ArrayList<>();
        Optional<List<Game>> finishedGames = gameRepository.findAllByStatus(GameStatus.FINISHED);
        Optional<List<Game>> inProgressGames = gameRepository.findAllByStatus(GameStatus.IN_PROGRESS);
        Optional<List<Game>> NewGames = gameRepository.findAllByStatus(GameStatus.NEW);
        if(NewGames.isPresent()){                   //new games will be deleted if they are created for than 1 hour ago
            for(Game game : NewGames.get()){
                Duration difference = Duration.between(game.getCreationTime(), LocalDateTime.now());
                if(difference.toHours() >= 1){
                    deletedGames.add(game);
                }
            }
        }
        if(inProgressGames.isPresent()){        //In_Progress games will be deleted if they are in progress but nobody is in the game
            for(Game game: inProgressGames.get()){
                if(game.getPlayers().isEmpty()){
                    deletedGames.add(game);
                }
            }
        }
        LoggerFactory.getLogger(this.getClass()).info("DeleteGamesSchedulerRun");
        finishedGames.ifPresent(deletedGames::addAll);
        for(Game game : deletedGames){
            cardService.deleteCardConfigs(game.getId());
            gameRepository.deleteById(game.getId());
        }
    }

}
