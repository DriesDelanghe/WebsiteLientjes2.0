package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Allergie;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.models.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query("select p from Product p " +
            "where :productName is not null " +
            "and lower(p.name) not like concat('%',lower(:productName),'%') " +
            "and p.menuSubSection in (select distinct mss from MenuSubSection mss where :menuSection is null or :menuSection in mss.menuSectionList) ")
    List<Product> getAllByProductNameAndGeneralSection(@Param("productName") String productName,
                                                       @Param("menuSection") MenuSection menuSection);

    @Query("select distinct p from Product p join p.allergies a join p.categories c " +
            "where not(:productName is null or lower(p.name) like concat('%',lower(:productName),'%')) " +
            "and not(:allergies is null or :allergies in (a)) " +
            "and not(:category is null or :category in (c))")
    List<Product> allProductsFilteredByIncludeCategoryAndNameExcludeAllergy(@Param("allergies") List<Allergie> allergies,
                                                                            @Param("category") Locale.Category category,
                                                                            @Param("menuSection") MenuSection menuSection,
                                                                            @Param("productName") String productName);

    @Query("select distinct p from Product p join p.allergies a join p.categories c " +
            "where not(:productName is null or lower(p.name) like concat('%',lower(:productName),'%')) " +
            "and not(:allergies is null or :allergies in (a)) " +
            "or (:category is null or :category in (c))")
    List<Product> allProductsFilteredByExcludeCategoryAndNameExcludeAllergy(@Param("allergies") List<Allergie> allergies,
                                                                            @Param("category") Locale.Category category,
                                                                            @Param("menuSection") MenuSection menuSection,
                                                                            @Param("productName") String productName);

}
