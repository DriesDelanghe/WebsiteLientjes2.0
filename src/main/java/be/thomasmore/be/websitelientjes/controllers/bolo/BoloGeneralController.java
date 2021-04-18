package be.thomasmore.be.websitelientjes.controllers.bolo;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.Openingsuur;
import be.thomasmore.be.websitelientjes.models.Personnel;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.OpeningsuurRepository;
import be.thomasmore.be.websitelientjes.repositories.PageRepository;
import be.thomasmore.be.websitelientjes.repositories.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/bolo")
public class BoloGeneralController {

    @Autowired
    PageRepository pageRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    PersonnelRepository personnelRepository;
    @Autowired
    OpeningsuurRepository openingsuurRepository;

    @ModelAttribute("domain")
    public Domain getdomain(){
        return domainRepository.findById(2).get();
    }

    @ModelAttribute("personnelList")
    public List<Personnel> getPersonnelList(@ModelAttribute("domain") Domain domain){
        return personnelRepository.getByDomain(domain);
    }

    @ModelAttribute("openingsuren")
    public List<Openingsuur> getOpeningsuren(@ModelAttribute("domain") Domain domain){
        return openingsuurRepository.getByDomain(domain);
    }

    @GetMapping({"", "/", "/home"})
    public String home(Model model,
                       @ModelAttribute("domain") Domain domain,
                       @ModelAttribute("personnelList") List<Personnel> personnelList){

        Integer listSize = personnelList.size();
        Integer iterationSize = listSize/2;

        model.addAttribute("page", pageRepository.getByDomainAndPageId(domain, 6));
        model.addAttribute("iterationSize", iterationSize);
        return "bolo/home";
    }
}
