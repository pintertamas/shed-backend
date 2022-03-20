package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.NotValidUpdateException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getById(Long id) throws UserNotFoundException {
        Optional<User> userData = userRepository.findById(id);
        if(userData.isPresent())
            return userData.get();
        else
            throw new UserNotFoundException();
    }

    public User getByUsername(String username , boolean wanted_by_auth) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null && !wanted_by_auth) {
            throw new UserNotFoundException();
        }else{
            return user;
        }
    }

    public User updateById(Long id,User editedUser) throws Exception {
        Optional<User> userData = userRepository.findById(editedUser.getId());

        if(userData.isEmpty()) {
            throw new UserNotFoundException();
        }
        else {
            if(!Objects.equals(editedUser.getId(), id)) {
                throw new NotValidUpdateException();
            }
            else {
                User user = userData.get();
                return userRepository.save(user);
            }
        }
    }

    public void deleteById(Long id) throws Exception {
        if(userRepository.findById(id).isEmpty())
            throw new UserNotFoundException();
        else
            userRepository.deleteById(id);
    }
}
