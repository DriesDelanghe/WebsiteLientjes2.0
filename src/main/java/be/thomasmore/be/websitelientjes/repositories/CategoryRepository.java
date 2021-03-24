package be.thomasmore.be.websitelientjes.repositories;

import be.thomasmore.be.websitelientjes.models.ProductCategory;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<ProductCategory, Integer> {
}
