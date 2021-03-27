package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.ContactType;
import be.thomasmore.be.websitelientjes.models.Domain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactTypeRepository extends CrudRepository<ContactType, Integer> {

    @Query("select ct from ContactType ct where :domain is null or ct.domain = :domain")
    List<ContactType> getByDomain(@Param("domain")Domain domain);
}
