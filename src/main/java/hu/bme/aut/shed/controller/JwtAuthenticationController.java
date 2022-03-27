package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.component.JwtTokenUtil;
import hu.bme.aut.shed.exception.UserAlreadyExistsException;
import hu.bme.aut.shed.model.JwtRequest;
import hu.bme.aut.shed.model.JwtResponse;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.service.UserService;
import org.apache.juli.logging.LogFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.security.auth.login.LoginException;
import javax.validation.Valid;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        String token;
        try {
            token = userService.login(authenticationRequest);
        } catch (LoginException e) {
            LogFactory.getLog(this.getClass()).error("ERROR AT LOGIN");
            return new ResponseEntity<>("Could not reach database", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LogFactory.getLog(this.getClass()).info("NEW LOGIN");
        return ResponseEntity.ok(new JwtResponse(token, userService.getByUsername(authenticationRequest.getUsername())));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@Valid @RequestBody User newUser) {
        try {
            User user = userService.register(newUser);
            LoggerFactory.getLogger(this.getClass()).info("USER CREATED: " + newUser);
            return ResponseEntity.ok(user);
        } catch (UserAlreadyExistsException exception) {
            LoggerFactory.getLogger(this.getClass()).error("USER ALREADY EXISTS: " + exception.getExistingUser());
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/check-token-validity")
    public ResponseEntity<?> isTokenExpired() {
        String token = JwtTokenUtil.getToken();

        try {
            boolean isExpired = jwtTokenUtil.isTokenExpired(token);
            LoggerFactory.getLogger(this.getClass()).info("TOKEN IS " + (isExpired ? "EXPIRED" : "NOT EXPIRED"));
            if (isExpired) {
                return new ResponseEntity<>("Token is not valid", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Token is valid", HttpStatus.OK);
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).error(e.getMessage());
            LoggerFactory.getLogger(this.getClass()).error("ERROR CHECKING TOKEN EXPIRY");
            return ResponseEntity.internalServerError().body("Error checking token expiry");
        }
    }
}
