package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Icon;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IconRepository extends CrudRepository<Icon, Integer> {

    @Query("select i from Icon i where i in (select sm.icon from SocialMedia sm)")
    List<Icon> getSocialMediaIcons();

    @Query("select i from Icon i where i in (select ic.icon from ContactInfo ic)")
    List<Icon> getInfoContentIcons();
}
