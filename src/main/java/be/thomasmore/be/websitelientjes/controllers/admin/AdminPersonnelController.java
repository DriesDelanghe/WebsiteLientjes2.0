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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
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
    MenuSectionRepository menuSectionRepository;
    @Autowired
    DomainRepository domainRepository;

    Logger logger = LoggerFactory.getLogger(AdminPersonnelController.class);

    @ModelAttribute("personnelList")
    public List<Personnel> getPersonnelList(){
        return (List<Personnel>) personnelRepository.findAll();
    }

    @GetMapping("/personeellijst")
    public String personeellijst(Model model){

        return "admin/personeellijst";
    }

    @GetMapping({"/personeeldetail", "/personeeldetail/{id}"})
    public String personeellijstdetail(@ModelAttribute("personnelList") List<Personnel> personnelList, Model model,
                                       @PathVariable(required = false) Integer id){
        Optional<Personnel> personnelOptional = personnelRepository.findById(id);
        if(personnelOptional.isPresent()){
            Personnel personnel = personnelOptional.get();
            model.addAttribute("personnel", personnel);
        logger.info(String.format("image is null -- %s", personnel.getImage() == null));
        logger.info(String.format("personel id -- %s", personnel.getId()));
        }


        return "admin/personeeldetail";
    }

    @PostMapping("/personeeldetail/{id}")
    public String personeeldetailPost(Model model,
                                      @Valid @ModelAttribute("personnel") Personnel personnel,
                                      BindingResult bindingResult,
                                      @PathVariable Integer id,
                                      @RequestParam Integer imageId){

        logger.info(String.format("image is null -- %s", personnel.getImage() == null));
        logger.info(String.format("personel id -- %s", personnel.getId()));

        if(bindingResult.hasErrors()){
            model.addAttribute("personnel", personnel);
            return "redirect:/admin/personeeldetail/" + id;
        }

        Image image = imageRepository.findById(imageId).get();

        personnel.setImage(image);

        personnelRepository.save(personnel);
        return"redirect:/admin/personeellijst";
    }

    @GetMapping("/productlijst")
    public String productLijst(Model model){
        Domain bistro = domainRepository.getByDomainName("bistro");
        Domain bolo = domainRepository.getByDomainName("bolo");
        List<MenuSection> menuSectionListBistro = menuSectionRepository.getByDomain(bistro);
        List<MenuSection> menuSectionListBolo = menuSectionRepository.getByDomain(bolo);

        model.addAttribute("menuSectionListBistro", menuSectionListBistro);
        model.addAttribute("menuSectionListBolo", menuSectionListBistro);

        return "admin/productlijst";
    }
}
