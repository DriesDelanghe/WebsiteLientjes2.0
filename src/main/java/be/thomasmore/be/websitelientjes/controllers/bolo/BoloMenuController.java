package be.thomasmore.be.websitelientjes.controllers.bolo;

import be.thomasmore.be.websitelientjes.controllers.wrapperclass.FilterWrapper;
import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.models.MenuSubSection;
import be.thomasmore.be.websitelientjes.models.Product;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.html.Option;
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

    @ModelAttribute("domain")
    public Domain getDomain(){
        return domainRepository.findById(2).get();
    }

    @ModelAttribute("menuSectionList")
    public List<MenuSection> getMenuSectionList(@ModelAttribute("domain") Domain domain){
        return menuSectionRepository.getByDomain(domain);
    }

    @ModelAttribute("pageName")
    public String getPageName(){
        return "menu";
    }

    @ModelAttribute("menuSection")
    public MenuSection getMenuSection(@PathVariable(required = false) Integer menuSectionId){
        if(menuSectionId != null){
            Optional<MenuSection> optionalMenuSection = menuSectionRepository.findById(menuSectionId);
            if(optionalMenuSection.isPresent()){
                return optionalMenuSection.get();
            }
        }
        return null;
    }

    @ModelAttribute("productList")
    public List<Product> getProductList(@ModelAttribute("menuSection") MenuSection menuSection){
        return productRepository.getAllProductsByMenuSection(menuSection);
    }

    @ModelAttribute("filterWrapper")
    public FilterWrapper getFilterWrapper(@ModelAttribute("menuSection") MenuSection menuSection){
        if(menuSection != null) {
            FilterWrapper wrapper = new FilterWrapper();
            wrapper.setAllergieList(allergieRepository.getAllAllergiesByMenuSubSections(menuSection.getMenuSubSectionList()));
            wrapper.setCategoryList(categoryRepository.getAllCategoryByMenuSubSections(menuSection.getMenuSubSectionList()));
            return wrapper;
        }
        return null;
    }

    @GetMapping("/menuoverzicht")
    public String menuOverzichtPage(){
        return "bolo/menuoverzicht";
    }

    @GetMapping("/menudetail/{menuSectionId}")
    public String menuDetailPage(){
        return "bolo/menudetail";
    }
}
