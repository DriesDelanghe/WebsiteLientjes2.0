package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.ContactInfo;
import be.thomasmore.be.websitelientjes.models.Domain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactInfoRepository extends CrudRepository<ContactInfo, Integer> {

    @Query("Select ci from ContactInfo ci where ci.domain = :domain")
    List<ContactInfo> getByDomain(@Param("domain") Domain domain);
}
