package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.Image;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.ImageRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    ImageRepository imageRepository;

    Logger logger = LoggerFactory.getLogger(AdminMenuController.class);

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

    @ModelAttribute("menuSection")
    public MenuSection getMenuSection(@PathVariable(required = false) Integer menuSectionId){
        try{
            Optional<MenuSection> menuSectionOptional = menuSectionRepository.findById(menuSectionId);
            if(menuSectionOptional.isPresent()){
                return menuSectionOptional.get();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @ModelAttribute("menuImages")
    public List<Image> getMenuImages(){
        return imageRepository.getAllMenuImages();
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

    @GetMapping({"/menusectie", "/menusectie/{menuSectionId}"})
    public String menuSectie(Model model, @PathVariable(required = false) Integer menuSectionId){

        return "admin/menusectie";
    }

    @PostMapping("/menusectie/{menuSectionId}")
    public String menuSectiePost(@PathVariable Integer menuSectionId,
                                 Model model,
                                 @Valid @ModelAttribute("menuSection") MenuSection menuSection,
                                 BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "redirect:/admin/menusectie";
        }

        logger.info("domain id -- " + menuSection.getId());
        menuSectionRepository.save(menuSection);
        model.addAttribute("changesSaved", true);
        return "admin/menusectie";
    }

    @PostMapping("/menu/imagechange")
    public String changeImageMenu(@ModelAttribute("menuSection") MenuSection menuSection,
                                  @RequestParam Integer imageId){

        if(imageId != null && menuSection.getImage().getId() != imageId) {
            menuSection.setImage(new Image(imageId));
        }

        menuSectionRepository.save(menuSection);

        return "redirect:admin/menusectie/" + menuSection.getId();
    }

}
