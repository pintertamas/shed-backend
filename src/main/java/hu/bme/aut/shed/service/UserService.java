package hu.bme.aut.shed.service;

import hu.bme.aut.shed.component.JwtTokenUtil;
import hu.bme.aut.shed.exception.NotValidUpdateException;
import hu.bme.aut.shed.exception.UserAlreadyExistsException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.JwtRequest;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    UserRepository userRepository;

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public String login(JwtRequest authenticationRequest) throws Exception {
        User existingUser = userRepository.findByUsername(authenticationRequest.getUsername());
        if (existingUser == null) throw new BadCredentialsException("Could not find a user with this username");
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        return jwtTokenUtil.generateToken(userDetails);
    }

    public User register(User newUser) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(newUser.getUsername()) != null) {
            throw new UserAlreadyExistsException(newUser);
        }
        newUser.setPassword(bcryptEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        return newUser;
    }

    public User getById(Long id) throws UserNotFoundException {
        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent())
            return userData.get();
        else
            throw new UserNotFoundException();
    }

    public User getByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        } else {
            return user;
        }
    }

    public User updateById(Long id, User editedUser) throws Exception {
        Optional<User> userData = userRepository.findById(editedUser.getId());

        if (userData.isEmpty()) {
            throw new UserNotFoundException();
        } else {
            if (!Objects.equals(editedUser.getId(), id)) {
                throw new NotValidUpdateException();
            } else {
                User user = userData.get();
                return userRepository.save(user);
            }
        }
    }

    public void deleteById(Long id) throws Exception {
        if (userRepository.findById(id).isEmpty())
            throw new UserNotFoundException();
        else
            userRepository.deleteById(id);
    }
}
