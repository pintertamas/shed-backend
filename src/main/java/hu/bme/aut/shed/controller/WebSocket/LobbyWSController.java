package hu.bme.aut.shed.controller.WebSocket;

import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyWSController {

    @SubscribeMapping("/subscribe")
    public String sendOneTimeMessage() {
        LoggerFactory.getLogger(this.getClass()).info("subscribed");
        return "server one-time message via the application";
    }
}
