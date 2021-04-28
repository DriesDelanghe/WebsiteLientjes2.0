package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Image;
import be.thomasmore.be.websitelientjes.repositories.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminImageController {
    @Autowired
    ImageRepository imageRepository;

    @Value("${upload.images.dir}")
    private String uploadImagesDirString;

    Logger logger = LoggerFactory.getLogger(AdminImageController.class);


    @ModelAttribute("unusedImages")
    List<Image> getUnusedImages(){
        return imageRepository.getUnusedImages();
    }



    @GetMapping("/afbeeldingen")
    public String getImagePage(){
        return "admin/imagespage";
    }
    @GetMapping("/afbeeldingen/verwijderen")
    public String getImageRemovePage(){
        return "admin/imagesremove";
    }

    @PostMapping("/images/remove")
    public String removeImages(@RequestParam(name = "imageIds[]", required = false) List<Integer> imageIds){
        if(imageIds != null && !imageIds.isEmpty()){
            List<Image> ImagesToRemove = (List<Image>) imageRepository.findAllById(imageIds);

            for(Image image : ImagesToRemove){
                File imageFile = new File(uploadImagesDirString + image.getImageLocation());
                if(imageFile.delete()){
                    logger.info("removed image: " + image.getImageLocation());
                    imageRepository.delete(image);
                }
            }
        }
        if(imageIds == null || imageIds.isEmpty()){
            logger.info("mission failed");
        }

        return "redirect:/admin/afbeeldingen/verwijderen";
    }
}
