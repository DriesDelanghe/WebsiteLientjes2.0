package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends CrudRepository<Image, Integer> {

    @Query("select i from Image i where i.ImageLocation = :location")
    Optional<Image> getByImageLocation(@Param("location") String location);

    @Query("select distinct i from Image i where i in " +
            "(select p.image from Personnel p)")
    List<Image> getAllPersonnelImages();

    @Query("select distinct i from Image i where i in " +
            "(select ms.image from MenuSection ms)")
    List<Image> getAllMenuImages();
}
