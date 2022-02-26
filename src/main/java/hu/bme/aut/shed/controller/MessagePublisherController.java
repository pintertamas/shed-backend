package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.config.RabbitMQConfig;
import hu.bme.aut.shed.model.CustomMessage;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class MessagePublisherController {

    @Autowired
    private RabbitTemplate template;

    @PostMapping("/publish")
    public String publishMessage(@RequestParam String channelID, @RequestBody CustomMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageDate(new Date());
        template.convertAndSend(RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY + channelID, message);

        LoggerFactory.getLogger(MessagePublisherController.class).info("Message received: " + message);
        return message.toString();
    }
}