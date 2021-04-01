package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends CrudRepository<Page, Integer> {

    @Query("select p from Page p where (:domain is null or p.domain = :domain) and (:pageName is null or lower(p.pageName) like lower(:pageName))")
    Page getByDomainAndPageName(@Param("domain")Domain domain,
                                @Param("pageName") String pageName);

    @Query("select p from Page p where (:domain is null or p.domain = :domain) and (:pageId is null or p.id = :pageId)")
    Page getByDomainAndPageId(@Param("domain")Domain domain,
                              @Param("pageId") Integer pageId);
}
