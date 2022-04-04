package hu.bme.aut.shed.controller.WebSocket;

import hu.bme.aut.shed.dto.Response.LobbyMessage;
import hu.bme.aut.shed.dto.Response.StartGameMessage;
import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.LobbyIsFullException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.Game;
import hu.bme.aut.shed.repository.GameRepository;
import hu.bme.aut.shed.service.GameService;
import hu.bme.aut.shed.service.PlayerService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyWSController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @SubscribeMapping("/subscribe")
    public String sendOneTimeMessage() {
        LoggerFactory.getLogger(this.getClass()).info("subscribed");
        return "server one-time message via the application";
    }

    @MessageMapping("/start-game/{gameName}")
    @SendTo("/topic/{gameName}")
    public StartGameMessage startGame(@DestinationVariable String gameName) {
        LoggerFactory.getLogger(this.getClass()).info("GameStarted : {}", gameName);
        return new StartGameMessage("game-start", gameName);
    }

    @MessageMapping("/join-game/{gameName}/{username}")
    @SendTo("/topic/{gameName}")
    public LobbyMessage joinGame(@DestinationVariable String gameName, @DestinationVariable String username) {
        try {
            Game game = gameService.getGameByName(gameName);
            LoggerFactory.getLogger(this.getClass()).info("Connecting (" + username + ") to game: " + gameName);
            playerService.connectPlayer(username, game.getId());
            LoggerFactory.getLogger(this.getClass()).info("User (" + username + ") joined to game: " + gameName);
            return new LobbyMessage("join", username);
        } catch (GameNotFoundException e) {
            LoggerFactory.getLogger(this.getClass()).info("Game with name (" + gameName + ") not found!");
            return new LobbyMessage("error", "game not found");
        } catch (UserNotFoundException e) {
            LoggerFactory.getLogger(this.getClass()).info("User with name (" + username + ") not found!");
            return new LobbyMessage("error", "user not found");
        } catch (LobbyIsFullException e) {
            LoggerFactory.getLogger(this.getClass()).info("Lobby of game (" + gameName + ") is full!");
            return new LobbyMessage("error", "lobby is full");
        }
    }

    @MessageMapping("/leave-game/{gameName}/{username}")
    @SendTo("/topic/{gameName}")
    public LobbyMessage leaveGame(@DestinationVariable String username, @DestinationVariable String gameName) {
        try {
            playerService.disconnectPlayer(username);
            LoggerFactory.getLogger(this.getClass()).info("User (" + username + ") left the game: " + gameName);
            return new LobbyMessage("leave", username);
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).info("User (" + username + ") could not leave the game: " + gameName);
            return new LobbyMessage("error", "user could not be disconnected");
        }
    }
}
