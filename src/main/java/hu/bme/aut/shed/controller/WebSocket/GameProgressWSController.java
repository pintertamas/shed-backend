package hu.bme.aut.shed.controller.WebSocket;

import hu.bme.aut.shed.service.GameService;
import hu.bme.aut.shed.service.PlayerCardService;
import hu.bme.aut.shed.service.TableCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameProgressWSController {
    @Autowired
    private TableCardService tableCardService;
    @Autowired
    private PlayerCardService playerCardService;
    @Autowired
    private GameService gameService;

    @MessageMapping("/throw-a-card/{gameName}/{username}")
    @SendTo("/topic/{gameName}")
    public void throwACard(@DestinationVariable String gameName, @DestinationVariable String username) {

    }


}
