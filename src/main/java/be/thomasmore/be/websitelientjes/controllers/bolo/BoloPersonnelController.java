package be.thomasmore.be.websitelientjes.controllers.bolo;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.Personnel;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/bolo")
@Controller
public class BoloPersonnelController {

    @Autowired
    PersonnelRepository personnelRepository;
    @Autowired
    DomainRepository domainRepository;


    @ModelAttribute("personnelList")
    public List<Personnel> getPersonnelList(){
        Domain domain = domainRepository.findById(2).get();
        return personnelRepository.getByDomain(domain);
    }

    @GetMapping("/personeeloverzicht")
    public String personeelOverzicht(Model model){
        model.addAttribute("pageName", "personnel");
        return "bolo/personnel";
    }
}
