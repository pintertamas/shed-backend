package hu.bme.aut.shed.repository;

import hu.bme.aut.shed.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String Username);

    Optional<User> findById(Long id);
}
