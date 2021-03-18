package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Page;
import be.thomasmore.be.websitelientjes.models.TextFragment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextFragmentRepository extends CrudRepository<TextFragment, Integer> {

    @Query("select t from TextFragment t where t.page = :page and (:isHeaderText is null or t.headerText = :isHeaderText)")
    List<TextFragment> getByPageAndHeaderText(@Param("page") Page page,
                                              @Param("isHeaderText") Boolean isHeaderText);
}
