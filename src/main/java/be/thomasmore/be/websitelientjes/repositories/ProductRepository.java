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

    @Query("select p from Product p " +
            "where :productName is null " +
            "or lower(p.name) not like concat('%',lower(:productName),'%') " +
            "and p.menuSubSection in (select distinct mss from MenuSubSection mss join mss.menuSectionList ms where :menuSection is null or :menuSection in (ms)) ")
    List<Product> getAllByProductNameAndGeneralSection(@Param("productName") String productName,
                                                       @Param("menuSection") MenuSection menuSection);

    @Query("select p from Product p join p.allergies a " +
            "where (:productName is not null or (lower(p.name) like concat('%', lower(:productName), '%')) ) " +
            "or ((:allergies) is null or (:allergies) in (a)) " +
            "and p.menuSubSection in (select distinct mss from MenuSubSection mss join mss.menuSectionList ms where :menuSection is null or :menuSection in (ms))")
    List<Product> filterOnAllergieAndName(@Param("allergies") List<Allergie> allergies,
                                   @Param("productName") String productName,
                                          @Param("menuSection") MenuSection menuSection);

    @Query("select distinct p from Product p join p.categories c " +
            "where p in (:productList) and c in :categories")
    List<Product> filterOnCategory(@Param("categories") List<ProductCategory> category,
                                   @Param("productList") List<Product> productList);
    @Query("select distinct p from Product p join p.categories c " +
            "where p in (:productList) and c not in :categories")
    List<Product> filterExceptCategory(@Param("categories") List<ProductCategory> category,
                                       @Param("productList") List<Product> productList);

    @Query("select p from Product p join p.allergies a join p.categories c " +
            "where (:productName is null or lower(p.name) like concat('%',lower(:productName),'%')) " +
            "and ((:allergies) is null or (:allergies) in (a)) " +
            "and (:category is null or :category in (c)) " +
            "and p.menuSubSection in (select mss from MenuSubSection mss join mss.menuSectionList msl where :menuSection in (msl))")
    List<Product> allProductsFilteredByExcludeCategoryAndNameExcludeAllergy(@Param("allergies") List<Allergie> allergies,
                                                                            @Param("category") ProductCategory category,
                                                                            @Param("menuSection") MenuSection menuSection,
                                                                            @Param("productName") String productName);

    @Query("select distinct p from Product p join p.menuSubSection mss " +
            "where mss in (select distinct mss from MenuSubSection mss join mss.menuSectionList ms where :menuSection is null or :menuSection in (ms))")
    List<Product> getAllProductsByMenuSection(@Param("menuSection") MenuSection menuSection);

}
