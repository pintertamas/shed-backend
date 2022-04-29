package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.component.JwtTokenUtil;
import hu.bme.aut.shed.dto.Response.LobbyMessage;
import hu.bme.aut.shed.dto.Response.PlayerResponse;
import hu.bme.aut.shed.exception.*;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.service.GameService;
import hu.bme.aut.shed.service.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping(value = "/check-already-in-game/{username}", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> getPlayerGame(@PathVariable String username) {
        try {
            log.info(username);
            return ResponseEntity.ok(playerService.getGameNameByPlayerUsername(username));
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.ok("");
        }
    }

    // only for testing purposes
    @RequestMapping(value = "/connect/", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<?> connect(@RequestParam Long gameId, @RequestParam String username) {
        try {
            String token = JwtTokenUtil.getToken();
            User currentUser = jwtTokenUtil.getUserFromToken(token);
            if (!username.equals(currentUser.getUsername())) {
                throw new AuthorizationServiceException("You dont have permission to make changes");
            }
            Game game = gameService.getGameById(gameId);
            Player player = playerService.connectPlayer(username, game);
            log.info("User (" + player.getUsername() + ") connected to game: " + gameId);
            return ResponseEntity.ok(new PlayerResponse(player.getId(), player.getUsername()));
        } catch (GameNotFoundException | UserNotFoundException | LobbyIsFullException | GameAlreadyStartedException | AlreadyConnectedToOtherGameException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // only for testing purposes
    @RequestMapping(value = "/disconnect/{username}", method = {RequestMethod.DELETE}, produces = "application/json")
    public ResponseEntity<?> disconnect(@PathVariable String username) {
        try {
            String token = JwtTokenUtil.getToken();
            User currentUser = jwtTokenUtil.getUserFromToken(token);
            if (!username.equals(currentUser.getUsername())) {
                throw new AuthorizationServiceException("You dont have permission to make changes");
            }
            Player player = playerService.getPlayerByUsername(username);
            playerService.disconnectPlayer(username);
            log.info("User (" + username + ") disconnected from the game");
            simpMessagingTemplate.convertAndSend("/topic/"+player.getGame().getName(), new LobbyMessage("leave" , username));
            return ResponseEntity.ok("Player " + username + "disconnected from the game!");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/list/{gameId}", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> listPlayersByGameId(@PathVariable Long gameId) {
        try {
            Game game = gameService.getGameById(gameId);
            List<Player> players = playerService.getPlayersByGame(game);
            List<PlayerResponse> playerResponses = new ArrayList<PlayerResponse>();
            for (Player player : players) {
                playerResponses.add(new PlayerResponse(player.getId(), player.getUsername()));
            }
            return ResponseEntity.ok(playerResponses);
        } catch (GameNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/list/", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> listPlayersByGameName(@RequestParam String gameName) {
        try {
            Game game = gameService.getGameByName(gameName);
            List<Player> players = playerService.getPlayersByGame(game);
            List<PlayerResponse> playerResponses = new ArrayList<PlayerResponse>();
            for (Player player : players) {
                playerResponses.add(new PlayerResponse(player.getId(), player.getUsername()));
            }
            return ResponseEntity.ok(playerResponses);
        } catch (GameNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
