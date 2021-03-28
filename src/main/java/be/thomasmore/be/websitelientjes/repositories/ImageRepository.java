package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImageRepository extends CrudRepository<Image, Integer> {

    @Query("select i from Image i where i.ImageLocation = :location")
    Optional<Image> getByImageLocation(@Param("location") String location);
}