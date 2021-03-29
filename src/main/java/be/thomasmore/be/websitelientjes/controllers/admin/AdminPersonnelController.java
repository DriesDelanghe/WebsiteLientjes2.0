package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.Image;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.models.Personnel;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.ImageRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.PersonnelRepository;
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
public class AdminPersonnelController {

    @Autowired
    PersonnelRepository personnelRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;

    Logger logger = LoggerFactory.getLogger(AdminPersonnelController.class);

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

    @ModelAttribute("personnelList")
    public List<Personnel> getPersonnelList(){
        return (List<Personnel>) personnelRepository.findAll();
    }

    @ModelAttribute("personnel")
    public Personnel getPersonnel(@PathVariable(required = false) Integer id){
        if(id != null) {
            Optional<Personnel> personnelOptional = personnelRepository.findById(id);
            if (personnelOptional.isPresent()) {
                return personnelOptional.get();
            }
        }
        return null;
    }

    @ModelAttribute("personnelImages")
    public List<Image> getPersonnelImages(){
        return imageRepository.getAllPersonnelImages();
    }


    @GetMapping("/personeellijst")
    public String personeellijst(Model model){

        return "admin/personeellijst";
    }

    @GetMapping({"/personeeldetail", "/personeeldetail/{id}"})
    public String personeeldetail(){

        return "admin/personeeldetail";
    }

    @PostMapping("/personeeldetail/{id}")
    public String personeeldetailPost(Model model,
                                      @Valid @ModelAttribute("personnel") Personnel personnel,
                                      BindingResult bindingResult,
                                      @PathVariable Integer id){

        logger.info(String.format("image is null -- %s", personnel.getImage() == null));
        logger.info(String.format("personel id -- %s", personnel.getId()));

        if(bindingResult.hasErrors()){
            model.addAttribute("personnel", personnel);
            return "redirect:/admin/personeeldetail/" + id;
        }

        personnelRepository.save(personnel);
        return"redirect:/admin/personeellijst";
    }
}
