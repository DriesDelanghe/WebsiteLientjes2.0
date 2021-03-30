package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.Image;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.models.Personnel;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.ImageRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.PersonnelRepository;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;

    @Value("${upload.images.dir}")
    private String uploadImagesDirString;

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
    public List<Personnel> getPersonnelList(@PathVariable(required = false) Integer domainId){
        if(domainId != null){
            return personnelRepository.getByDomain(domainRepository.findById(domainId).get());
        }

        return (List<Personnel>) personnelRepository.findAll();
    }

    @ModelAttribute("personnel")
    public Personnel getPersonnel(@PathVariable(required = false) Integer personnelId){
        if(personnelId != null) {
            Optional<Personnel> personnelOptional = personnelRepository.findById(personnelId);
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

    @ModelAttribute("newPersonnel")
    public Personnel newPersonnel(){
        Personnel personnel = new Personnel();
        personnel.setImage(imageRepository.findById(1).get());
        return personnel;
    }


    @GetMapping({"/personeellijst", "/personeellijst/{domainId}"})
    public String personeellijst(Model model, @PathVariable(required = false) Integer domainId){

        return "admin/personeellijst";
    }

    @GetMapping({"/personeeldetail", "/personeeldetail/{personnelId}"})
    public String personeeldetail(){

        return "admin/personeeldetail";
    }

    @PostMapping("/personeeldetail/{personnelId}")
    public String personeeldetailPost(Model model,
                                      @Valid @ModelAttribute("personnel") Personnel personnel,
                                      BindingResult bindingResult,
                                      @PathVariable Integer personnelId,
                                      @RequestParam Integer domainId){

        logger.info(String.format("image is null -- %s", personnel.getImage() == null));
        logger.info(String.format("personel id -- %s", personnel.getId()));

        if(bindingResult.hasErrors()){
            model.addAttribute("personnel", personnel);
            return "redirect:/admin/personeeldetail/" + personnelId;
        }
        if(domainId != personnel.getDomain().getId()){
            personnel.setDomain(new Domain(domainId));
        }

        personnelRepository.save(personnel);
        return"redirect:/admin/personeellijst";
    }

    @PostMapping("/personnel/imagechange")
    public String changeImage(@ModelAttribute("personnel") Personnel personnel,
                              @RequestParam Integer imageId){
        Image image = new Image(imageId);
        personnel.setImage(image);

        personnelRepository.save(personnel);

        return "redirect:/admin/personeeldetail" + personnel.getId();
    }

    @PostMapping("/newpersonnel")
    public String createNewPersonnel(@Valid @ModelAttribute("newPersonnel") Personnel personnel,
                                     @RequestParam Integer domainId){

        personnel.setDomain(new Domain(domainId));
        personnelRepository.save(personnel);

        return "redirect:/admin/personeellijst";
    }

    @PostMapping("/personnel/imagechange/{personnelId}")
    public String changeImagePersonnel(@ModelAttribute("personnel") Personnel personnel,
                                  @RequestParam Integer imageId,
                                  @PathVariable Integer personnelId,
                                  Model model) {

        if (imageId != null && personnel.getImage().getId() != imageId) {
            personnel.setImage(new Image(imageId));
        }

        personnelRepository.save(personnel);
        model.addAttribute("changesSaved", true);
        return "redirect:/admin/personeeldetail/" + personnelId;
    }

    @PostMapping("/personnel/newimage/{personnelId}")
    public String newImagePersonnel(@ModelAttribute("Personnel") Personnel personnel,
                               @PathVariable Integer personnelId,
                               @NotNull @RequestParam MultipartFile newImage,
                               Model model) throws Exception {

        try {
            String newImageName = newImage.getOriginalFilename();
            String path = uploadImagesDirString + "/images/personnel";

            if (!newImageName.isEmpty()) {
                File imageFileDir = new File(path);
                if (!imageFileDir.exists()) imageFileDir.mkdirs();
                File imageFile = new File(path, newImageName);
                logger.info("filesize: " + newImage.getBytes().toString());
                newImage.transferTo(imageFile);
                logger.info(imageFile.getPath());
                Optional<Image> imageOptional = imageRepository.getByImageLocation(imageFile.getPath());
                if (!imageOptional.isPresent()) {
                    Image image = new Image("/images/personnel/" + newImageName);
                    personnel.setImage(image);
                    imageRepository.save(image);
                } else {
                    personnel.setImage(imageOptional.get());
                }
            }
        } catch (FileSizeLimitExceededException fileSizeLimitExceededException) {
            model.addAttribute("FileSizeException", true);
            return "redirect:/admin/personeeldetail/" + personnelId;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/personeeldetail/" + personnelId;
        }

        personnelRepository.save(personnel);
        return "redirect:/admin/personeeldetail/" + personnelId;
    }
}
