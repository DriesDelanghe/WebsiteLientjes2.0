package be.thomasmore.be.websitelientjes.controllers;

import be.thomasmore.be.websitelientjes.controllers.wrapperclass.TextWrapper;
import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.Page;
import be.thomasmore.be.websitelientjes.models.TextFragment;
import be.thomasmore.be.websitelientjes.repositories.ContactInfoRepository;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.PageRepository;
import be.thomasmore.be.websitelientjes.repositories.TextFragmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class LandingController {

    @Autowired
    ContactInfoRepository contactInfoRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    TextFragmentRepository textFragmentRepository;

    @ModelAttribute("domainBistro")
    public Domain getDomainBistro(){
        return domainRepository.findById(1).get();
    }

    @ModelAttribute("domainBolo")
    public Domain getDomainBolo(){
        return domainRepository.findById(2).get();
    }

    @ModelAttribute("homepageBolo")
    public Page getHomepageBolo(){
        return pageRepository.findById(6).get();
    }

    @ModelAttribute("homepageBistro")
    public Page getHomepageBistro(){
        return pageRepository.findById(1).get();
    }

    @ModelAttribute("page")
    public Page getPage(){
        return pageRepository.findById(9).get();
    }

    @ModelAttribute("textWrapper")
    public TextWrapper getTextWrapper(@ModelAttribute("page") Page page){
        TextWrapper wrapper = new TextWrapper();
        wrapper.setHeaderText((ArrayList<TextFragment>) textFragmentRepository.getByPageAndHeaderText(page, true));
        wrapper.setParagraphText((ArrayList<TextFragment>) textFragmentRepository.getByPageAndHeaderText(page, false));

        return wrapper;
    }


    @GetMapping("/")
    public String landing(Principal principal){

        if(principal != null){
            return "redirect:/admin/home";
        }

        return "redirect:/landingspage";
    }

    @GetMapping("/landingspage")
    public String landingspage(){
        return "landing";
    }
}
