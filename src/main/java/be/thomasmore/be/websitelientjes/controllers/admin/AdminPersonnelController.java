package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Personnel;
import be.thomasmore.be.websitelientjes.repositories.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminPersonnelController {

    @Autowired
    PersonnelRepository personnelRepository;

    @ModelAttribute("personnelList")
    public List<Personnel> getPersonnelList(){
        return (List<Personnel>) personnelRepository.findAll();
    }

    @GetMapping("/personeellijst")
    public String personeellijst(Model model){

        return "admin/personeellijst";
    }
}
