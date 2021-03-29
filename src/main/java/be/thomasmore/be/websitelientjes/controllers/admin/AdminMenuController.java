package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.Image;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class AdminMenuController {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;

    @ModelAttribute("domainBistro")
    public Domain getDomainBistro(){
        return domainRepository.getByDomainName("bistro");
    }
    @ModelAttribute("domainBolo")
    public Domain getDomainBolo(){
        return domainRepository.getByDomainName("bolo");
    }

    @ModelAttribute("menuSectionListBistro")
    public List<MenuSection> getMenuSectionListBistro(@ModelAttribute("domainBistro") Domain domainBistro){
        return menuSectionRepository.getByDomain(domainBistro);
    }
    @ModelAttribute("menuSectionListBolo")
    public List<MenuSection> getMenuSectionListBolo(@ModelAttribute("domainBolo") Domain domainBolo){
        return menuSectionRepository.getByDomain(domainBolo);
    }

    @GetMapping({"/menulijst", "/menulijst/{id}"})
    public String productLijst(Model model, @PathVariable(required = false) Integer id){

        model.addAttribute("menuSectionList", null);
        model.addAttribute("domain", null);
        try {
            if (id != null) {
                Optional<Domain> domainOptional = domainRepository.findById(id);
                if (domainOptional.isPresent()) {
                    Domain domain = domainOptional.get();
                    List<MenuSection> menuSectionList = menuSectionRepository.getByDomain(domain);
                    model.addAttribute("menuSectionList", menuSectionList);
                    model.addAttribute("domain", domain);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return "admin/menulijst";
        }

        return "admin/menulijst";
    }

    @GetMapping({"/menusectie", "/menusectie/{id}"})
    public String menuSectie(Model model, @PathVariable(required = false) Integer id){
        model.addAttribute("menuSection", null);
        try{
            Optional<MenuSection> menuSectionOptional = menuSectionRepository.findById(id);
            model.addAttribute("section", null);
            if(menuSectionOptional.isPresent()){
                model.addAttribute("menuSection", menuSectionOptional.get());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "admin/menusectie";
    }

    @PostMapping("/menusectie/{id}")
    public String menuSectiePost(@Valid @ModelAttribute("menuSection") MenuSection menuSection,
                                 @PathVariable Integer id,
                                 Model model,
                                 @RequestParam String name){

        menuSection.setName(name);
        menuSectionRepository.save(menuSection);
        model.addAttribute("menuSection", menuSection);
        model.addAttribute("changesSaved", true);
        return "redirect:/admin/menusectie" +id;
    }

}
