package hu.bme.aut.shed.controller;

import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<User> getUserById(@RequestParam String id) {
        Optional<User> userData = userRepository.findByID(id);
        if(userData.isPresent()){
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/edit/")
    public ResponseEntity<User> updateUser(@RequestParam String id,@Valid @RequestBody User editedUser) {
        Optional<User> userData = userRepository.findById(editedUser.getID());
        if (userData.isPresent() && (Objects.equals(id, editedUser.getID()))) {
            return new ResponseEntity<>(userRepository.save(editedUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam String id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> saveUser(@Valid @RequestBody User newUser) {
        User user = userRepository.findByUsername(newUser.getUsername());
        if (user != null) {
            LoggerFactory.getLogger(this.getClass()).error("USER ALREADY EXISTS: " + user);
            return ResponseEntity.badRequest().body("User with this username already exists");
        }
        LoggerFactory.getLogger(this.getClass()).info("USER CREATED: " + newUser);
        return ResponseEntity.ok(userRepository.save(newUser));
    }
}
