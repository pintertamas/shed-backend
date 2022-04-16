package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.GameNotFoundException;
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
    public Player connectPlayer(String username, Game game) throws GameNotFoundException, UserNotFoundException, LobbyIsFullException {
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
        playerRepository.save(connectedPlayer);
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
    public void initPlayer(Player player, Game game, int OrderId){
        for (int i = 0 ; i < game.getCardsInHand(); i++){
            PlayerCard playerCardInHand =  playerCardService.createPlayerCards(game.getDeck().get(OrderId++ + i), player, PlayerCardState.HAND);
            PlayerCard playerCardVisible =  playerCardService.createPlayerCards(game.getDeck().get(OrderId++ + i), player, PlayerCardState.VISIBLE);
            PlayerCard playerCardInvisible =  playerCardService.createPlayerCards(game.getDeck().get(OrderId++ + i), player, PlayerCardState.INVISIBLE);
            player.getCards().add(playerCardInHand);
            player.getCards().add(playerCardVisible);
            player.getCards().add(playerCardInvisible);
            playerRepository.save(player);
        }

    }
}
