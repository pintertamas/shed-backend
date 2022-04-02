package hu.bme.aut.shed.controller.WebSocket;

import hu.bme.aut.shed.dto.Response.StartGameMessage;
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

    @SubscribeMapping("/subscribe")
    public String sendOneTimeMessage() {
        LoggerFactory.getLogger(this.getClass()).info("subscribed");
        return "server one-time message via the application";
    }

    @MessageMapping("/start-game/{gameName}")
    @SendTo("/topic/{gameName}")
    public StartGameMessage startGame(@DestinationVariable String gameName) {
        LoggerFactory.getLogger(this.getClass()).info("GameStarted : {}", gameName);
        return new StartGameMessage("Echo Tomi ezt kérte",gameName);
    }
}
