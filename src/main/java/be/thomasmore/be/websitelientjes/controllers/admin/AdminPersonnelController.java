package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
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
import java.util.ArrayList;
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
    @Autowired
    PageRepository pageRepository;

    @Value("${upload.images.dir}")
    private String uploadImagesDirString;

    Logger logger = LoggerFactory.getLogger(AdminPersonnelController.class);

    @ModelAttribute("domainBistro")
    public Domain getDomainBistro() {
        return domainRepository.findById(1).get();
    }

    @ModelAttribute("domainBolo")
    public Domain getDomainBolo() {
        return domainRepository.findById(2).get();
    }

    @ModelAttribute("domain")
    public Domain getReferencedDomain(@PathVariable(required = false) Integer domainId) {
        if (domainId != null) {
            Optional<Domain> domainOptional = domainRepository.findById(domainId);

            if (domainOptional.isPresent()) {
                return domainOptional.get();
            }
        }
        return null;
    }

    @ModelAttribute("menuSectionListBistro")
    public List<MenuSection> getMenuSectionListBistro(@ModelAttribute("domainBistro") Domain domainBistro) {

        return menuSectionRepository.getByDomain(domainBistro);
    }

    @ModelAttribute("menuSectionListBolo")
    public List<MenuSection> getMenuSectionListBolo(@ModelAttribute("domainBolo") Domain domainBolo) {

        return menuSectionRepository.getByDomain(domainBolo);
    }

    @ModelAttribute("personnelList")
    public List<Personnel> getPersonnelList(@ModelAttribute("domain") Domain domain,
                                            @PathVariable(required = false) Integer domainId) {
        if (domainId != null) {
            if (domain != null) {
                return personnelRepository.getByDomain(domain);
            }
            return null;
        }
        return (List<Personnel>) personnelRepository.findAll();
    }

    @ModelAttribute("personnel")
    public Personnel getPersonnel(@PathVariable(required = false) Integer personnelId) {
        if (personnelId != null) {
            Optional<Personnel> personnelOptional = personnelRepository.findById(personnelId);
            if (personnelOptional.isPresent()) {
                return personnelOptional.get();
            }
        }
        return null;
    }

    @ModelAttribute("personnelImages")
    public List<Image> getPersonnelImages() {
        return imageRepository.getAllPersonnelImages();
    }

    @ModelAttribute("newPersonnel")
    public Personnel newPersonnel() {
        Personnel personnel = new Personnel();
        personnel.setImage(imageRepository.findById(1).get());
        return personnel;
    }

    @ModelAttribute("homepageBistro")
    public Page getHomepageBistro() {
        return pageRepository.findById(1).get();
    }

    @ModelAttribute("homepageBolo")
    public Page getHomepageBolo() {
        return pageRepository.findById(6).get();
    }

    @GetMapping({"/personeellijst", "/personeellijst/{domainId}"})
    public String personeellijst(Model model, @PathVariable(required = false) Integer domainId) {

        return "admin/personeellijst";
    }

    @GetMapping({"/personeeldetail", "/personeeldetail/{personnelId}"})
    public String personeeldetail() {

        return "admin/personeeldetail";
    }

    @PostMapping("/personeeldetail/{personnelId}")
    public String personeeldetailPost(Model model,
                                      @Valid @ModelAttribute("personnel") Personnel personnel,
                                      BindingResult bindingResult,
                                      @PathVariable Integer personnelId,
                                      @RequestParam Integer domainId) {

        logger.info(String.format("image is null -- %s", personnel.getImage() == null));
        logger.info(String.format("personel id -- %s", personnel.getId()));

        if (bindingResult.hasErrors()) {
            model.addAttribute("personnel", personnel);
            return "admin/personeeldetail";
        }
        if (domainId != personnel.getDomain().getId()) {
            personnel.setDomain(new Domain(domainId));
        }

        personnelRepository.save(personnel);
        return "redirect:/admin/personeellijst";
    }

    @PostMapping("/personnel/imagechange")
    public String changeImage(@ModelAttribute("personnel") Personnel personnel,
                              @RequestParam Integer imageId) {
        Image image = new Image(imageId);
        personnel.setImage(image);

        personnelRepository.save(personnel);

        return "redirect:/admin/personeeldetail/" + personnel.getId();
    }

    @PostMapping("/newpersonnel")
    public String createNewPersonnel(@Valid @ModelAttribute("newPersonnel") Personnel personnel,
                                     @RequestParam Integer domainId) {

        personnel.setDomain(new Domain(domainId));
        personnelRepository.save(personnel);

        return "redirect:/admin/personeeldetail/" + personnel.getId();
    }

    @PostMapping("/personnel/imagechange/{personnelId}")
    public String changeImagePersonnel(@ModelAttribute("personnel") Personnel personnel,
                                       @RequestParam Integer imageId,
                                       @PathVariable Integer personnelId) {

        if (imageId != null && personnel.getImage().getId() != imageId) {
            personnel.setImage(new Image(imageId));
        }

        personnelRepository.save(personnel);

        return "redirect:/admin/personeeldetail/" + personnelId;
    }

    @PostMapping("/personnel/newimage/{personnelId}")
    public String newImagePersonnel(@PathVariable Integer personnelId,
                                    @NotNull @RequestParam MultipartFile newImage,
                                    Model model) throws Exception {

            Personnel personnel = personnelRepository.findById(personnelId).get();
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
            return "admin/personeeldetail";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/personeeldetail";
        }

        personnelRepository.save(personnel);
        return "redirect:/admin/personeeldetail/" + personnelId;
    }

    @PostMapping("/removepersonnel/{personnelId}")
    public String removePersonnel(@PathVariable Integer personnelId) {

        Personnel personnel = personnelRepository.findById(personnelId).get();
        personnelRepository.delete(personnel);

        return "redirect:/admin/personeellijst";
    }

    @PostMapping("/personnel/updatehomepage")
    public String updatePersonnelOnHomepage(@RequestParam(name = "onHomePage[]", required = false) List<Integer> personnelIds,
                                            Model model,
                                            @ModelAttribute("homepageBistro") Page homepageBistro,
                                            @ModelAttribute("homepageBolo") Page homepageBolo) {
        ArrayList<Personnel> personnelHomepageBistro = new ArrayList<>();
        if (personnelIds != null) {
            for (Integer i : personnelIds) {
                Personnel personnel = personnelRepository.findById(i).get();
                if (personnel.getDomain().getId() == 1) {
                    personnelHomepageBistro.add(personnel);
                }
            }
        }

        if (personnelHomepageBistro.size() > 3) {
            model.addAttribute("countNoMatch", true);
            return "admin/personeellijst";
        }

        for (Personnel personnel : personnelHomepageBistro) {
            if (!personnel.getPages().contains(homepageBistro)) {
                personnel.getPages().add(homepageBistro);
            }
        }

        ArrayList<Personnel> allUpdatedPersonnel = new ArrayList<>();
        allUpdatedPersonnel.addAll(personnelHomepageBistro);

        List<Personnel> excludedPersonnel = personnelRepository.getPersonnelExludeList(allUpdatedPersonnel);

        for (Personnel personnel : excludedPersonnel) {
            if (personnel.getDomain().getId() == 1) {
                if (personnel.getPages().contains(homepageBistro)) {
                    personnel.getPages().remove(homepageBistro);
                }
            }
        }

        personnelRepository.saveAll(personnelRepository.findAll());

        return "redirect:/admin/personeellijst";
    }

    @PostMapping("/personnel/updatehomepage/{domainId}")
    public String updatePersonnelOnHomepageWithDomain(@RequestParam(name = "onHomePage[]") List<Integer> personnelIds,
                                                      @PathVariable Integer domainId,
                                                      @ModelAttribute("domain") Domain domain,
                                                      Model model,
                                                      @ModelAttribute("homepageBistro") Page homepageBistro,
                                                      @ModelAttribute("homepageBolo") Page homepageBolo) {

        List<Personnel> personnelList = (List<Personnel>) personnelRepository.findAllById(personnelIds);

        if (personnelList.size() != 3) {
            model.addAttribute("countNoMatch", true);
            return "admin/personeellijst";
        }
        List<Personnel> AllPersonnelDomain = personnelRepository.getByDomain(domain);

        for (Personnel personnel : personnelList) {
            if (domain.getId() == 1) {
                if (!personnel.getPages().contains(homepageBistro)) {
                    personnel.getPages().add(homepageBistro);
                }
            }
        }
        List<Personnel> personnelExclude = personnelRepository.getPersonnelExludeList(personnelList);
        for (Personnel personnel : personnelExclude) {
            if (domain.getId() == 1) {
                if (personnel.getPages().contains(homepageBistro)) {
                    personnel.getPages().remove(homepageBistro);
                }
            }

        }
        personnelRepository.saveAll(AllPersonnelDomain);

        return "redirect:/admin/personeellijst/" + domainId;
    }
}
