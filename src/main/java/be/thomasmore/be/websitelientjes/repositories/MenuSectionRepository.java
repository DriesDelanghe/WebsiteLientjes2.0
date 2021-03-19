package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.MenuSection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuSectionRepository extends CrudRepository<MenuSection, Integer> {
}
