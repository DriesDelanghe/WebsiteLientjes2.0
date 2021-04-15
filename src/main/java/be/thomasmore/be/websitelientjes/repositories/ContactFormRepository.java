package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.ContactForm;
import be.thomasmore.be.websitelientjes.models.ContactType;
import be.thomasmore.be.websitelientjes.models.Domain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactFormRepository extends CrudRepository<ContactForm, Integer> {

    @Query("select cf from ContactForm cf where cf.contactType = :contactType")
    List<ContactForm> getByContactType(@Param("contactType") ContactType contactType);

    @Query("select cf from ContactForm cf where (:contactType is null or cf.contactType = :contactType) and cf.read = false")
    List<ContactForm> getUnreadBycontactType(@Param("contactType") ContactType contactType);
}
