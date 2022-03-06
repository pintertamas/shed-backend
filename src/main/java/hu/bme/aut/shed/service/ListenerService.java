package hu.bme.aut.shed.service;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ListenerService {
    @RabbitListener(queues = "messageQueue")
    public void listenMessage(String message) {
        LoggerFactory.getLogger(ListenerService.class).info("Event consumed : " + message);
    }
}