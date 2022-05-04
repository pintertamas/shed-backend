package hu.bme.aut.shed.controller.WebSocket;

import hu.bme.aut.shed.dto.Request.ActionRequest;
import hu.bme.aut.shed.dto.Response.ActionResponse;
import hu.bme.aut.shed.dto.Response.CardResponse;
import hu.bme.aut.shed.dto.Response.Message;
import hu.bme.aut.shed.exception.*;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.service.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameProgressWSController {
    @Autowired
    private TableCardService tableCardService;
    @Autowired
    private PlayerCardService playerCardService;
    @Autowired
    private CardConfigService cardConfigService;
    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService playerService;

    @MessageMapping("/throw-a-card/{gameName}/{username}")
    @SendTo("/topic/{gameName}")
    public ActionResponse throwACard(@DestinationVariable String gameName, @DestinationVariable String username, ActionRequest actionRequest) {
        try {
            Game game = gameService.getGameByName(gameName);
            Player player = playerService.getPlayerByUsername(username);
            player.setCards(playerCardService.getPlayerCardsByPlayer(player)); //I set the players cards list for safety purposes

            if (game.getCurrentPlayer() != player) {
                throw new NotYourRoundException();
            }

            for (int i = 0; i < actionRequest.getCards().size(); i++) {
                TableCard lastThrowTableCard = tableCardService.getLastTableCard(TableCardState.THROW, game);
                CardConfig cardConfig = cardConfigService.getCardConfigById(actionRequest.getCards().get(i).getCardConfigId());
                PlayerCard playerCard = playerCardService.getPlayerCardByCardConfig(cardConfig);

                playerService.throwCard(player, lastThrowTableCard, playerCard);

                boolean fourLastSame = tableCardService.checkSameFourLastThrowTableCard(game);
                if (fourLastSame) {
                    tableCardService.removeAllTableCardByTableCardStateAndGame(TableCardState.THROW, game);
                }

                //tableCardService.removeById(lastPickTableCard.getId());
            }

            List<CardResponse> pickPlayerCards = new ArrayList<>();

            LoggerFactory.getLogger(this.getClass()).info("Player card size: " + player.getCards().size());
            while (player.getCards().size() != 3) {

                TableCard lastPickTableCard = tableCardService.getLastTableCard(TableCardState.PICK, game);
                playerService.pickCard(player, lastPickTableCard);

                CardResponse cardResponse = new CardResponse(lastPickTableCard.getCardConfig().getId(),
                        lastPickTableCard.getCardConfig().getNumber(),
                        lastPickTableCard.getCardConfig().getShape().getName(),
                        lastPickTableCard.getCardConfig().getRule().getName(),
                        lastPickTableCard.getCardConfig().getGame().getName(),
                        lastPickTableCard.getState().getName()
                );
                pickPlayerCards.add(cardResponse);

                tableCardService.removeById(lastPickTableCard.getId());
            }

            gameService.setNextPlayer(game);

            return new ActionResponse("valid", null, username, pickPlayerCards);

        } catch (GameNotFoundException exception) {
            LoggerFactory.getLogger(this.getClass()).info("Game with name (" + gameName + ") not found!");
            return new ActionResponse("invalid", new Message("error", exception.getMessage()), username, null);

        } catch (UserNotFoundException exception) {
            LoggerFactory.getLogger(this.getClass()).info("User with name (" + username + ") not found!");
            return new ActionResponse("invalid", new Message("error", exception.getMessage()), username, null);

        } catch (NotYourRoundException exception) {
            LoggerFactory.getLogger(this.getClass()).info("Not your round");
            return new ActionResponse("invalid", new Message("error", exception.getMessage()), username, null);

        } catch (CantThrowCardException exception) {
            LoggerFactory.getLogger(this.getClass()).info("Can't throw this card");
            return new ActionResponse("invalid", new Message("error", exception.getMessage()), username, null);

        } catch (CardConfigNotFound exception) {
            LoggerFactory.getLogger(this.getClass()).info("Card not found");
            return new ActionResponse("invalid", new Message("error", exception.getMessage()), username, null);

        } catch (Exception exception) {
            LoggerFactory.getLogger(this.getClass()).info("You Can't Play these Cards");
            return new ActionResponse("invalid", new Message("error", exception.getMessage()), username, null);
        }
    }

    @MessageMapping("/pick-a-card/{gameName}/{username}")
    @SendTo("/topic/{gameName}")
    public ActionResponse pickACard(@DestinationVariable String gameName, @DestinationVariable String username, ActionRequest actionRequest) {
        try {
            Game game = gameService.getGameByName(gameName);
            Player player = playerService.getPlayerByUsername(username);

            return null;
        } catch (GameNotFoundException exception) {
            LoggerFactory.getLogger(this.getClass()).info("Game with name (" + gameName + ") not found!");
            return new ActionResponse("invalid", new Message("error", exception.getMessage()), username, null);

        } catch (UserNotFoundException exception) {
            LoggerFactory.getLogger(this.getClass()).info("User with name (" + username + ") not found!");
            return new ActionResponse("invalid", new Message("error", exception.getMessage()), username, null);

        } catch (Exception exception) {
            LoggerFactory.getLogger(this.getClass()).info("You Can't Play these Cards");
            return new ActionResponse("invalid", new Message("error", exception.getMessage()), username, null);
        }
    }


}
