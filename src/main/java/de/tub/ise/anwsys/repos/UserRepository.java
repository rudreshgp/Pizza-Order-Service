package de.tub.ise.anwsys.repos;

import de.tub.ise.anwsys.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByName(String name);
}
