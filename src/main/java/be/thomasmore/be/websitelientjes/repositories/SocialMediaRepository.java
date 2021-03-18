package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.SocialMedia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SocialMediaRepository extends CrudRepository<SocialMedia, Integer> {

    @Query("select sm from SocialMedia sm where sm.domain = :domain")
    List<SocialMedia> findByDomain(@Param("domain") Domain domain);
}
