package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.TelephoneNumber;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TelephoneNumberRepository extends CrudRepository<TelephoneNumber, Integer> {

    @Query("select tn from TelephoneNumber tn where :domain is null or :domain = tn.domain")
    TelephoneNumber getTelephoneNumberByDomain(@Param("domain") Domain domain);
}
