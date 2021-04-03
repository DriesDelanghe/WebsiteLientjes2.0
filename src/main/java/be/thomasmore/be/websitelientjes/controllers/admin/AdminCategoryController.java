package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.CategoryRepository;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @ModelAttribute("allProductsBistro")
    public MenuSection getAllProductsBistro() {
        return menuSectionRepository.findById(1).get();
    }

    @ModelAttribute("allProductsBolo")
    public MenuSection getAllProductsBolo() {
        return menuSectionRepository.findById(2).get();
    }

    @ModelAttribute("categoryList")
    public List<ProductCategory> getCategoryList() {
        return (List<ProductCategory>) categoryRepository.findAll();
    }

    @ModelAttribute("category")
    public ProductCategory getCategory(@PathVariable(required = false) Integer categoryId) {
        if (categoryId != null) {
            Optional<ProductCategory> categoryOptional = categoryRepository.findById(categoryId);
            if (categoryOptional.isPresent()) {
                return categoryOptional.get();
            }
        }
        return null;
    }

    @ModelAttribute("newCategory")
    public ProductCategory newCategory() {
        return new ProductCategory();
    }


    @GetMapping("/categorielijst")
    public String categorieLijstPage() {

        return "admin/categorylist";
    }

    @GetMapping("/categorie/{categoryId}")
    public String allergieItemPage() {

        return "admin/categorie";
    }


    @PostMapping("/removeproductfromcategory/{categoryId}")
    public String removeProductFromAllergie(@ModelAttribute("category") ProductCategory category,
                                            @RequestParam(name = "productId[]") List<Integer> productIds) {

        List<Product> productsToRemove = (List<Product>) productRepository.findAllById(productIds);
        for (Product p : productsToRemove) {
            p.getCategories().remove(category);
            productRepository.save(p);
        }
        category.getProducts().removeAll(productsToRemove);
        categoryRepository.save(category);

        return "redirect:/admin/categorie/" + category.getId();
    }

    @PostMapping("/addproducttocategory/{categoryId}")
    public String adProductToAllergie(@ModelAttribute("category") ProductCategory category,
                                      @RequestParam(name = "productId[]") List<Integer> productIds) {

        List<Product> productsToAdd = (List<Product>) productRepository.findAllById(productIds);
        for (Product p : productsToAdd) {
            p.getCategories().add(category);
            productRepository.save(p);
        }
        category.getProducts().addAll(productsToAdd);
        categoryRepository.save(category);

        return "redirect:/admin/categorie/" + category.getId();
    }

    @PostMapping("/updatecategory/{categoryId}")
    public String updateAllergie(@Valid @ModelAttribute("category") ProductCategory category,
                                 BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return "redirect:/admin/categorie/" + category.getId();
        }

        categoryRepository.save(category);

        return "redirect:/admin/categorielijst";
    }

    @PostMapping("/newcategory")
    public String newAllergiePost(@Valid @ModelAttribute("newCategory") ProductCategory productCategory,
                                  BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/categorylist";
        }
        categoryRepository.save(productCategory);

        return "redirect:/admin/categorielijst";
    }

    @PostMapping("/removecategory/{categoryId}")
    public String removeAllergiePost(@PathVariable Integer categoryId){
        ProductCategory category = categoryRepository.findById(categoryId).get();
        List<Product> productList = category.getProducts();
        for(Product p : productList){
            p.getCategories().remove(category);
        }
        categoryRepository.delete(category);

        return "redirect:/admin/categorielijst";
    }

}
