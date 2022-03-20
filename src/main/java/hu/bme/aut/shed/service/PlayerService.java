package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.repository.GameRepository;
import hu.bme.aut.shed.repository.PlayerRepository;
import hu.bme.aut.shed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GameService gameService;

    public List<Player> getPlayersByGameId(Long gameId) throws GameNotFoundException {
        Game game = gameService.getGameById(gameId);
        List<Player> players = playerRepository.findByGame(game);
        return players;
    }

    public Player connectPlayer(String username, Long gameId) throws GameNotFoundException, UserNotFoundException, LobbyIsFullException {
        Game game = gameService.getGameById(gameId);
        User searchedUser = userRepository.findByUsername(username);
        if (searchedUser == null) throw new UserNotFoundException();

        List<Player> players = playerRepository.findByGame(game);
        if(players.size() >= game.getMaxPlayers()) {
            throw new LobbyIsFullException();
        }
        Player connectedPlayer = new Player(searchedUser);
        connectedPlayer.setGame(game);
        playerRepository.save(connectedPlayer);
        return connectedPlayer;
    }
}