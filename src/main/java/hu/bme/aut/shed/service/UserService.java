package hu.bme.aut.shed.service;

import hu.bme.aut.shed.component.JwtTokenUtil;
import hu.bme.aut.shed.exception.NotValidUpdateException;
import hu.bme.aut.shed.exception.UserAlreadyExistsException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.dto.Request.JwtRequest;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    EmailService emailService;

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
        if (userRepository.findByUsername(newUser.getUsername()) != null || userRepository.findByEmail(newUser.getEmail()) != null) {
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

    public User getByEmail(String email) throws UserNotFoundException {
        User userData = userRepository.findByEmail(email);
        if (userData == null){
            throw new UserNotFoundException();
        }
        else{
            return userData;
        }
    }

    public User getByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        } else {
            return user;
        }
    }

    @Transactional
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
    public User changePassword(String email,String password) throws UserNotFoundException {
        User user = getByEmail(email);
        user.setPassword(bcryptEncoder.encode(password));
        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) throws Exception {
        if (userRepository.findById(id).isEmpty())
            throw new UserNotFoundException();
        else
            userRepository.deleteById(id);
    }

    public void checkAvailability(String username, String email) throws UserAlreadyExistsException {
        User user = userRepository.findByUsername(username);
        if (user != null) throw new UserAlreadyExistsException(user);
        user = userRepository.findByEmail(email);
        if (user != null) throw new UserAlreadyExistsException(user);
    }
}
