package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Allergie;
import be.thomasmore.be.websitelientjes.models.MenuSubSection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AllergieRepository extends CrudRepository<Allergie, Integer> {

    @Query("select a from Allergie a where a.name in (:allergieNames)")
    List<Allergie> getAllAllergiesByName(@Param("allergieNames") String[] allergieNames);

    @Query("select distinct a from Allergie a join a.products p " +
            "where p in (" +
            "select distinct p from Product p where p.menuSubSection in :menuSubSections )")
    List<Allergie> getAllAllergiesByMenuSubSections(@Param("menuSubSections") List<MenuSubSection> menuSubSections);

}
