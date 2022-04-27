package hu.bme.aut.shed.service;

import hu.bme.aut.shed.dto.Request.ActionRequest;
import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


@Service
@AllArgsConstructor
public class GameService {

    @Autowired
    private final GameRepository gameRepository;

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
        Optional<List<Game>> games = gameRepository.findAllByStatusAndVisibility(state, true);
        if (games.isEmpty()) {
            throw new GameNotFoundException();
        }
        return games.get();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Game createGame(int numberOfCards, int numberOfDecks, Map<Integer, Rule> cardRules, boolean visibility, boolean jokers) {
        String url = "http://names.drycodes.com/1?nameOptions=funnyWords";
        RestTemplate restTemplate = new RestTemplate();
        Object[] response;
        Optional<Object> nameResponse;

        String gameName = new UUID(1, 0).toString();
        boolean nameIsUniq = false;
        int step = 0;
        while(!nameIsUniq && step <= 20){
            response = restTemplate.getForObject(url, Object[].class);
            if (response != null) {
                nameResponse = Arrays.stream(response).findFirst();
                if (nameResponse.isPresent()) {
                    if(gameRepository.findByName(nameResponse.get().toString()).isEmpty()){
                        gameName = nameResponse.get().toString();
                        nameIsUniq = true;
                    }
                }
            }
            step++;
        }
        Game game = new Game(numberOfCards, numberOfDecks, gameName, visibility, jokers);
        gameRepository.save(game);

        ArrayList<CardConfig> cards = cardConfigService.createCards(game, cardRules);
        game.setDeck(cards);
        return gameRepository.save(game);
    }

    public Game initGame(Game game) {
        game.setStatus(GameStatus.IN_PROGRESS);

        int cardCounter = 0;
        for (Player player : game.getPlayers()) {
            cardCounter = playerService.initPlayer(player, game, cardCounter);
        }

        int maxCardNumber = game.getPlayers().size() * (game.getCardsInHand() + 6);

        for (int i = maxCardNumber; i < game.getDeck().size(); i++) {
            LoggerFactory.getLogger(this.getClass()).info(String.valueOf(i));
            LoggerFactory.getLogger(this.getClass()).info(String.valueOf(game.getDeck().get(i).getId()));
            tableCardService.createTableCard(game.getDeck().get(i), TableCardState.PICK);
        }
        return gameRepository.save(game);
    }

    public Game startGame(Long id) throws GameNotFoundException {
        Game game = getGameById(id);
        game.setDeck(cardConfigService.getCardConfigsByGameId(game.getId())); //Erre azért van szükség mivel valamilyen természetes ellenes okból kifolyolag megváltozik a connect során a game.deck list mérete,
        // így a cardconfig gameId-ja alapján újra visszaállítom a rendes decket
        return initGame(game);
    }

    public Game action(ActionRequest action) throws GameNotFoundException {
        Optional<Game> game = gameRepository.findById(action.getGameId());
        if (game.isEmpty()) {
            throw new GameNotFoundException();
        }
        return game.get();
    }

    public void checkWinCondition() {

    }

    @Scheduled(fixedRate = 900000)
    public void deleteGamesScheduler() {
        List<Game> deletedGames = new ArrayList<>();
        Optional<List<Game>> finishedGames = gameRepository.findAllByStatus(GameStatus.FINISHED);
        Optional<List<Game>> inProgressGames = gameRepository.findAllByStatus(GameStatus.IN_PROGRESS);
        Optional<List<Game>> NewGames = gameRepository.findAllByStatus(GameStatus.NEW);
        if (NewGames.isPresent()) {                   //new games will be deleted if they are created for than 1 hour ago
            for (Game game : NewGames.get()) {
                Duration difference = Duration.between(game.getCreationTime(), LocalDateTime.now());
                if (difference.toHours() >= 1) {
                    deletedGames.add(game);
                }
            }
        }
        if (inProgressGames.isPresent()) {        //In_Progress games will be deleted if they are in progress but nobody is in the game
            for (Game game : inProgressGames.get()) {
                if (game.getPlayers().isEmpty()) {
                    deletedGames.add(game);
                }
            }
        }
        LoggerFactory.getLogger(this.getClass()).info("DeleteGamesSchedulerRun");
        finishedGames.ifPresent(deletedGames::addAll);
        for (Game game : deletedGames) {
            cardConfigService.deleteCardConfigs(game.getId());
            for (Player player : game.getPlayers()) {
                playerService.disconnectPlayer(player.getUsername());
            }
            gameRepository.deleteById(game.getId());
        }
    }

}
