package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.dto.Response.PlayerResponse;
import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.service.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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

    @RequestMapping(value = "/check-already-in-game/{username}", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> getPlayerGame(@PathVariable String username) {
        try {
            return ResponseEntity.ok(playerService.getPlayerGame(username));
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/connect/", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<?> connect(@RequestParam Long gameId, @RequestParam String username) {
        try {
            Player player = playerService.connectPlayer(username, gameId);
            log.info("User (" + player.getUsername() + ") connected to game: " + gameId);
            return ResponseEntity.ok(new PlayerResponse(player.getId(), player.getUsername()));
        } catch (GameNotFoundException | UserNotFoundException | LobbyIsFullException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/disconnect/", method = {RequestMethod.DELETE}, produces = "application/json")
    public ResponseEntity<?> disconnect(@RequestParam String username) {
        try {
            playerService.disconnectPlayer(username);
            log.info("User (" + username + ") disconnected from the game");
            return ResponseEntity.ok("Player " + username + "disconnected from the game!");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/list/{gameId}", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> listPlayersByGameId(@PathVariable Long gameId) {
        try {
            List<Player> players = playerService.getPlayersByGameId(gameId);
            List<PlayerResponse> playerResponses = new ArrayList<PlayerResponse>();
            for (Player player : players) {
                playerResponses.add(new PlayerResponse(player.getId(), player.getUsername()));
            }
            return ResponseEntity.ok(playerResponses);
        } catch (GameNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/list/", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> listPlayersByGameName(@RequestParam String gameName) {
        try {
            List<Player> players = playerService.getPlayersByGameName(gameName);
            List<PlayerResponse> playerResponses = new ArrayList<PlayerResponse>();
            for (Player player : players) {
                playerResponses.add(new PlayerResponse(player.getId(), player.getUsername()));
            }
            return ResponseEntity.ok(playerResponses);
        } catch (GameNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
}
