package be.thomasmore.be.websitelientjes.controllers;

import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequestMapping("/bistro")
@Controller
public class BistroController {

    Logger logger = LoggerFactory.getLogger(BistroController.class);

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    TextFragmentRepository textFragmentRepository;
    @Autowired
    OpeningsuurRepository openingsuurRepository;
    @Autowired
    PersonnelRepository personnelRepository;
    @Autowired
    SymbolRepository symbolRepository;
    @Autowired
    SocialMediaRepository socialMediaRepository;
    @Autowired
    ReferenceRepository referenceRepository;
    @Autowired
    ContactInfoRepository contactInfoRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    MenuSubSectionRepository menuSubSectionRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    AllergieRepository allergieRepository;
    @Autowired
    ProductRepository productRepository;


    @RequestMapping(value = {"", "/", "/home"}, method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest request) {
        Domain domain = domainRepository.getByDomainName("bistro");
        Page page = pageRepository.getByDomainAndPageName(domain, "home");

        List<TextFragment> headerText = textFragmentRepository.getByPageAndHeaderText(page, true);
        List<TextFragment> paragraphText = textFragmentRepository.getByPageAndHeaderText(page, false);
        List<Openingsuur> openingsuren = openingsuurRepository.getByDomain(domain);
        List<Personnel> personnelList = personnelRepository.getByPage(page);
        List<SocialMedia> socialMediaList = socialMediaRepository.findByDomain(domain);
        Symbol rightArrow = symbolRepository.getSymbolByReferenceName("rightArrow");

        Collections.sort(paragraphText);
        Collections.sort(headerText);
        Collections.sort(socialMediaList);

        model.addAttribute("headerText", headerText);
        model.addAttribute("paragraphText", paragraphText);
        model.addAttribute("openingsuren", openingsuren);
        model.addAttribute("personnelList", personnelList);
        model.addAttribute("socialMediaList", socialMediaList);
        model.addAttribute("rightArrow", rightArrow);
        model.addAttribute("page", page);

        logger.info(String.format("Returned homepage to client -- %s", request.getRemoteAddr()));

        return "bistro/home";

    }

    @GetMapping("/references")
    public String reference(Model model, HttpServletRequest request) {

        Domain domain = domainRepository.getByDomainName("bistro");
        Page page = pageRepository.getByDomainAndPageName(domain, "references");
        List<Reference> referenceList = (List<Reference>) referenceRepository.findAll();

        model.addAttribute("page", page);
        model.addAttribute("referenceList", referenceList);

        logger.info(String.format("Returned references page to client -- %s", request.getRemoteAddr()));

        return "bistro/references";
    }

    @GetMapping("/contact")
    public String contact(Model model, HttpServletRequest request) {
        Domain domain = domainRepository.getByDomainName("bistro");
        Page page = pageRepository.getByDomainAndPageName(domain, "contact");
        List<ContactInfo> contactInfoList = contactInfoRepository.getByDomain(domain);
        List<TextFragment> headerText = textFragmentRepository.getByPageAndHeaderText(page, true);
        List<SocialMedia> socialMediaList = socialMediaRepository.findByDomain(domain);

        Collections.sort(headerText);
        Collections.sort(socialMediaList);

        model.addAttribute("headerText", headerText);
        model.addAttribute("socialMediaList", socialMediaList);
        model.addAttribute("contactInfoList", contactInfoList);
        model.addAttribute("page", page);

        logger.info(String.format("Returned contact page to client -- %s", request.getRemoteAddr()));

        return "bistro/contact";
    }

    @GetMapping("/personeel")
    public String personeel(Model model, HttpServletRequest request) {

        Domain domain = domainRepository.getByDomainName("bistro");
        Page page = pageRepository.getByDomainAndPageName(domain, "personeel");
        List<Personnel> personnelList = personnelRepository.getByPage(page);


        model.addAttribute("personnelList", personnelList);
        model.addAttribute("page", page);

        logger.info(String.format("Returned personnel page to client -- %s", request.getRemoteAddr()));

        return "bistro/personeel";
    }

    @GetMapping("/menu")
    public String menu(Model model, HttpServletRequest request) {

        Domain domain = domainRepository.getByDomainName("bistro");
        Page page = pageRepository.getByDomainAndPageName(domain, "menu");

        List<MenuSection> menuSectionList = menuSectionRepository.getByDomain(domain);
        Symbol rightArrow = symbolRepository.getSymbolByReferenceName("rightArrow");

        model.addAttribute("rightArrow", rightArrow);
        model.addAttribute("menuSectionList", menuSectionList);
        model.addAttribute("page", page);

        logger.info(String.format("Returned menu page to client -- %s", request.getRemoteAddr()));


        return "bistro/menu";
    }

    @GetMapping({"/menudetails", "/menudetails/{id}"})
    public String menudetails(Model model, @PathVariable(required = false) Integer id,
                              @RequestParam(required = false) String productSearch,
                              @RequestParam(required = false, name = "allergieFilter[]") List<Integer> allergieFilters,
                              @RequestParam(required = false, value = "categoryFilter") HashMap<String, String> categoryFilter) {

        model.addAttribute("productSearch", productSearch);
        model.addAttribute("allergieFilters", allergieFilters);

        if(categoryFilter != null){
            for (String s : categoryFilter.values()){
                logger.info(String.format("filter catergory -- %s", s));
            }
        }else{
            logger.info("No filter for categories");
        }



        if (productSearch == null || productSearch.isBlank()) {
            logger.info("productsearch was blank");
            productSearch = null;
        } else {
            logger.info("productsearch was not blank");
        }

        logger.info(String.format("value productsearch -- '%s'", productSearch));
        logger.info(String.format("allergieFilters is null -- %s", allergieFilters == null));

        Domain domain = domainRepository.getByDomainName("bistro");

        Optional<MenuSection> menuSectionOptional = menuSectionRepository.findById(id);
        if (menuSectionOptional.isPresent()) {
            MenuSection menuSection = menuSectionOptional.get();
            model.addAttribute("menuSection", menuSection);

            List<MenuSection> menuSectionList = menuSectionRepository.getByDomain(domain);

            int indexMenuSection = menuSectionList.indexOf(menuSection);
            logger.info("found index");
            int nextId = (indexMenuSection != menuSectionList.size() - 1) ?
                    menuSectionList.get(indexMenuSection + 1).getId() :
                    menuSectionList.get(0).getId();
            int prevId = (indexMenuSection != 0) ?
                    menuSectionList.get(indexMenuSection - 1).getId() :
                    menuSectionList.get(menuSectionList.size() - 1).getId();

            List<MenuSubSection> menuSubSectionList = menuSubSectionRepository.getByDomain(domain);
            menuSectionList.remove(menuSection);

            Symbol rightArrow = symbolRepository.getSymbolByReferenceName("rightArrow");

            List<ProductCategory> categoryList = categoryRepository.getAllCategoryByMenuSubSections(menuSection.getMenuSubSectionList());
            List<Allergie> allergieList = allergieRepository.getAllAllergiesByMenuSubSections(menuSection.getMenuSubSectionList());
            model.addAttribute("categoryList", categoryList);
            model.addAttribute("allergieList", allergieList);

            List<Allergie> allergies = null;
            if(allergieFilters != null){
                allergies = (List<Allergie>) allergieRepository.findAllById(allergieFilters);
                allergies.forEach(allergie -> logger.info(String.format("searching on allergie -- %s", allergie.getName())));
            }

            logger.info(String.format("allergies is null -- %s", allergies == null));

            List<ProductCategory> categoryFilterShowList = null;
            List<ProductCategory> categoryFilterHideList = null;
            if(categoryFilter != null) {
                for (String s : categoryFilter.values()) {
                    if (s.contains("hide")) {
                        s = s.replace("hide", "");
                        categoryFilterHideList.add(categoryRepository.findById(Integer.parseInt(s)).get());
                    }
                    if (s.contains("show")) {
                        s = s.replace("show", "");
                        categoryFilterShowList.add(categoryRepository.findById(Integer.parseInt(s)).get());
                    }
                }
            }

            if ((categoryFilterHideList != null)) {
                categoryFilterHideList.forEach(category -> logger.info(String.format("hidden category -- '%s'", category.getName())));
            } else {
                logger.info("no categories hidden");
            }
            if ((categoryFilterShowList != null)) {
                categoryFilterShowList.forEach(category -> logger.info(String.format("shown category -- '%s'", category.getName())));
            } else {
                logger.info("no categories shown solo");
            }

            List<Product> productList = productRepository.getAllProductsByMenuSection(menuSection);

            List<Product> productListFiltered = productRepository.filterOnAllergieAndName(allergies, productSearch, menuSection);
            productListFiltered = productRepository.filterOnCategory(categoryList, productListFiltered);

            productListFiltered.forEach(product -> logger.info(String.format("found product matching filter -- %s", product.getName())));
            logger.info(String.format("number of filtered products -- %d", productListFiltered.size()));
            model.addAttribute("productList", productList);
            model.addAttribute("productListFiltered", productListFiltered);


            model.addAttribute("rightArrow", rightArrow);
            model.addAttribute("nextId", nextId);
            model.addAttribute("prevId", prevId);
            model.addAttribute("menuSectionList", menuSectionList);
            model.addAttribute("menuSubSectionList", menuSubSectionList);
        }


        return "bistro/menudetails";
    }

}
