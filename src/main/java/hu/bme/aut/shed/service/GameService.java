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
    private CardConfigService cardConfigService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TableCardService tableCardService;


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

    public Game createGame(int numberOfCards, int numberOfDecks, Map<Integer,Rule> cardRules, boolean jokers) {
        String url = "http://names.drycodes.com/1?nameOptions=funnyWords";
        RestTemplate restTemplate = new RestTemplate();
        Object[] response;
        Optional<Object> nameResponse;

        Game game = new Game(numberOfCards, numberOfDecks, new UUID(2, 2).toString(), false, jokers);
        gameRepository.save(game);
        ArrayList<CardConfig> cards = cardConfigService.createCards(game, cardRules);
        game.setDeck(cards);
        LoggerFactory.getLogger(this.getClass()).info("GameDeck size : " + String.valueOf(game.getDeck().size()));

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

    public Game initGame(Game game) {
        game.setStatus(GameStatus.IN_PROGRESS);
        LoggerFactory.getLogger(this.getClass()).info("1.GameDeck size : " + String.valueOf(game.getDeck().size()));

        int cardCounter = 0;
        for (Player player : game.getPlayers()){
            LoggerFactory.getLogger(this.getClass()).info("CardCounter before : " + String.valueOf(cardCounter));
            cardCounter = playerService.initPlayer(player, game , cardCounter);
            LoggerFactory.getLogger(this.getClass()).info("CardCounter after : " + String.valueOf(cardCounter));
        }

        int maxCardNumber = game.getPlayers().size() * (game.getCardsInHand() + 6);
        LoggerFactory.getLogger(this.getClass()).info("MaxCardNumber : " + String.valueOf(maxCardNumber));

        LoggerFactory.getLogger(this.getClass()).info("2.GameDeck size : " + String.valueOf(game.getDeck().size()));

        for (CardConfig cardConfig : game.getDeck() ) {
            LoggerFactory.getLogger(this.getClass()).info("faszomebbe 1: " + String.valueOf(cardConfig.getId()));
        }

        LoggerFactory.getLogger(this.getClass()).info("faszomebbe 2: " + String.valueOf(game.getDeck().size()));

        for (int i = maxCardNumber ; i < game.getDeck().size() ; i++){
            LoggerFactory.getLogger(this.getClass()).info(String.valueOf(i));
            LoggerFactory.getLogger(this.getClass()).info(String.valueOf(game.getDeck().get(i).getId()));
            tableCardService.createTableCard(game.getDeck().get(i));
        }
        LoggerFactory.getLogger(this.getClass()).info("3.GameDeck size : " + String.valueOf(game.getDeck().size()));
        return gameRepository.save(game);
    }

    public Game startGame(Long id) throws GameNotFoundException, UserNotFoundException {
        Game game = getGameById(id);
        LoggerFactory.getLogger(this.getClass()).info("0.1.GameDeck size : " + String.valueOf(game.getDeck().size()));
        Game game1 = initGame(game);
        LoggerFactory.getLogger(this.getClass()).info("0.2.GameDeck size : " + String.valueOf(game1.getDeck().size()));
        return game1;
    }

    public Game action(ActionRequest action) throws GameNotFoundException {
        Optional<Game> game = gameRepository.findById(action.getGameId());
        if (game.isEmpty()) {
            throw new GameNotFoundException();
        }
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
            cardConfigService.deleteCardConfigs(game.getId());
            gameRepository.deleteById(game.getId());
        }
    }

}
