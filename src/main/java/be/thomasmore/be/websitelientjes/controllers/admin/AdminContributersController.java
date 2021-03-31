package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Reference;
import be.thomasmore.be.websitelientjes.repositories.ReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminContributersController {

    @Autowired
    ReferenceRepository referenceRepository;

    @ModelAttribute("contributers")
    public List<Reference> getContributers(){
        return (List<Reference>) referenceRepository.findAll();
    }

    @GetMapping("contributers")
    public String contributerPage(){
        return "admin/contributers";
    }

}
