package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.*;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.repository.GameRepository;
import hu.bme.aut.shed.repository.PlayerRepository;
import hu.bme.aut.shed.service.RuleStrategy.RuleStrategy;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PlayerService {

    @Autowired
    private final PlayerRepository playerRepository;
    @Autowired
    private final GameRepository gameRepository;

    @Autowired
    private final PlayerCardService playerCardService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final TableCardService tableCardService;
    @Autowired
    private final CardConfigService cardConfigService;
    @Autowired
    private final Map<String, RuleStrategy> ruleStrategy;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Player> getPlayersByGame(Game game) {
        return playerRepository.findByGame(game);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Player getPlayerByUsername(String username) throws UserNotFoundException {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new UserNotFoundException();
        }
        return player;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String getGameNameByPlayerUsername(String username) throws UserNotFoundException {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new UserNotFoundException();
        }
        Game game = player.getGame();
        return game.getName();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<PlayerCard> getPlayerCardsByUsername(String username) {
        Player player = playerRepository.findByUsername(username);
        return player.getCards();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Player connectPlayer(String username, Game game) throws UserNotFoundException, LobbyIsFullException, GameAlreadyStartedException, AlreadyConnectedToOtherGameException {
        User searchedUser = userService.getByUsername(username);

        Player alreadyConnectedPlayer = playerRepository.findByUsernameAndGameId(username, game.getId());
        if (alreadyConnectedPlayer != null) {
            return alreadyConnectedPlayer;
        }

        if (game.getStatus() == GameStatus.IN_PROGRESS || game.getStatus() == GameStatus.FINISHED) {
            throw new GameAlreadyStartedException();
        }

        if (playerRepository.findByUsername(username) != null) {
            throw new AlreadyConnectedToOtherGameException();
        }

        List<Player> players = playerRepository.findByGame(game);
        if (players.size() >= game.getMaxPlayers()) {
            throw new LobbyIsFullException();
        }

        Player connectedPlayer = new Player(searchedUser);
        connectedPlayer.setGame(game);
        game.getPlayers().add(connectedPlayer);
        gameRepository.save(game);
        playerRepository.save(connectedPlayer);
        return connectedPlayer;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void disconnectPlayer(String username){
        LoggerFactory.getLogger(this.getClass()).info(String.valueOf(playerRepository.findAll().size()));
        Player player = playerRepository.findByUsername(username);
        Game game = player.getGame();

        LoggerFactory.getLogger(this.getClass()).info("Player cardSize : "+String.valueOf(player.getCards().size()));
        for (PlayerCard playerCard : player.getCards() ){
            LoggerFactory.getLogger(this.getClass()).info(String.valueOf(playerCard.getId()));
        }

        for (PlayerCard playerCard : player.getCards() ){
            LoggerFactory.getLogger(this.getClass()).info("Deleting: " +String.valueOf(playerCard.getId()));
            playerCardService.removeById(playerCard.getId());
        }
        game.getPlayers().remove(player);
        gameRepository.save(game);
        playerRepository.deleteById(player.getId());
        LoggerFactory.getLogger(this.getClass()).info(String.valueOf(playerRepository.findAll().size()));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int initPlayer(Player player, Game game, int OrderId) {

        for (int i = OrderId; i < game.getCardsInHand() + OrderId; i++) {
            PlayerCard playerCardInHand = playerCardService.createPlayerCard(game.getDeck().get(i), player, PlayerCardState.HAND);
            player.getCards().add(playerCardInHand);
        }
        OrderId += game.getCardsInHand();
        for (int i = OrderId; i < (OrderId + 3); i++) {
            PlayerCard playerCardVisible = playerCardService.createPlayerCard(game.getDeck().get(i), player, PlayerCardState.VISIBLE);
            player.getCards().add(playerCardVisible);
        }
        OrderId += 3;
        for (int i = OrderId; i < (OrderId + 3); i++) {
            PlayerCard playerCardInvisible = playerCardService.createPlayerCard(game.getDeck().get(i), player, PlayerCardState.INVISIBLE);
            player.getCards().add(playerCardInvisible);
        }
        OrderId += 3;
        playerRepository.save(player);
        return OrderId;
    }

    public void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        ruleStrategy.get(playerCard.getCardConfig().getRule().name()).throwCard(playerFrom, tableCard, playerCard);
    }


}

