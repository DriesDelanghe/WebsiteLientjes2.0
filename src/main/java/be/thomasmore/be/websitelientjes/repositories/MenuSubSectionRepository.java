package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.MenuSubSection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuSubSectionRepository extends CrudRepository<MenuSubSection, Integer> {
}
