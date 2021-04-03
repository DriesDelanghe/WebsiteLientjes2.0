package be.thomasmore.be.websitelientjes.controllers.admin;


import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.models.SocialMedia;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.SocialMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class AdminSocialMediaController {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    SocialMediaRepository socialMediaRepository;

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

    @ModelAttribute("socialMediaBistro")
    public List<SocialMedia> getSocialMediaBistro(@ModelAttribute("domainBistro") Domain domain){
        return socialMediaRepository.findByDomain(domain);
    }

    @ModelAttribute("socialMediaBolo")
    public List<SocialMedia> getSocialMediaBolo(@ModelAttribute("domainBolo") Domain domain){
        return socialMediaRepository.findByDomain(domain);
    }

    @ModelAttribute("domain")
    public Domain getDomainById(@PathVariable(required = false) Integer domainId){
        if(domainId != null){
            Optional<Domain> domainOptional = domainRepository.findById(domainId);
            if(domainOptional.isPresent()){
                return domainOptional.get();
            }
        }
        return null;
    }

    @GetMapping({"/socialmedialijst", "/socialmedialijst/{domainId}"})
    public String getSocialMediaLijst(Model model){
        return "admin/socialmedialist";
    }

}
