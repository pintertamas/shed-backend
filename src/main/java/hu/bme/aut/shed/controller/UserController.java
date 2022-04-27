package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.component.JwtTokenUtil;
import hu.bme.aut.shed.dto.Request.OtpRequest;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.service.OtpService;
import hu.bme.aut.shed.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(value = "/", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<?> getUserById(@RequestParam Long id) {
        try {
            User user = userService.getById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (UserNotFoundException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/edit/", method = {RequestMethod.PUT}, produces = "application/json")
    public ResponseEntity<?> updateUser(@RequestParam Long id,@RequestBody User editedUser) {
        try{
            String token = JwtTokenUtil.getToken();
            User currentUser = jwtTokenUtil.getUserFromToken(token);
            if (!editedUser.getId().equals(currentUser.getId())){
                throw new AuthorizationServiceException("You dont have permission to make changes");
            }
            User user = userService.updateById(id,editedUser);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/change-password", method = {RequestMethod.PUT}, produces = "application/json")
    public ResponseEntity<?> changePassword(@RequestBody OtpRequest otpRequest) {
        try{
            if (!otpService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp())){
                throw new Exception("wrong one time password");
            }
            User user = userService.changePassword(otpRequest.getEmail(),otpRequest.getPassword());
            LoggerFactory.getLogger(this.getClass()).info("Password changed for: " + user.getUsername());
            return new ResponseEntity<>("Successful password changing", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delete/", method = {RequestMethod.DELETE}, produces = "application/json")
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        try {
            String token = JwtTokenUtil.getToken();
            User currentUser = jwtTokenUtil.getUserFromToken(token);
            if (!userService.getById(id).getId().equals(currentUser.getId())){
                throw new AuthorizationServiceException("You dont have permission to make changes");
            }
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
