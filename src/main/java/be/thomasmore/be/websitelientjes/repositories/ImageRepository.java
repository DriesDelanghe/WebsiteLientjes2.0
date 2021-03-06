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

    @Query("select distinct i from Image i where i.ImageLocation like concat('%', '/personnel/', '%') ")
    List<Image> getAllPersonnelImages();

    @Query("select distinct i from Image i where i.ImageLocation like concat('%', '/food/', '%') ")
    List<Image> getAllMenuImages();

    @Query("select distinct i from Image i where i.ImageLocation like '%/google/%' and i not in (select ig.image from ImageGoogle ig join ig.image) ")
    List<Image> getAllGoogleImages();

    @Query("select distinct i from Image i where i not in (select ms.image from MenuSection ms join ms.image) " +
            "and i not in (select p.image from Page p join p.image) " +
            "and i not in (select pers.image from Personnel pers join pers.image) " +
            "and i not in (select ig.image from ImageGoogle ig join ig.image)")
    List<Image> getUnusedImages();
}
