package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.repository.PlayerRepository;
import hu.bme.aut.shed.repository.UserRepository;
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
    UserRepository userRepository;

    @Autowired
    GameService gameService;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Player> getPlayersByGameId(Long gameId) throws GameNotFoundException {
        Game game = gameService.getGameById(gameId);
        return playerRepository.findByGame(game);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Player> getPlayersByGameName(String gameName) throws GameNotFoundException {
        Game game = gameService.getGameByName(gameName);
        return playerRepository.findByGame(game);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String getPlayerGameName(String username) throws UserNotFoundException{
        Player player = playerRepository.findByUsername(username);
        Game game = player.getGame();
        return game.getName();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Player connectPlayer(String username, Long gameId) throws GameNotFoundException, UserNotFoundException, LobbyIsFullException {
        Game game = gameService.getGameById(gameId);
        User searchedUser = userRepository.findByUsername(username);
        if (searchedUser == null) throw new UserNotFoundException();
        Player alreadyConnectedPlayer = playerRepository.findByUserAndGameId(searchedUser, gameId);
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
        gameService.removePlayerFromList(player.getGame(),player);
        playerRepository.deleteById(player.getId());
        LoggerFactory.getLogger(this.getClass()).info(String.valueOf(playerRepository.findAll().size()));
    }
}
