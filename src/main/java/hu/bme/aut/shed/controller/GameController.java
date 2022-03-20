package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.GameStatus;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.model.dto.ActionRequest;
import hu.bme.aut.shed.model.dto.ConnectionRequest;
import hu.bme.aut.shed.model.dto.GameOptionsRequest;
import hu.bme.aut.shed.repository.GameRepository;
import hu.bme.aut.shed.repository.UserRepository;
import hu.bme.aut.shed.service.GameService;
import hu.bme.aut.shed.service.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    @Autowired
    private final GameService gameService;
    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/")
    public ResponseEntity<Game> getGameByState(@RequestParam GameStatus status) {
        try {
            Game game = gameService.getGameByState(status);
            return new ResponseEntity<>(game, HttpStatus.OK);
        }
        catch (GameNotFoundException exception){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> create(@RequestParam int numberOfDecks, @RequestParam int numberOfCards) {
        GameOptionsRequest request = new GameOptionsRequest(numberOfDecks, numberOfCards);
        log.info("create game request: {}", request);
        try {
            Game game = gameService.createGame(request.getNumberOfCards(), request.getNumberOfDecks());
            return ResponseEntity.ok(game);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/start", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<?> start(@RequestBody ConnectionRequest request) {
        log.info("start game request: {}", request);
        try {
            return ResponseEntity.ok(gameService.startGame(request.getGameId()));
        } catch (GameNotFoundException | UserNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/action", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<Game> gamePlay(@RequestBody ActionRequest request) {
        log.info("action: {}", request);
        try {
            Game game = gameService.action(request);
            simpMessagingTemplate.convertAndSend("/topic/action/" + game.getId(), game);
            return ResponseEntity.ok(game);
        } catch (GameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
