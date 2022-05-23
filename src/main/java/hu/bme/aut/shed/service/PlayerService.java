package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.*;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.repository.GameRepository;
import hu.bme.aut.shed.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
    private final PlayerCardStateHelperService playerCardStateHelperService;

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
    public List<PlayerCard> getPlayerCardsByUsername(String username) throws UserNotFoundException {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new UserNotFoundException();
        }
        player.setCards(playerCardService.getPlayerCardsByPlayer(player));
        return player.getCards();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Player> getAllPlayersByStateAndGame(GameStatus status, Game game) {
        return playerRepository.findAllByStatusAndGame(status, game);
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
        connectedPlayer.setStatus(GameStatus.NEW);

        game.getPlayers().add(connectedPlayer);
        gameRepository.save(game);
        playerRepository.save(connectedPlayer);

        return connectedPlayer;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void disconnectPlayer(String username) {

        Player player = playerRepository.findByUsername(username);
        Game game = player.getGame();
        LoggerFactory.getLogger(this.getClass()).info("Players in tables" + String.valueOf(game.getPlayers().size()));

        player.setCards(playerCardService.getPlayerCardsByPlayer(player));
        LoggerFactory.getLogger(this.getClass()).info("Player cardSize : " + String.valueOf(player.getCards().size()));
        for (PlayerCard playerCard : player.getCards()) {
            LoggerFactory.getLogger(this.getClass()).info(String.valueOf(playerCard.getId()));
        }

        for (PlayerCard playerCard : player.getCards()) {
            LoggerFactory.getLogger(this.getClass()).info("Deleting: " + String.valueOf(playerCard.getId()));
            playerCardService.removeById(playerCard.getId());
        }


        game.setPlayers(this.getPlayersByGame(game)); //Spring array list novelo hiba miatt
        LoggerFactory.getLogger(this.getClass()).info("Players in tables" + String.valueOf(game.getPlayers().size()));

        if(Objects.equals(game.getCurrentPlayer().getUsername(), player.getUsername())){
            if (game.getPlayers().size() == 1) {
                game.setCurrentPlayer(null);
                gameRepository.save(game);
            } else {

                Player currentPlayer = game.getCurrentPlayer();
                int currentPlayerIndex = game.getPlayers().indexOf(currentPlayer);
                int lastPlayerIndex = game.getPlayers().size() - 1;

                for (Player gamePlayer : game.getPlayers()){
                    if (Objects.equals(gamePlayer.getUsername(), currentPlayer.getUsername())){
                        currentPlayerIndex = game.getPlayers().indexOf(gamePlayer);
                    }
                }

                Player nextPlayer = new Player();
                while (nextPlayer.getStatus() != GameStatus.IN_PROGRESS) {
                    if (currentPlayerIndex + 1 > lastPlayerIndex) { //if index bigger than the last index than then we go back to the first index
                        nextPlayer = game.getPlayers().get(0); //first player of the list
                        game.setCurrentPlayer(nextPlayer);
                    } else {
                        currentPlayerIndex++;
                        nextPlayer = game.getPlayers().get(currentPlayerIndex);
                        game.setCurrentPlayer(nextPlayer);
                    }

                }
            }
        }

        game.getPlayers().remove(player);
        gameRepository.save(game);
        playerRepository.deleteById(player.getId());
        LoggerFactory.getLogger(this.getClass()).info(String.valueOf(playerRepository.findAll().size()));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int initPlayer(Player player, Game game, int OrderId) {
        player.setStatus(GameStatus.IN_PROGRESS);

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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void throwCard(Player playerFrom, TableCard tableCard, PlayerCard playerCard) throws CantThrowCardException {
        playerCardStateHelperService.throwCard(playerFrom, tableCard, playerCard);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void pickCard(Player playerTo, TableCard tableCard) {
        CardConfig cardConfig = tableCard.getCardConfig();
        playerCardService.createPlayerCard(cardConfig, playerTo, PlayerCardState.HAND); //This adds card also to player cards list
        tableCardService.removeTableCardByCardConfig(tableCard.getCardConfig());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void setPlayerFinishedPosition(Player player, Game game) {
        player.setStatus(GameStatus.FINISHED);
        List<Player> finishedPlayers = this.getAllPlayersByStateAndGame(GameStatus.FINISHED, game);
        player.setFinishedPosition(finishedPlayers.size() + 1);
        LoggerFactory.getLogger(this.getClass()).info("Player is Finished" + String.valueOf(player.getUsername()) + "Position :" + String.valueOf(player.getFinishedPosition()));
        playerRepository.save(player);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void checkEndCondition(Player player, Game game) {
        int pickCardsSize = tableCardService.getAllByTableCardStateAndGame(TableCardState.PICK, game).size();
        if (player.getCards().size() == 0 && pickCardsSize == 0) {
            this.setPlayerFinishedPosition(player, game);
        }
    }


}

