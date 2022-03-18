package hu.bme.aut.shed.service;

import hu.bme.aut.shed.exception.NotValidUpdateException;
import hu.bme.aut.shed.exception.UserNotFoundException;
import hu.bme.aut.shed.model.User;
import hu.bme.aut.shed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getById(Long Id) throws UserNotFoundException {
        Optional<User> userData = userRepository.findById(Id);
        if(userData.isPresent())
            return userData.get();
        else
            throw new UserNotFoundException();
    }

    public User updateById(Long Id,User editedUser) throws Exception {
        Optional<User> userData = userRepository.findById(editedUser.getId());

        if(userData.isEmpty()) {
            throw new UserNotFoundException();
        }
        else {
            if(!Objects.equals(editedUser.getId(), Id)) {
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
