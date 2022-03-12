package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByID(String s);
    User findByUsername(String Username);
}