package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.Openingsuur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpeningsuurRepository extends CrudRepository<Openingsuur, Integer> {

    @Query("select o from Openingsuur o where o.domain = :domain")
    List<Openingsuur> getByDomain(@Param("domain") Domain domain);
}
