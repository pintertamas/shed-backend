package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.exception.UserAlreadyExistsException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.JwtRequest;
import hu.bme.aut.shed.model.JwtResponse;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.service.JwtUserDetailsService;
import hu.bme.aut.shed.service.UserService;
import org.apache.juli.logging.LogFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


import javax.security.auth.login.LoginException;
import javax.validation.Valid;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUserDetailsService userDetailsService;

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
        } catch (UserAlreadyExistsException uaee) {
            LoggerFactory.getLogger(this.getClass()).error("USER ALREADY EXISTS: " + uaee.getExistingUser());
            return ResponseEntity.badRequest().body(uaee.getMessage());
        }
    }
}
