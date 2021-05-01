package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.Allergie;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.models.Product;
import be.thomasmore.be.websitelientjes.models.ProductCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query("select distinct p from Product p left join p.allergies a " +
            "where ((:allergies) is null or (a) in (:allergies)) " +
            "and p.menuSubSection in (select mss from MenuSubSection mss join mss.menuSectionList msl where :menuSection is null or :menuSection in (msl))")
    List<Product> filterOnAllergie(@Param("allergies") List<Allergie> allergies,
                                   @Param("menuSection")MenuSection menuSection);

    @Query("select distinct p from Product p left join p.allergies a " +
            "where (:productName is null or lower(p.name) not like concat('%', lower(:productName), '%')) " +
            "and ((:allergies) is null or (a) in (:allergies)) " +
            "and p.menuSubSection in (select mss from MenuSubSection mss join mss.menuSectionList msl where :menuSection is null or :menuSection in (msl))")
    List<Product> filterOnAllergieAndName(@Param("allergies") List<Allergie> allergies,
                                          @Param("productName") String productName,
                                          @Param("menuSection") MenuSection menuSection);

    @Query("select distinct p from Product p left join p.categories c " +
            "where ((:productList) is empty p not in (:productList)) and ((:categorylist) is not empty and (c) in (:categoryList)) " +
            "and p.menuSubSection in (select mss from MenuSubSection mss join mss.menuSectionList msl where :menuSection is null or :menuSection in (msl))")
    List<Product> filterListOnCategory(@Param("productList") List<Product> productList,
                                       @Param("categoryList") List<ProductCategory> hiddenCategoryList,
                                       @Param("menuSection") MenuSection menuSection);

    @Query("select distinct p from Product p left join p.categories c " +
            "where (c) in (:categoryList) " +
            "and p.menuSubSection in (select mss from MenuSubSection mss join mss.menuSectionList msl where :menuSection is null or :menuSection in (msl))")
    List<Product> filterOnlyOnCategory(@Param("categoryList") List<ProductCategory> hiddenCategoryList,
                                       @Param("menuSection") MenuSection menuSection);

    @Query("select distinct p from Product p join p.menuSubSection m " +
            "where m in (select mss from MenuSubSection mss join mss.menuSectionList ms where :menuSection is null or :menuSection in (ms))")
    List<Product> getAllProductsByMenuSection(@Param("menuSection") MenuSection menuSection);

}
