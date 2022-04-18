package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.repository.GameRepository;
import hu.bme.aut.shed.repository.PlayerRepository;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerCardService playerCardService;
    @Autowired
    UserService userService;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Player> getPlayersByGame(Game game){
        return playerRepository.findByGame(game);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String getGameNameByPlayerUsername(String username) throws UserNotFoundException{
        Player player = playerRepository.findByUsername(username);
        if(player == null){
            throw new UserNotFoundException();
        }
        Game game = player.getGame();
        return game.getName();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Player connectPlayer(String username, Game game) throws UserNotFoundException, LobbyIsFullException {
        User searchedUser = userService.getByUsername(username);

        Player alreadyConnectedPlayer = playerRepository.findByUserAndGameId(searchedUser, game.getId());
        if (alreadyConnectedPlayer != null) {
            return alreadyConnectedPlayer;
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
        LoggerFactory.getLogger(this.getClass()).info("GameDeck size : " + String.valueOf(game.getDeck().size()));
        return connectedPlayer;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void disconnectPlayer(String username) {
        LoggerFactory.getLogger(this.getClass()).info(String.valueOf(playerRepository.findAll().size()));
        Player player = playerRepository.findByUsername(username);
        Game game = player.getGame();

        game.getPlayers().remove(player);
        gameRepository.save(game);

        playerRepository.deleteById(player.getId());
        LoggerFactory.getLogger(this.getClass()).info(String.valueOf(playerRepository.findAll().size()));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int initPlayer(Player player, Game game, int OrderId){

        //LoggerFactory.getLogger(this.getClass()).info("1.OrderId : " + String.valueOf(OrderId));
        for (int i = OrderId ; i < game.getCardsInHand() + OrderId; i++) {
            //LoggerFactory.getLogger(this.getClass()).info("i : " + String.valueOf(i));
            PlayerCard playerCardInHand = playerCardService.createPlayerCards(game.getDeck().get(i), player, PlayerCardState.HAND);
            player.getCards().add(playerCardInHand);
        }
        OrderId += game.getCardsInHand();
        //LoggerFactory.getLogger(this.getClass()).info("2.OrderId : " + String.valueOf(OrderId));
        for (int i = OrderId ; i < (OrderId + 3) ; i++) {
            //LoggerFactory.getLogger(this.getClass()).info("i : " + String.valueOf(i));
            PlayerCard playerCardVisible =  playerCardService.createPlayerCards(game.getDeck().get(i), player, PlayerCardState.VISIBLE);
            player.getCards().add(playerCardVisible);
        }
        OrderId += 3;
        //LoggerFactory.getLogger(this.getClass()).info("3.OrderId : " + String.valueOf(OrderId));
        for (int i = OrderId; i < (OrderId + 3) ; i++) {
            //LoggerFactory.getLogger(this.getClass()).info("i : " + String.valueOf(i));
            PlayerCard playerCardInvisible =  playerCardService.createPlayerCards(game.getDeck().get(i), player, PlayerCardState.INVISIBLE);
            player.getCards().add(playerCardInvisible);
        }
        OrderId += 3;
        //LoggerFactory.getLogger(this.getClass()).info("End OrderId : " + String.valueOf(OrderId));
        playerRepository.save(player);
        return OrderId;
    }


}

