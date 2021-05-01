package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.ContactType;
import be.thomasmore.be.websitelientjes.models.RedirectEmail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RedirectEmailRepository extends CrudRepository<RedirectEmail, Integer> {

    @Query("select distinct re from RedirectEmail re join re.contactTypeList cl where :contactType is null or :contactType in (cl)")
    List<RedirectEmail> getByContactType(@Param("contactType") ContactType contactType);

}
