package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.dto.Request.CardRuleRequest;
import hu.bme.aut.shed.dto.Request.ConnectionRequest;
import hu.bme.aut.shed.dto.Request.GameOptionsRequest;
import hu.bme.aut.shed.dto.Response.GameResponse;
import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.model.GameStatus;
import hu.bme.aut.shed.model.Rule;
import hu.bme.aut.shed.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    @Autowired
    private final GameService gameService;

    @RequestMapping(value = "/", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> getGameByName(@RequestParam String gameName) {
        try {
            Game game = gameService.getGameByName(gameName);
            GameResponse gameResponses = new GameResponse(game.getId(), game.getName());
            return new ResponseEntity<>(gameResponses, HttpStatus.OK);
        } catch (GameNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/list/", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> getGamesByStatus(@RequestParam String statusValue) {
        try {
            List<Game> games = gameService.getGamesByState(GameStatus.fromName(statusValue));
            List<GameResponse> gameResponses = new ArrayList<>();
            for (Game game : games) {
                gameResponses.add(new GameResponse(game.getId(), game.getName()));
            }
            return new ResponseEntity<>(gameResponses, HttpStatus.OK);
        } catch (GameNotFoundException | IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<?> create(@RequestBody GameOptionsRequest request) {
        log.info("create game request: {}", request);
        try {
            Map<Integer, Rule> rules = new HashMap<>();
            for (CardRuleRequest cardRuleRequest : request.getCardRules()) {
                rules.put(cardRuleRequest.getNumber(), Rule.fromName(cardRuleRequest.getRule()));
            }
            Game game = gameService.createGame(request.getNumberOfCardsInHand(), request.getNumberOfDecks(), rules, request.isVisible(), request.isJoker());
            GameResponse gameResponse = new GameResponse(game.getId(), game.getName());
            return ResponseEntity.ok(gameResponse);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/start", method = {RequestMethod.POST}, produces = "application/json")
    public ResponseEntity<?> start(@RequestBody ConnectionRequest request) {
        log.info("start game request: {}", request);
        try {
            Game game = gameService.startGame(request.getGameId());
            GameResponse gameResponse = new GameResponse(game.getId(), game.getName());
            return ResponseEntity.ok(gameResponse);
        } catch (GameNotFoundException exception) {
            log.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
