package de.jare.gildeddice.repositories;

import de.jare.gildeddice.entities.users.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
