package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Address;
import be.thomasmore.be.websitelientjes.models.Domain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends CrudRepository<Address, Integer> {

    @Query("select a from Address a where :domain is null or a.domain = :domain")
    Address getByDomain(@Param("domain") Domain domain);
}
