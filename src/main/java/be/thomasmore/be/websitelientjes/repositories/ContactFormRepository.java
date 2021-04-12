package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.ContactForm;
import be.thomasmore.be.websitelientjes.models.ContactType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactFormRepository extends CrudRepository<ContactForm, Integer> {

    @Query("select cf from ContactForm cf where cf.contactType = :contactType")
    List<ContactForm> getByContactType(@Param("contactType") ContactType contactType);
}
