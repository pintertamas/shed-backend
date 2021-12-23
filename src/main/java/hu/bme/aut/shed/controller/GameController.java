package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.dto.ConnectionRequest;
import hu.bme.aut.shed.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/start")
    public ResponseEntity<?> start(@RequestBody Game game, @RequestBody Player player) {
        log.info("start game request: {}", player);
        try {
            return ResponseEntity.ok(gameService.startGame(game, player));
        } catch (GameNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectionRequest request) {
        log.info("connect request: {}", request);
        try {
            return ResponseEntity.ok(gameService.connectPlayer(request.getPlayer(), request.getGameId()));
        } catch (LobbyIsFullException | GameNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody Player player) {
        log.info("connect random {}", player);
        try {
            return ResponseEntity.ok(gameService.connectToRandomGame(player));
        } catch (GameNotFoundException | LobbyIsFullException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
