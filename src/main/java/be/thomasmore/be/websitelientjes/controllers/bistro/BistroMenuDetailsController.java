package be.thomasmore.be.websitelientjes.controllers.bistro;


import be.thomasmore.be.websitelientjes.controllers.wrapperclass.FilterWrapper;
import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/bistro")
@Controller
public class BistroMenuDetailsController {

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
    PageRepository pageRepository;

    Logger logger = LoggerFactory.getLogger(BistroMenuDetailsController.class);

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

    @ModelAttribute("domain")
    public Domain getDomain() {
        Domain domain = domainRepository.findById(1).get();

        return domain;
    }

    @ModelAttribute("menuSection")
    public MenuSection getMenuSection(@PathVariable(required = false) Integer menuSectionId) {
        Optional<MenuSection> menuSectionOptional = menuSectionRepository.findById(menuSectionId);
        if (menuSectionOptional.isPresent()) {
            MenuSection menuSection = menuSectionOptional.get();

            return menuSection;
        }
        return null;
    }


    @ModelAttribute("page")
    public Page getPage() {
        return pageRepository.findById(5).get();
    }

    @ModelAttribute("menuSectionList")
    public List<MenuSection> getMenuSectionList(Model model,
                                                @ModelAttribute("domain") Domain domain,
                                                @ModelAttribute("menuSection") MenuSection menuSection) {


        List<MenuSection> menuSectionList = menuSectionRepository.getByDomain(domain);
        if (menuSection != null) {
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
        }
        return menuSectionList;
    }

    @ModelAttribute("menuSubSectionList")
    public List<MenuSubSection> getMenuSubSectionList(@ModelAttribute("domain") Domain domain) {
        List<MenuSubSection> menuSubSectionList = menuSubSectionRepository.getByDomain(domain);

        return menuSubSectionList;
    }

    @ModelAttribute("categoryList")
    public List<ProductCategory> getCategoryList(@ModelAttribute("menuSection") MenuSection menuSection) {
        if (menuSection != null) {
            List<ProductCategory> categoryList = categoryRepository.getAllCategoryByMenuSubSections(menuSection.getMenuSubSectionList());

            return categoryList;
        }
        return null;
    }

    @ModelAttribute("allergieList")
    public List<Allergie> getAllergieList(@ModelAttribute("menuSection") MenuSection menuSection) {
        if (menuSection != null) {
            List<Allergie> allergieList = allergieRepository.getAllAllergiesByMenuSubSections(menuSection.getMenuSubSectionList());

            return allergieList;
        }
        return null;
    }

    @ModelAttribute("productList")
    public List<Product> getProductList(@ModelAttribute("menuSection") MenuSection menuSection) {
        List<Product> productList = productRepository.getAllProductsByMenuSection(menuSection);

        return productList;
    }


    @GetMapping({"/menudetails", "/menudetails/{menuSectionId}"})
    public String menudetails(Model model, @ModelAttribute("categoryList") List<ProductCategory> categoryList,
                              @PathVariable(required = false) Integer menuSectionId) {

        model.addAttribute("productSearch", null);
        model.addAttribute("allergieFilters", null);
        model.addAttribute("productListFiltered", null);
        model.addAttribute("filteredCategoryList", categoryList);

        return "bistro/menudetails";
    }


    @PostMapping({"/menudetails", "/menudetails/{menuSectionId}"})
    public String menudetailspost(@ModelAttribute("filterWrapper") FilterWrapper wrapper,
                                  @ModelAttribute("menuSection") MenuSection menuSection,
                                  @RequestParam(required = false, name = "category[]") ArrayList<Integer> categoryIdList,
                                  @RequestParam(required = false, name = "allergy[]") ArrayList<Integer> allergyIdList,
                                  Model model) {

        wrapper.setAllergyIdList(allergyIdList);
        wrapper.setCategoryIdList(categoryIdList);

        List<Allergie> allergieList = null;
        if(wrapper.getAllergyIdList() != null) {
            allergieList = (List<Allergie>) allergieRepository.findAllById(wrapper.getAllergyIdList());
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

        logger.info(String.valueOf("allergie list is empty : " + allergieList != null));
        if (allergieList != null) {
            allergieList.forEach(allergie -> logger.info("allergie id: " + allergie.getId()));
        }

        ArrayList<Product> filteredProductsList = new ArrayList<>(productRepository.filterOnAllergieAndName(allergieList, menuSection));
        ArrayList<Product> filterListOnCategory = new ArrayList<>(productRepository.filterListOnCategory(filteredProductsList, hiddenCategories, menuSection));
        filteredProductsList = new ArrayList<>(productRepository.filterOnName(wrapper.getProductSearch(), filterListOnCategory));

        model.addAttribute("filteredProductList", filteredProductsList);
        logger.info("amount of filtered products: " + filterListOnCategory.size());


        return "bistro/menudetails";
    }
}
