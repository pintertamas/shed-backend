package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.model.dto.ConnectionRequest;
import hu.bme.aut.shed.service.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @RequestMapping(value = "/connect/", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<Player> connect(@RequestParam Long gameId, @RequestParam String username) {
        ConnectionRequest request = new ConnectionRequest(gameId, username);
        log.info("connect request: {}", request);
        try {

            return ResponseEntity.ok(playerService.connectPlayer(username, request.getGameId()));
        } catch (GameNotFoundException | UserNotFoundException | LobbyIsFullException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<List<Player>> listPlayersByGameId(@RequestParam Long gameId) {
        try {
            return ResponseEntity.ok(playerService.getPlayersByGameId(gameId));
        } catch (GameNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
