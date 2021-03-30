package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.CategoryRepository;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class AdminCategoryController {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    @ModelAttribute("domainBistro")
    public Domain getDomainBistro() {
        return domainRepository.findById(1).get();
    }

    @ModelAttribute("domainBolo")
    public Domain getDomainBolo() {
        return domainRepository.findById(2).get();
    }

    @ModelAttribute("menuSectionListBistro")
    public List<MenuSection> getMenuSectionListBistro(@ModelAttribute("domainBistro") Domain domainBistro) {
        return menuSectionRepository.getByDomain(domainBistro);
    }

    @ModelAttribute("menuSectionListBolo")
    public List<MenuSection> getMenuSectionListBolo(@ModelAttribute("domainBolo") Domain domainBolo) {
        return menuSectionRepository.getByDomain(domainBolo);
    }

    @ModelAttribute("categoryList")
    public List<ProductCategory> getCategoryList(){
        return (List<ProductCategory>) categoryRepository.findAll();
    }
    @ModelAttribute("category")
    public ProductCategory getAllergy(@PathVariable(required = false) Integer categoryId){
        if(categoryId != null){
            Optional<ProductCategory> categoryOptional = categoryRepository.findById(categoryId);
            if(categoryOptional.isPresent()){
                return categoryOptional.get();
            }
        }
        return null;
    }





    @GetMapping("/categorielijst")
    public String categorieLijstPage(){

        return "admin/categorylist";
    }

    @GetMapping("/categorie/{categoryId}")
    public String allergieItemPage(){

        return "admin/categorie";
    }



    @PostMapping("/removeproductfromcategory/{categoryId}/{productId}")
    public String removeProductFromAllergie(@ModelAttribute("category") ProductCategory category,
                                            @PathVariable Integer productId){

        Product product = productRepository.findById(productId).get();
        product.getAllergies().remove(category);
        category.getProducts().remove(product);
        categoryRepository.save(category);
        productRepository.save(product);

        return "redirect:/admin/categorie/" + category.getId();
    }

}
