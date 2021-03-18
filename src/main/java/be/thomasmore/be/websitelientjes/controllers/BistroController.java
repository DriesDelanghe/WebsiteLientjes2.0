package be.thomasmore.be.websitelientjes.controllers;

import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class BistroController {

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



    @GetMapping({"/bistro", "/bistro/" ,"/bistro/home"})
    public String home(Model model){
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

        return "bistro/home";
    }

    @GetMapping("/bistro/references")
    public String reference(Model model){

        Domain domain = domainRepository.getByDomainName("bistro");
        Page page = pageRepository.getByDomainAndPageName(domain, "references");
        List<Reference> referenceList = (List<Reference>) referenceRepository.findAll();

        model.addAttribute("page", page);
        model.addAttribute("referenceList", referenceList);

        return "bistro/references";
    }

    @GetMapping("/bistro/contact")
    public String contact(Model model){
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

        return "bistro/contact";
    }

    @GetMapping("/bistro/personeel")
    public String personeel(Model model){

        Domain domain = domainRepository.getByDomainName("bistro");
        Page page = pageRepository.getByDomainAndPageName(domain, "personeel");
        List<Personnel> personnelList = personnelRepository.getByPage(page);


        model.addAttribute("personnelList", personnelList);
        model.addAttribute("page", page);
        return "bistro/personeel";
    }

}
