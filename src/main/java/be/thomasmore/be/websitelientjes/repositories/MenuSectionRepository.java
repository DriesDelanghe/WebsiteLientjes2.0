package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuSectionRepository extends CrudRepository<MenuSection, Integer> {

    @Query("select ms from MenuSection ms where ms.domain = :domain")
    List<MenuSection> getByDomain(@Param("domain") Domain domain);

}
