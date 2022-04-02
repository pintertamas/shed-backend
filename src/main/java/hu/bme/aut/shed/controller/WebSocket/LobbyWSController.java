package hu.bme.aut.shed.controller.WebSocket;

import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyWSController {

    @SubscribeMapping("/subscribe")
    public String sendOneTimeMessage() {
        return "server one-time message via the application";
    }
}
