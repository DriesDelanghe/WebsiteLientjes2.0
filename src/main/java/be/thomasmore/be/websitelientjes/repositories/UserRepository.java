package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
