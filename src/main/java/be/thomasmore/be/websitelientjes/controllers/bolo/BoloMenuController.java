package be.thomasmore.be.websitelientjes.controllers.bolo;

import be.thomasmore.be.websitelientjes.controllers.wrapperclass.FilterWrapper;
import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/bolo")
@Controller
public class BoloMenuController {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    MenuSubSectionRepository menuSubSectionRepository;
    @Autowired
    AllergieRepository allergieRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    Logger logger = LoggerFactory.getLogger(BoloMenuController.class);

    @ModelAttribute("domain")
    public Domain getDomain() {
        return domainRepository.findById(2).get();
    }

    @ModelAttribute("menuSectionList")
    public List<MenuSection> getMenuSectionList(@ModelAttribute("domain") Domain domain) {
        return menuSectionRepository.getByDomain(domain);
    }

    @ModelAttribute("pageName")
    public String getPageName() {
        return "menu";
    }

    @ModelAttribute("menuSection")
    public MenuSection getMenuSection(@PathVariable(required = false) Integer menuSectionId) {
        if (menuSectionId != null) {
            Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(menuSectionId);
            if (optionalMenuSection.isPresent()) {
                return optionalMenuSection.get();
            }
        }
        return null;
    }

    @ModelAttribute("productList")
    public List<Product> getProductList(@ModelAttribute("menuSection") MenuSection menuSection) {
        return productRepository.getAllProductsByMenuSection(menuSection);
    }

    @ModelAttribute("filterWrapper")
    public FilterWrapper getFilterWrapper(@ModelAttribute("menuSection") MenuSection menuSection) {
        if (menuSection != null) {
            FilterWrapper wrapper = new FilterWrapper();
            menuSection.getMenuSubSectionList().forEach(menuSubSection -> logger.info("subsection name: " + menuSubSection.getName()));
            wrapper.setAllergieList(allergieRepository.getAllAllergiesByMenuSubSections(menuSection.getMenuSubSectionList()));
            wrapper.getAllergieList().forEach(allergie -> logger.info("allergie name: " + allergie.getName()));
            wrapper.setCategoryList(categoryRepository.getAllCategoryByMenuSubSections(menuSection.getMenuSubSectionList()));
            return wrapper;
        }
        return null;
    }

    @GetMapping("/menuoverzicht")
    public String menuOverzichtPage() {
        return "bolo/menuoverzicht";
    }

    @GetMapping("/menudetail/{menuSectionId}")
    public String menuDetailPage() {
        return "bolo/menudetail";
    }

    @PostMapping("/menudetail/{menuSectionId}")
    public String menuDetailFiltered(@ModelAttribute("filterWrapper") FilterWrapper wrapper,
                                     @ModelAttribute("menuSection") MenuSection menuSection,
                                     @RequestParam(required = false, name = "category[]") ArrayList<Integer> categoryIdList,
                                     @RequestParam(required = false, name = "allergy[]") ArrayList<Integer> allergyIdList,
                                     Model model) {

        wrapper.setAllergyIdList(allergyIdList);
        wrapper.setCategoryIdList(categoryIdList);

        if(wrapper.getProductSearch().isBlank()){
            wrapper.setProductSearch(null);
        }

        ArrayList<Allergie> allergieArrayList = new ArrayList<>();
        if (wrapper.getAllergyIdList() != null && !wrapper.getAllergyIdList().isEmpty()) {
            wrapper.getAllergyIdList().forEach(integer -> allergieArrayList.add(allergieRepository.findById(integer).get()));
        }

        ArrayList<ProductCategory> productCategoryArrayList = new ArrayList<>();
        if (wrapper.getCategoryIdList() != null && !wrapper.getCategoryIdList().isEmpty()) {
            wrapper.getCategoryIdList().forEach(integer -> productCategoryArrayList.add(categoryRepository.findById(integer).get()));
        }
        ArrayList<ProductCategory> hiddenCategories = new ArrayList<>();
        for (ProductCategory productCategory : wrapper.getCategoryList()) {
            if (!productCategoryArrayList.contains(productCategory)) {
                hiddenCategories.add(productCategory);
            }
        }

        logger.info("allergie list is empty : " + allergieArrayList.isEmpty());
        if(!allergieArrayList.isEmpty()){
            allergieArrayList.forEach(allergie -> logger.info("allergie id: " + allergie.getId()));
        }

        ArrayList<Product> filteredProductsList;
        ArrayList<Product> filterListOnCategory = new ArrayList<>();

        if (!allergieArrayList.isEmpty()) {
            filteredProductsList = new ArrayList<>(productRepository.filterOnAllergieAndName(allergieArrayList, wrapper.getProductSearch(), menuSection));
        } else {
            filteredProductsList = new ArrayList<>(productRepository.filterOnAllergieAndName(null, wrapper.getProductSearch(), menuSection));
        }
        if (!hiddenCategories.isEmpty()) {
            if (!filteredProductsList.isEmpty()) {
                filterListOnCategory.addAll(productRepository.filterListOnCategory(filteredProductsList, hiddenCategories, menuSection));
                filteredProductsList.addAll(filterListOnCategory);
            } else {
                filterListOnCategory.addAll(productRepository.filterOnlyOnCategory(hiddenCategories, menuSection));
            }
        }

        if (!filteredProductsList.isEmpty()) {
            model.addAttribute("filteredProductList", filteredProductsList);
            logger.info("amount of filtered products: " + filteredProductsList.size());
        }else{
            model.addAttribute("filteredProductList", filterListOnCategory);
        }

        return "bolo/menudetail";
    }
}
