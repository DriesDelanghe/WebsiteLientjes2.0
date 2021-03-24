package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Allergie;
import be.thomasmore.be.websitelientjes.models.MenuSubSection;
import be.thomasmore.be.websitelientjes.models.ProductCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends CrudRepository<ProductCategory, Integer> {

    @Query("select c from ProductCategory c where c.name in (:categoryNames)")
    List<ProductCategory> getAllCategoryByName(@Param("categoryNames") String[] categoryNames);

    @Query("select distinct c from ProductCategory c join c.products p " +
            "where p in (" +
            "select distinct p from Product p where p.menuSubSection in :menuSubSections )")
    List<ProductCategory> getAllCategoryByMenuSubSections(@Param("menuSubSections") List<MenuSubSection> menuSubSections);
}
