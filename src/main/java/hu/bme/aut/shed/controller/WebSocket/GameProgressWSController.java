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
import java.util.Objects;
import java.util.UUID;

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

            LoggerFactory.getLogger(this.getClass()).info("Player : " + player.getUsername() + player.getId());
            LoggerFactory.getLogger(this.getClass()).info("CurrentPlayer : " + player.getGame().getCurrentPlayer().getUsername() + player.getGame().getCurrentPlayer().getId());
            if (!Objects.equals(game.getCurrentPlayer().getId(), player.getId())) {
                throw new NotYourRoundException();
            }

            if (actionRequest.getCards().size() == 0) {
                throw new NoCardsSelectedException();
            }

            for (int i = 0; i < actionRequest.getCards().size(); i++) {
                TableCard lastThrowTableCard = tableCardService.getLastTableCard(TableCardState.THROW, game);
                CardConfig cardConfig = cardConfigService.getCardConfigById(actionRequest.getCards().get(i).getCardConfigId());//Checked if its real if its not then throw error
                PlayerCard playerCard = playerCardService.getPlayerCardByCardConfig(cardConfig);//Checked if its real if not then throw error

                playerService.throwCard(player, lastThrowTableCard, playerCard);
                LoggerFactory.getLogger(this.getClass()).info(String.valueOf("Card has been thrown : " + playerCard.getId()));

                boolean fourLastSame = tableCardService.checkSameFourLastThrowTableCard(game);
                LoggerFactory.getLogger(this.getClass()).info(String.valueOf("FourLastSame : " + fourLastSame));
                if (fourLastSame) {
                    tableCardService.removeAllTableCardByTableCardStateAndGame(TableCardState.THROW, game);
                }
                LoggerFactory.getLogger(this.getClass()).info(String.valueOf("idaig 7"));
                //tableCardService.removeById(lastPickTableCard.getId());
            }

            List<CardResponse> pickPlayerCards = new ArrayList<>();
            LoggerFactory.getLogger(this.getClass()).info("Player card size: " + player.getCards().size());
            List<PlayerCard> InHandsCards = playerCardService.getAllPlayerCardsByPlayerAndState(player, PlayerCardState.HAND);
            LoggerFactory.getLogger(this.getClass()).info("Player HandCard size: " + InHandsCards.size());

            while (InHandsCards.size() < 3) { //Pick up cards for the player while he have at least 3 cards in his hands

                if (tableCardService.getAllByTableCardStateAndGame(TableCardState.PICK, game).size() == 0) { //If no more pick card then ha cant pick up cards
                    break;
                }

                TableCard lastPickTableCard = tableCardService.getLastTableCard(TableCardState.PICK, game);
                playerService.pickCard(player, lastPickTableCard);
                InHandsCards = playerCardService.getAllPlayerCardsByPlayerAndState(player, PlayerCardState.HAND);

                LoggerFactory.getLogger(this.getClass()).info(String.valueOf("Card has been drawn : " + lastPickTableCard.getId()));
                LoggerFactory.getLogger(this.getClass()).info("Player HandCard size: " + InHandsCards.size());
                CardResponse cardResponse = new CardResponse(lastPickTableCard.getCardConfig().getId(),
                        lastPickTableCard.getCardConfig().getNumber(),
                        lastPickTableCard.getCardConfig().getShape().getName(),
                        lastPickTableCard.getCardConfig().getRule().getName(),
                        lastPickTableCard.getCardConfig().getGame().getName(),
                        lastPickTableCard.getState().getName()
                );

                pickPlayerCards.add(cardResponse);
            }

            playerService.checkEndCondition(player,game);
            //gameService.checkEndCondition(game);

            gameService.setNextPlayer(game);
            LoggerFactory.getLogger(this.getClass()).info(String.valueOf("NextPlayer :" + game.getCurrentPlayer()));
            LoggerFactory.getLogger(this.getClass()).info(String.valueOf("ThrowCardMethodEnds"));
            return new ActionResponse(UUID.randomUUID().toString(), "valid", null, username, pickPlayerCards);

        } catch (GameNotFoundException exception) {
            LoggerFactory.getLogger(this.getClass()).info("Game with name (" + gameName + ") not found!");
            return new ActionResponse(UUID.randomUUID().toString(), "invalid", new Message("error", exception.getMessage()), username, null);

        } catch (UserNotFoundException exception) {
            LoggerFactory.getLogger(this.getClass()).info("User with name (" + username + ") not found!");
            return new ActionResponse(UUID.randomUUID().toString(), "invalid", new Message("error", exception.getMessage()), username, null);

        } catch (NotYourRoundException exception) {
            LoggerFactory.getLogger(this.getClass()).info("Not your round");
            return new ActionResponse(UUID.randomUUID().toString(), "invalid", new Message("error", exception.getMessage()), username, null);

        } catch (CantThrowCardException exception) {
            LoggerFactory.getLogger(this.getClass()).info("Can't throw this card");
            return new ActionResponse(UUID.randomUUID().toString(), "invalid", new Message("error", exception.getMessage()), username, null);

        } catch (CardConfigNotFound exception) {
            LoggerFactory.getLogger(this.getClass()).info("Card not found");
            return new ActionResponse(UUID.randomUUID().toString(), "invalid", new Message("error", exception.getMessage()), username, null);

        } catch (Exception exception) {
            LoggerFactory.getLogger(this.getClass()).info("You Can't Play these Cards");
            LoggerFactory.getLogger(this.getClass()).info(exception.getMessage());
            return new ActionResponse(UUID.randomUUID().toString(), "invalid", new Message("error", exception.getMessage()), username, null);
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
            return new ActionResponse(UUID.randomUUID().toString(), "invalid", new Message("error", exception.getMessage()), username, null);

        } catch (UserNotFoundException exception) {
            LoggerFactory.getLogger(this.getClass()).info("User with name (" + username + ") not found!");
            return new ActionResponse(UUID.randomUUID().toString(), "invalid", new Message("error", exception.getMessage()), username, null);

        } catch (Exception exception) {
            LoggerFactory.getLogger(this.getClass()).info("You Can't Play these Cards");
            return new ActionResponse(UUID.randomUUID().toString(), "invalid", new Message("error", exception.getMessage()), username, null);
        }
    }


}
