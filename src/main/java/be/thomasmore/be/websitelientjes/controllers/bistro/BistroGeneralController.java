package be.thomasmore.be.websitelientjes.controllers.bistro;

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
import java.util.List;

@RequestMapping("/bistro")
@Controller
public class BistroGeneralController {

    Logger logger = LoggerFactory.getLogger(BistroGeneralController.class);

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

}
