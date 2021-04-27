package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Image;
import be.thomasmore.be.websitelientjes.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminImageController {
    @Autowired
    ImageRepository imageRepository;

    @ModelAttribute("unusedImages")
    List<Image> getUnusedImages(){
        return imageRepository.getUnusedImages();
    }

    @GetMapping("/images")
    public String getImagePage(){
        return "admin/imagespage";
    }
}
