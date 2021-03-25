package be.thomasmore.be.websitelientjes.controllers;


import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/bistro")
@Controller
public class MenuDetailsController {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    AllergieRepository allergieRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MenuSubSectionRepository menuSubSectionRepository;
    @Autowired
    SymbolRepository symbolRepository;

    Logger logger = LoggerFactory.getLogger(MenuDetailsController.class);


    @ModelAttribute("rightArrow")
    public Symbol getArrow() {
        Symbol rightArrow = symbolRepository.getSymbolByReferenceName("rightArrow");

        return rightArrow;
    }

    @ModelAttribute("domain")
    public Domain getDomain() {
        Domain domain = domainRepository.getByDomainName("bistro");

        return domain;
    }

    @ModelAttribute("menuSection")
    public MenuSection getMenuSection(@PathVariable(required = false) Integer id) {
        Optional<MenuSection> menuSectionOptional = menuSectionRepository.findById(id);
        if (menuSectionOptional.isPresent()) {
            MenuSection menuSection = menuSectionOptional.get();

            return menuSection;
        }
        return null;
    }


    @ModelAttribute("menuSectionList")
    public List<MenuSection> getMenuSectionList(Model model,
                                                @ModelAttribute("domain") Domain domain,
                                                @ModelAttribute("menuSection") MenuSection menuSection) {

        List<MenuSection> menuSectionList = menuSectionRepository.getByDomain(domain);

        int indexMenuSection = menuSectionList.indexOf(menuSection);
        int prevId = (indexMenuSection != 0) ?
                menuSectionList.get(indexMenuSection - 1).getId() :
                menuSectionList.get(menuSectionList.size() - 1).getId();
        int nextId = (indexMenuSection != menuSectionList.size() - 1) ?
                menuSectionList.get(indexMenuSection + 1).getId() :
                menuSectionList.get(0).getId();
        model.addAttribute("prevId", prevId);
        model.addAttribute("nextId", nextId);

        menuSectionList.remove(menuSection);

        return menuSectionList;
    }

    @ModelAttribute("menuSubSectionList")
    public List<MenuSubSection> getMenuSubSectionList(@ModelAttribute("domain") Domain domain) {
        List<MenuSubSection> menuSubSectionList = menuSubSectionRepository.getByDomain(domain);

        return menuSubSectionList;
    }

    @ModelAttribute("categoryList")
    public List<ProductCategory> getCategoryList(@ModelAttribute("menuSection") MenuSection menuSection) {
        List<ProductCategory> categoryList = categoryRepository.getAllCategoryByMenuSubSections(menuSection.getMenuSubSectionList());

        return categoryList;
    }

    @ModelAttribute("allergieList")
    public List<Allergie> getAllergieList(@ModelAttribute("menuSection") MenuSection menuSection) {
        List<Allergie> allergieList = allergieRepository.getAllAllergiesByMenuSubSections(menuSection.getMenuSubSectionList());

        return allergieList;
    }

    @ModelAttribute("productList")
    public List<Product> getProductList(@ModelAttribute("menuSection") MenuSection menuSection) {
        List<Product> productList = productRepository.getAllProductsByMenuSection(menuSection);

        return productList;
    }


    @GetMapping({"/menudetails", "/menudetails/{id}"})
    public String menudetails(Model model, @ModelAttribute("categoryList") List<ProductCategory> categoryList) {

        model.addAttribute("productSearch", null);
        model.addAttribute("allergieFilters", null);
        model.addAttribute("productListFiltered", null);
        model.addAttribute("filteredCategoryList", categoryList);

        return "bistro/menudetails";
    }


    @PostMapping({"/menudetails", "/menudetails/{id}"})
    public String menudetailspost(Model model,
                                  @RequestParam(required = false) String productSearch,
                                  @RequestParam(required = false, name = "allergieFilter[]") List<Integer> allergieFilters,
                                  @RequestParam(required = false, name = "categoryFilter[]") List<Integer> categoryFilter,
                                  @ModelAttribute("categoryList") List<ProductCategory> categoryList,
                                  @ModelAttribute("menuSection") MenuSection menuSection) {

        model.addAttribute("productSearch", productSearch);
        model.addAttribute("allergieFilters", allergieFilters);

        List<ProductCategory> filteredCategoryList = null;

        if (categoryFilter != null) {
            for (Integer d : categoryFilter) {
                logger.info(String.format("show catergoryId -- %d", d));
            }
            filteredCategoryList = (List<ProductCategory>) categoryRepository.findAllById(categoryFilter);
        } else {
            logger.info("No shown category");
        }

        List<ProductCategory> missingCategories = categoryList;
        if (filteredCategoryList != null) {
            missingCategories.removeAll(filteredCategoryList);
        }

        if (!missingCategories.isEmpty()) {
            missingCategories.forEach(category -> logger.info(String.format("Hiding category -- %s", category.getName())));
        }


        if (productSearch == null || productSearch.isBlank()) {
            logger.info("productsearch was blank");
            productSearch = null;
        } else {
            logger.info("productsearch was not blank");
        }

        logger.info(String.format("value productsearch -- '%s'", productSearch));
        logger.info(String.format("allergieFilters is null -- %s", allergieFilters == null));

        List<Allergie> filteredAllergies = null;
        if (allergieFilters != null) {
            filteredAllergies = (List<Allergie>) allergieRepository.findAllById(allergieFilters);
            filteredAllergies.forEach(allergie -> logger.info(String.format("Filtering on allergie -- %s", allergie.getName())));
        }

        logger.info(String.format("allergies is null -- %s", filteredAllergies == null));

        logger.info(String.format("searching on productName -- '%s'", productSearch));
        logger.info(String.format("searching in menuSection -- %s", menuSection.getName()));

        if(allergieFilters !=null) {
            List<Product> productListFiltered = productRepository.filterOnAllergieAndName(filteredAllergies, productSearch, menuSection);
            List<Product> productListCategoryFilter = productRepository.filterListOnCategory(productListFiltered, missingCategories, menuSection);

            productListFiltered.addAll(productListCategoryFilter);
        productListFiltered.forEach(product -> logger.info(String.format("found product matching filter -- %s", product.getName())));
        logger.info(String.format("number of filtered products -- %d", productListFiltered.size()));

            model.addAttribute("productListFiltered", productListFiltered);
        }else{
            List<Product> productListFiltered = productRepository.filterOnlyOnCategory(missingCategories, menuSection);
            model.addAttribute("productListFiltered", productListFiltered);
        }

        model.addAttribute("filteredCategoryList", filteredCategoryList);

        return "bistro/menudetails";
    }
}
