package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.Player;
import hu.bme.aut.shed.model.dto.ActionRequest;
import hu.bme.aut.shed.model.dto.ConnectionRequest;
import hu.bme.aut.shed.model.dto.GameOptionsRequest;
import hu.bme.aut.shed.repository.GameRepository;
import hu.bme.aut.shed.repository.UserRepository;
import hu.bme.aut.shed.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserRepository userRepository;

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping(value = "/create", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<?> create(@RequestBody GameOptionsRequest request) {
        log.info("create game request: {}", request);
        try {
            //if (userRepository.findUserByUsername(request.getCreator()) == null) throw new UserNotFoundException();
            Player player = new Player(request.getCreator());
            Game game = gameService.createGame(player.getUsername(), request.getNumberOfCards(), request.getNumberOfDecks());
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
            Game game = gameRepository.findGameByGameId(request.getGameId());
            if (game == null) throw new GameNotFoundException();
            return ResponseEntity.ok(gameService.startGame(game));
        } catch (GameNotFoundException | UserNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/connect", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<Game> connect(@RequestBody ConnectionRequest request) {
        log.info("connect request: {}", request);
        try {
            //if (userRepository.findUserByUsername(request.getUsername()) == null) throw new UserNotFoundException();
            Player player = new Player(request.getUsername());
            return ResponseEntity.ok(gameService.connectPlayer(player, request.getGameId()));
        } catch (GameNotFoundException | UserNotFoundException | LobbyIsFullException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/connect/random", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<Game> connectRandom(@RequestBody String username) {
        log.info("connect random {}", username);
        try {
            //if (userRepository.findUserByUsername(username) == null) throw new UserNotFoundException();
            Player player = new Player(username);
            return ResponseEntity.ok(gameService.connectToRandomGame(player));
        } catch (GameNotFoundException | UserNotFoundException | LobbyIsFullException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/action", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<Game> gamePlay(@RequestBody ActionRequest request) {
        log.info("action: {}", request);
        try {
            Game game = gameService.action(request);
            simpMessagingTemplate.convertAndSend("/topic/action/" + game.getGameId(), game);
            return ResponseEntity.ok(game);
        } catch (GameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
