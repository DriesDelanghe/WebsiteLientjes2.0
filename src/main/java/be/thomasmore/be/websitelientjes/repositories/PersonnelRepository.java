package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Page;
import be.thomasmore.be.websitelientjes.models.Personnel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonnelRepository extends CrudRepository<Personnel, Integer> {

    @Query("select p from Personnel p join p.pages pages where :page in (pages)")
    List<Personnel> getByPage(@Param("page") Page page);
}
