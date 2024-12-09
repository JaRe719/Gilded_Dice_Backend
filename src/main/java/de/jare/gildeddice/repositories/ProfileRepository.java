package de.jare.gildeddice.repositories;

import de.jare.gildeddice.entities.users.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
    Profile findByUsername(String username);

}
