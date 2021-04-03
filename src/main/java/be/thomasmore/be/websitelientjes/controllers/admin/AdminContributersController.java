package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.models.Reference;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.ReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminContributersController {

    @Autowired
    ReferenceRepository referenceRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;

    @ModelAttribute("contributers")
    public List<Reference> getContributers(){
        return (List<Reference>) referenceRepository.findAll();
    }
    @ModelAttribute("contributer")
    public Reference getContributer(@PathVariable(required = false) Integer contributerId){
        if(contributerId != null) {
            return referenceRepository.findById(contributerId).get();
        }
        return  null;
    }

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

    @ModelAttribute("newContributer")
    public Reference getNewContributer(){
        return new Reference();
    }

    @GetMapping("contributers")
    public String contributerPage(){
        return "admin/contributers";
    }

    @GetMapping("/contributerdetail/{contributerId}")
    public String contributerDetailPage(@PathVariable Integer contributerId){
        return "admin/contributerdetail";
    }

    @PostMapping("/updatecontributer/{contributerId}")
    public String updateContributer(@Valid @ModelAttribute("contributer") Reference reference,
                                    BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/contributerdetail";
        }

        referenceRepository.save(reference);
        return "redirect:/admin/contributers";
    }

    @PostMapping("/newcontributer")
    public String newContributer(@Valid @ModelAttribute("newContributer") Reference reference,
                                 BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/contributers";
        }

        referenceRepository.save(reference);
        return "redirect:/admin/contributers";
    }

    @PostMapping("/removecontributer/{contributerId}")
    public String removeContributer(@PathVariable Integer contributerId,
                                    @ModelAttribute("contributer") Reference reference){
        referenceRepository.delete(reference);
        return "redirect:/admin/contributers";
    }
}
