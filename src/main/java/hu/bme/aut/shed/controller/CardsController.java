package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.component.JwtTokenUtil;
import hu.bme.aut.shed.dto.Response.CardResponse;
import hu.bme.aut.shed.exception.GameNotFoundException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.*;
import hu.bme.aut.shed.service.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/cards")
public class CardsController {

    @Autowired
    private PlayerCardService playerCardService;
    @Autowired
    private GameService gameService;
    @Autowired
    private TableCardService tableCardService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private CardConfigService cardConfigService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(value = "/player/{username}", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> getPlayerCards(@PathVariable String username) {
        try {
            String token = JwtTokenUtil.getToken();
            User currentUser = jwtTokenUtil.getUserFromToken(token);
            if (!username.equals(currentUser.getUsername())) {
                throw new AuthorizationServiceException("You dont have permission to make changes");
            }

            List<PlayerCard> playerCards = playerService.getPlayerCardsByUsername(username);
            List<CardResponse> responseList = new ArrayList<>();
            for (PlayerCard playerCard : playerCards){
                CardResponse response = new CardResponse(playerCard.getCardConfig().getShape(),
                                                         playerCard.getCardConfig().getRule().getName(),
                                                         playerCard.getCardConfig().getGame().getName(),
                                                         playerCard.getCardConfig().getNumber());
                responseList.add(response);
            }
            return new ResponseEntity<>(responseList,HttpStatus.OK);

        } catch (UserNotFoundException exception){
            LoggerFactory.getLogger(this.getClass()).info(username + ": Not Found");
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception exception){
            LoggerFactory.getLogger(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(value = "/table/{gameName}/{tableCardState}", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> getTableCards(@PathVariable String gameName,@PathVariable String tableCardState) {
        try {
            Game game = gameService.getGameByName(gameName);
            List<TableCard> tableCards = tableCardService.getAllByTableCardStateAndGame(TableCardState.fromName(tableCardState),game);
            List<CardResponse> responseList = new ArrayList<>();
            for (TableCard tableCard : tableCards){
                CardResponse response = new CardResponse(tableCard.getCardConfig().getShape(),
                        tableCard.getCardConfig().getRule().getName(),
                        tableCard.getCardConfig().getGame().getName(),
                        tableCard.getCardConfig().getNumber());
                responseList.add(response);
            }
            return new ResponseEntity<>(responseList,HttpStatus.OK);

        } catch (GameNotFoundException exception){
            LoggerFactory.getLogger(this.getClass()).info(gameName + ": Not Found");
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception exception){
            LoggerFactory.getLogger(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
