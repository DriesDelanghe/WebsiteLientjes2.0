package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.models.Personnel;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
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
public class AdminController {

    @Autowired
    PersonnelRepository personnelRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    DomainRepository domainRepository;


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
    public List<Personnel> getPersonnelList(){
        return (List<Personnel>) personnelRepository.findAll();
    }

    @GetMapping({"/", "", "/home"})
    public String home(Model model, @ModelAttribute("personnelList") List<Personnel> personnelList){

        Integer listSize = personnelList.size();
        Integer iterationSize = listSize/4;

        model.addAttribute("iterationSize", iterationSize);
        return "admin/home";
    }
}
