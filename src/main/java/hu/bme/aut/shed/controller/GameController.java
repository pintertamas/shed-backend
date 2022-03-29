package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.dto.Response.GameResponse;
import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.GameStatus;
import hu.bme.aut.shed.dto.Request.ActionRequest;
import hu.bme.aut.shed.dto.Request.ConnectionRequest;
import hu.bme.aut.shed.dto.Request.GameOptionsRequest;
import hu.bme.aut.shed.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    @Autowired
    private final GameService gameService;
    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/list-new")
    public ResponseEntity<?> getNewGames() {
        try {
            List<Game> games = gameService.getGamesByState(GameStatus.NEW);
            List<GameResponse> gameResponses = new ArrayList<>();
            for (Game game : games) {
                gameResponses.add(new GameResponse(game.getId(), game.getName()));
            }
            return new ResponseEntity<>(gameResponses, HttpStatus.OK);
        } catch (GameNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> create(@RequestBody GameOptionsRequest request) {
        log.info("create game request: {}", request);
        try {
            Game game = gameService.createGame(request.getNumberOfCardsInHand(), request.getNumberOfDecks(), true);
            GameResponse gameResponse = new GameResponse(game.getId(), game.getName());
            return ResponseEntity.ok(gameResponse);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/start", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<?> start(@RequestBody ConnectionRequest request) {
        log.info("start game request: {}", request);
        try {
            return ResponseEntity.ok(gameService.startGame(request.getGameId()));
        } catch (GameNotFoundException | UserNotFoundException exception) {
            log.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/action", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<?> gamePlay(@RequestBody ActionRequest request) {
        log.info("action: {}", request);
        try {
            Game game = gameService.action(request);
            simpMessagingTemplate.convertAndSend("/topic/action/" + game.getId(), game);
            return ResponseEntity.ok(game);
        } catch (GameNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
}
