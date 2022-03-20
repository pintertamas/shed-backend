package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.component.JwtTokenUtil;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.JwtRequest;
import hu.bme.aut.shed.model.JwtResponse;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.repository.UserRepository;
import hu.bme.aut.shed.service.JwtUserDetailsService;
import hu.bme.aut.shed.service.UserService;
import org.apache.juli.logging.LogFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
        User user = jwtTokenUtil.getUserFromToken(token);
        LogFactory.getLog(this.getClass()).info("NEW LOGIN: " + user.toString());

        return ResponseEntity.ok(new JwtResponse(token, user));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@Valid @RequestBody User newUser) {
        try {
            User user = userService.getByUsername(newUser.getUsername(),true);
            if (user != null) {
                LoggerFactory.getLogger(this.getClass()).error("USER ALREADY EXISTS: " + user);
                return ResponseEntity.badRequest().body("User with this username already exists");
            }
            LoggerFactory.getLogger(this.getClass()).info("USER CREATED: " + newUser);
            return ResponseEntity.ok(userDetailsService.save(newUser));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("User does not exist");
        }
    }
}
