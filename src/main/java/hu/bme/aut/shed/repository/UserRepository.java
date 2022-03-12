package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByID(String s);
    User findByUsername(String Username);
}
