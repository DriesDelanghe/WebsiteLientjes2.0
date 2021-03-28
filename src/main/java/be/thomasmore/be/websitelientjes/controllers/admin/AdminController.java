package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Personnel;
import be.thomasmore.be.websitelientjes.repositories.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    PersonnelRepository personnelRepository;

    @GetMapping({"/", "", "/home"})
    public String home(Model model){

        List<Personnel> personnelList = (List<Personnel>) personnelRepository.findAll();
        Integer listSize = personnelList.size();
        Integer iterationSize = listSize/4;

        model.addAttribute("iterationSize", iterationSize);
        model.addAttribute("personnelList", personnelList);
        return "admin/home";
    }
}
