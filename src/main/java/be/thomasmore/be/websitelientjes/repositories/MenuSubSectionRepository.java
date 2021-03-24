package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.MenuSubSection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuSubSectionRepository extends CrudRepository<MenuSubSection, Integer> {

    @Query("select mss from MenuSubSection mss join mss.menuSectionList ms join ms.domain d " +
            " where d = :domain")
    List<MenuSubSection> getByDomain(@Param("domain") Domain domain);
}
