package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.component.JwtTokenUtil;
import hu.bme.aut.shed.exception.UserAlreadyExistsException;
import hu.bme.aut.shed.dto.Request.JwtRequest;
import hu.bme.aut.shed.dto.Response.JwtResponse;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.service.EmailService;
import hu.bme.aut.shed.service.OtpService;
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
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

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

    @RequestMapping(value = "/check-availability", method = RequestMethod.GET)
    public ResponseEntity<?> checkAvailability(@RequestParam String username, @RequestParam String email) {
        try {
            userService.checkAvailability(username, email);
            LoggerFactory.getLogger(this.getClass()).info("USERNAME AND EMAIL ARE AVAILABLE");
            return ResponseEntity.ok("username and email is available");
        } catch (Exception exception) {
            LoggerFactory.getLogger(this.getClass()).error("USER ALREADY EXISTS");
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @RequestMapping(value = "/generate-otp", method = RequestMethod.POST)
    public ResponseEntity<?> generateOtp(@RequestBody String email) {
        try {
            int otp = otpService.generateOTP(email);
            if (!emailService.sendRegistrationMessage(email, otp))
                throw new Exception("Could not send one time password to: " + email);
            LoggerFactory.getLogger(this.getClass()).info("EMAIL WITH ONE TIME PASSWORD SUCCESSFULLY SENT TO: " + email);
            return ResponseEntity.ok("one time password sent to " + email);
        } catch (Exception exception) {
            LoggerFactory.getLogger(this.getClass()).error("COULD NOT SEND ONE TIME PASSWORD TO: " + email);
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@Valid @RequestBody User newUser, @RequestParam int otp) {
        try {
            if (!otpService.validateOtp(newUser.getEmail(), otp))
                throw new Exception("wrong one time password");
            User user = userService.register(newUser);
            LoggerFactory.getLogger(this.getClass()).info("USER CREATED: " + newUser);
            emailService.sendMessage(newUser.getEmail(),"Registration successful",
                    "Welcome to Shed " + newUser.getUsername() + "!"
                            + "\n"
                            +"Good Luck Have Fun"
                            + "\n"
                            + "\n"
                            + "Greetings"
                            + "\n"
                            + "Shed team"
                    );
            return ResponseEntity.ok(user);
        } catch (UserAlreadyExistsException exception) {
            LoggerFactory.getLogger(this.getClass()).error("USER ALREADY EXISTS: " + exception.getExistingUser());
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (Exception exception) {
            LoggerFactory.getLogger(this.getClass()).error("WRONG ONE TIME PASSWORD!");
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @RequestMapping(value = "/check-token-validity", method = RequestMethod.GET)
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
