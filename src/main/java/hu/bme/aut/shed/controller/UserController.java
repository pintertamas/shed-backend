package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.dto.Request.OtpRequest;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.service.OtpService;
import hu.bme.aut.shed.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @RequestMapping(value = "/", method = {RequestMethod.GET}, produces = "application/json")
    public ResponseEntity<User> getUserById(@RequestParam Long id) {
        try {
            User user = userService.getById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (UserNotFoundException exception){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/edit/", method = {RequestMethod.PUT}, produces = "application/json")
    public ResponseEntity<User> updateUser(@RequestParam Long id,@RequestBody User editedUser) {
        try{
            User user = userService.updateById(id,editedUser);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/change-password", method = {RequestMethod.PUT}, produces = "application/json")
    public ResponseEntity<?> changePassword(@RequestBody OtpRequest otpRequest) {
        try{
            if (!otpService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp()))
                throw new Exception("wrong one time password");
            User user = userService.getByEmail(otpRequest.getEmail());
            LoggerFactory.getLogger(this.getClass()).info("USERNAME" + user.getUsername());
            userService.updateById(user.getId(), user);
            return new ResponseEntity<>("Successful password changing", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delete/", method = {RequestMethod.DELETE}, produces = "application/json")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam Long id) {
        try {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
