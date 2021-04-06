package be.thomasmore.be.websitelientjes.controllers.admin;


import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.ImageRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.SocialMediaRepository;
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
import java.io.FileWriter;
import java.io.Reader;
import java.util.*;

@RequestMapping("/admin")
@Controller
public class AdminSocialMediaController {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    SocialMediaRepository socialMediaRepository;
    @Autowired
    ImageRepository imageRepository;
    @Value("${upload.images.dir}")
    private String uploadImagesDirString;

    Logger logger = LoggerFactory.getLogger(AdminSocialMediaController.class);

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
    public List<SocialMedia> getSocialMediaBistro(@ModelAttribute("domainBistro") Domain domain) {
        return socialMediaRepository.findByDomain(domain);
    }

    @ModelAttribute("socialMediaBolo")
    public List<SocialMedia> getSocialMediaBolo(@ModelAttribute("domainBolo") Domain domain) {
        return socialMediaRepository.findByDomain(domain);
    }

    @ModelAttribute("domain")
    public Domain getDomainById(@PathVariable(required = false) Integer domainId) {
        if (domainId != null) {
            Optional<Domain> domainOptional = domainRepository.findById(domainId);
            if (domainOptional.isPresent()) {
                return domainOptional.get();
            }
        }
        return null;
    }

    @ModelAttribute("socialMedia")
    public SocialMedia getSocialMedia(@PathVariable(required = false) Integer socialmediaId) {
        if (socialmediaId != null) {
            Optional<SocialMedia> optionalSocialMedia = socialMediaRepository.findById(socialmediaId);
            if (optionalSocialMedia.isPresent()) {
                return optionalSocialMedia.get();
            }
        }
        return null;
    }

    @ModelAttribute("images")
    public List<Image> getImages() {
        return imageRepository.getAllSocialMediaImages();
    }

    @ModelAttribute("newSocialMedia")
    public SocialMedia newSocialMediaAttribute() {
        return new SocialMedia();
    }

    @GetMapping({"/socialmedialijst", "/socialmedialijst/{domainId}"})
    public String getSocialMediaLijst(Model model) {
        return "admin/socialmedialist";
    }

    @GetMapping("/socialmedia/{socialmediaId}")
    public String getSocialMediaPage(Model model, @ModelAttribute("socialMedia") SocialMedia socialMedia) {
        return "admin/socialmedia";
    }

    @PostMapping("/socialmedia/imagechange/{socialmediaId}")
    public String changeImageSocialMedia(@ModelAttribute("socialMedia") SocialMedia socialMedia,
                                         @RequestParam Integer imageId,
                                         @PathVariable Integer socialmediaId) {
        if (imageId != null && socialMedia.getImage().getId() != imageId) {
            socialMedia.setImage(new Image(imageId));
        }

        socialMediaRepository.save(socialMedia);

        return "redirect:/admin/socialmedia/" + socialmediaId;
    }

    @PostMapping("/orderchangebistro")
    public String orderChangeBistro(@RequestParam List<Integer> orderReference,
                                    @ModelAttribute("socialMediaBistro") List<SocialMedia> socialMediaList,
                                    Model model) {
        Set<Integer> set = new HashSet<Integer>(orderReference);
        if (set.size() < orderReference.size()) {
            model.addAttribute("duplicates", true);
            return "admin/socialmedialist";
        }

        for (SocialMedia sm : socialMediaList) {
            int index = socialMediaList.indexOf(sm);
            logger.info(String.format("setting order of social media at index %d to %d", index, orderReference.get(index)));
            sm.setOrderReference(orderReference.get(index));
        }

        socialMediaRepository.saveAll(socialMediaList);

        return "redirect:/admin/socialmedialijst";
    }

    @PostMapping("/orderchangebolo")
    public String orderChangeBolo(@RequestParam List<Integer> orderReference,
                                  @ModelAttribute("socialMediaBolo") List<SocialMedia> socialMediaList,
                                  Model model) {
        Set<Integer> set = new HashSet<Integer>(orderReference);
        if (set.size() < orderReference.size()) {
            model.addAttribute("duplicates", true);
            return "admin/socialmedialist";
        }

        for (SocialMedia sm : socialMediaList) {
            int index = socialMediaList.indexOf(sm);
            sm.setOrderReference(orderReference.get(index));
        }

        socialMediaRepository.saveAll(socialMediaList);

        return "redirect:/admin/socialmedialijst";
    }

    @PostMapping("/changesocialmedia/{socialmediaId}")
    public String changeSocialMedia(@Valid @ModelAttribute("socialMedia") SocialMedia socialMedia,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/socialmedia";
        }

        socialMediaRepository.save(socialMedia);

        return "redirect:/admin/socialmedialijst";
    }

    @PostMapping("/newsocialmedia")
    public String newSocialMedia(@Valid @ModelAttribute("newSocialMedia") SocialMedia socialMedia,
                                 BindingResult bindingResult,
                                 @RequestParam Integer domainId,
                                 @ModelAttribute("socialMediaBistro") List<SocialMedia> socialMediaBistro,
                                 @ModelAttribute("socialMediaBolo") List<SocialMedia> socialMediaBolo) {

        if (bindingResult.hasErrors()) {
            return "admin/socialmedialist";
        }
        socialMedia.setDomain(new Domain(domainId));
        List<Image> imageList = imageRepository.getAllSocialMediaImages();
        socialMedia.setImage(imageList.get(0));
        if (domainId == 1) {
            socialMedia.setOrderReference(socialMediaBistro.size() + 1);
        }
        if (domainId == 2) {
            socialMedia.setOrderReference(socialMediaBolo.size() + 1);
        }

        socialMediaRepository.save(socialMedia);

        return "redirect:/admin/socialmedialijst";
    }

    @PostMapping("/socialmedia/newimage/{socialmediaId}")
    public String newImagePersonnel(@ModelAttribute("socialMedia") SocialMedia socialMedia,
                                    @PathVariable Integer socialmediaId,
                                    @NotNull @RequestParam MultipartFile newImage,
                                    Model model) throws Exception {

        try {
            String newImageName = newImage.getOriginalFilename();
            String path = uploadImagesDirString + "/images/socialmedia";
            logger.info(newImageName);

            String[] nameparts = newImageName.split("\\.");

            logger.info(nameparts[nameparts.length - 1]);

            if (!nameparts[nameparts.length - 1].toLowerCase(Locale.ROOT).equals("svg")) {
                model.addAttribute("falseFileFormat", true);
                return "admin/socialmedia";
            }

            if (!newImageName.isEmpty()) {
                File imageFileDir = new File(path);
                if (!imageFileDir.exists()) imageFileDir.mkdirs();
                File imageFile = new File(path, newImageName);
                int i = 1;
                while(imageFile.exists()){
                    newImageName = newImageName.replaceAll("\\.svg", "").replaceAll("[\\d]+", "").replaceAll("\\(", "").replaceAll("\\)", "");
                    newImageName += "" + i;
                    newImageName += ".svg";
                    imageFile = new File(path, newImageName);
                    i++;
                }
                newImage.transferTo(imageFile);
                logger.info(imageFile.getPath());

                if (imageFile.canRead() && imageFile.canWrite()) {
                    logger.info("this crazy idea might just work");

                    ArrayList<String> dataParts = new ArrayList<>();
                    Scanner myScanner = new Scanner(imageFile);
                    while (myScanner.hasNext()) {
                        String data = myScanner.next();
                        dataParts.addAll(Arrays.asList(data.split("\\s")));
                    }

                    String newData = "";
                    i = 0;
                    for (String s : dataParts) {
                        if (s.contains("fill")) {
                            s = "";
                        }

                        if (s.equals("<svg")) {
                            s += " fill=\"#eee\"";
                        }
                        logger.info(String.format("datapart %d -- %s", i, s));
                        i++;
                        if (!s.isBlank()) {
                            newData += s + " ";
                        }
                    }
                    logger.info(newData);
                    FileWriter myWriter = new FileWriter(imageFile.getAbsolutePath(), false);
                    myWriter.append(newData);
                    myWriter.close();
                }

                Optional<Image> imageOptional = imageRepository.getByImageLocation(imageFile.getPath());
                if (!imageOptional.isPresent()) {
                    Image image = new Image("/images/socialmedia/" + newImageName);
                    socialMedia.setImage(image);
                    imageRepository.save(image);
                } else {
                    socialMedia.setImage(imageOptional.get());
                }

            }
        } catch (FileSizeLimitExceededException fileSizeLimitExceededException) {
            model.addAttribute("FileSizeException", true);
            return "admin/socialmedia";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/socialmedia";
        }

        socialMediaRepository.save(socialMedia);
        return "redirect:/admin/socialmedia/" + socialmediaId;
    }

}
