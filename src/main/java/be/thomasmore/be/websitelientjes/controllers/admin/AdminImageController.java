package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.Image;
import be.thomasmore.be.websitelientjes.models.ImageGoogle;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.ImageGoogleRepository;
import be.thomasmore.be.websitelientjes.repositories.ImageRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminImageController {
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    ImageGoogleRepository imageGoogleRepository;

    @Value("${upload.images.dir}")
    private String uploadImagesDirString;

    Logger logger = LoggerFactory.getLogger(AdminImageController.class);


    @ModelAttribute("menuSectionListBistro")
    public List<MenuSection> getMenuSectionListBistro(@ModelAttribute("domainBistro") Domain domainBistro) {
        return menuSectionRepository.getByDomain(domainBistro);
    }

    @ModelAttribute("menuSectionListBolo")
    public List<MenuSection> getMenuSectionListBolo(@ModelAttribute("domainBolo") Domain domainBolo) {
        return menuSectionRepository.getByDomain(domainBolo);
    }

    @ModelAttribute("unusedImages")
    List<Image> getUnusedImages() {
        return imageRepository.getUnusedImages();
    }

    @ModelAttribute("domainBistro")
    public Domain getDomainBistro() {
        return domainRepository.findById(1).get();
    }

    @ModelAttribute("domainBolo")
    public Domain getDomainBolo() {
        return domainRepository.findById(2).get();
    }

    @ModelAttribute("domain")
    public Domain getDomain(@PathVariable(required = false) Integer domainId) {
        if (domainId != null) {
            Optional<Domain> optionalDomain = domainRepository.findById(domainId);
            if (optionalDomain.isPresent()) {
                return optionalDomain.get();
            }
        }
        return null;
    }

    @ModelAttribute("imageGoogleBistroList")
    public List<ImageGoogle> getImageGoogleBistroList(@ModelAttribute("domainBistro") Domain domain) {
        List<ImageGoogle> imageGoogleList = imageGoogleRepository.getByDomain(domain);
        Collections.sort(imageGoogleList);
        return imageGoogleList;
    }

    @ModelAttribute("imageGoogleBoloList")
    public List<ImageGoogle> getImageGoogleBoloList(@ModelAttribute("domainBolo") Domain domain) {
        List<ImageGoogle> imageGoogleList = imageGoogleRepository.getByDomain(domain);
        Collections.sort(imageGoogleList);
        return imageGoogleList;
    }

    @ModelAttribute("usedImagesList")
    public List<Image> getUsedImagesList() {
        return imageRepository.getAllGoogleImages();
    }


    @GetMapping("/afbeeldingen")
    public String getImagePage() {
        return "admin/imagespage";
    }

    @GetMapping("/afbeeldingen/verwijderen")
    public String getImageRemovePage() {
        return "admin/imagesremove";
    }

    @GetMapping({"/afbeeldingen/afbeeldingengoogle", "/afbeeldingen/afbeeldingengoogle/{domainId}"})
    public String getImagesGooglePages() {
        return "admin/imagesgoogle";
    }

    @PostMapping("/images/remove")
    public String removeImages(@RequestParam(name = "imageIds[]", required = false) List<Integer> imageIds) {
        if (imageIds != null && !imageIds.isEmpty()) {
            List<Image> ImagesToRemove = (List<Image>) imageRepository.findAllById(imageIds);

            for (Image image : ImagesToRemove) {
                File imageFile = new File(uploadImagesDirString + image.getImageLocation());
                if (imageFile.delete()) {
                    logger.info("removed image: " + image.getImageLocation());
                    imageRepository.delete(image);
                }
            }
        }
        if (imageIds == null || imageIds.isEmpty()) {
            logger.info("mission failed");
        }

        return "redirect:/admin/afbeeldingen/verwijderen";
    }

    @PostMapping("/addgoogleimage/{domainId}")
    public String addGoogleImages(@ModelAttribute("domain") Domain domain,
                                  @NotNull @RequestParam MultipartFile newImage) {

        ImageGoogle ig = new ImageGoogle();
        ig.setDomain(domain);
        try {
            String newImageName = newImage.getOriginalFilename();
            String path = uploadImagesDirString + "/images/google";

            if (!newImageName.isEmpty()) {
                File imageFileDir = new File(path);
                if (!imageFileDir.exists()) imageFileDir.mkdirs();
                File imageFile = new File(path, newImageName);
                logger.info("filesize: " + newImage.getBytes().toString());
                newImage.transferTo(imageFile);
                logger.info(imageFile.getPath());
                Optional<Image> imageOptional = imageRepository.getByImageLocation(imageFile.getPath());
                if (!imageOptional.isPresent()) {
                    Image image = new Image("/images/google/" + newImageName);
                    ig.setImage(image);
                    imageRepository.save(image);
                } else {
                    ig.setImage(imageOptional.get());
                }

                List<ImageGoogle> imageListOnDomain = imageGoogleRepository.getByDomain(domain);
                if (!imageListOnDomain.isEmpty()) {
                    ig.setOrderNr(imageListOnDomain.size() + 1);
                } else {
                    ig.setOrderNr(1);
                }
                imageGoogleRepository.save(ig);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/imagesgoogle";
        }


        return "redirect:/admin/afbeeldingen/afbeeldingengoogle/" + domain.getId();
    }

    @PostMapping("/afbeeldingen/google/orderchange/{imageGoogleId}")
    public String changeOrderImageGoogle(@PathVariable Integer imageGoogleId,
                                         @RequestParam Integer orderNr){

        Optional<ImageGoogle> optionalImageGoogle = imageGoogleRepository.findById(imageGoogleId);

        if(optionalImageGoogle.isPresent()){
            ImageGoogle image = optionalImageGoogle.get();
            Domain domain = image.getDomain();

            List<ImageGoogle> allImageGoogleByDomain = imageGoogleRepository.getByDomain(domain);
            allImageGoogleByDomain.remove(image);
            for(ImageGoogle img : allImageGoogleByDomain){
                if(img.getOrderNr() >= orderNr){
                    img.setOrderNr(img.getOrderNr() + 1);
                }
            }

            image.setOrderNr(orderNr);
            imageGoogleRepository.saveAll(allImageGoogleByDomain);
            imageGoogleRepository.save(image);

            return "redirect:/admin/afbeeldingen/afbeeldingengoogle/" + domain.getId();
        }

        return "redirect:/admin/afbeeldingen/afbeeldingengoogle";
    }

    @PostMapping("/afbeeldingen/google/remove/{imageGoogleId}")
    public String removeGoogleImage(@PathVariable Integer imageGoogleId){
        Optional<ImageGoogle> optionalImageGoogle = imageGoogleRepository.findById(imageGoogleId);
        if(optionalImageGoogle.isPresent()){
            ImageGoogle image = optionalImageGoogle.get();
            imageGoogleRepository.delete(image);

            List<ImageGoogle> allImagesGoogle = imageGoogleRepository.getByDomain(image.getDomain());

            for(ImageGoogle img : allImagesGoogle){
                if(img.getOrderNr() > image.getOrderNr()){
                    img.setOrderNr(img.getOrderNr() - 1);
                }
            }

            imageGoogleRepository.saveAll(allImagesGoogle);

            return "redirect:/admin/afbeeldingen/afbeeldingengoogle/" + image.getDomain().getId();
        }

        return "redirect:/admin/afbeeldingen/afbeeldingengoogle";
    }

    @PostMapping("/images/imagegoogle/{domainId}")
    public String newGoogleImageFromExistingImage(@ModelAttribute("domain") Domain domain,
                                                  @RequestParam Integer imageId){
        ImageGoogle imageGoogle = new ImageGoogle();
        imageGoogle.setImage(new Image(imageId));
        imageGoogle.setDomain(domain);
        imageGoogle.setOrderNr(imageGoogleRepository.getByDomain(domain).size() + 1);

        imageGoogleRepository.save(imageGoogle);

        return "redirect:/admin/afbeeldingen/afbeeldingengoogle/" + domain.getId();
    }

}
