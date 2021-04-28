package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.ImageGoogle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageGoogleRepository extends CrudRepository<ImageGoogle, Integer> {

    @Query("select ig from ImageGoogle ig where :domain is null or ig.domain = :domain")
    List<ImageGoogle> getByDomain(@Param("domain") Domain domain);
}
