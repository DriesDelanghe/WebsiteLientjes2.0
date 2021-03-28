package be.thomasmore.be.websitelientjes.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @GetMapping({"/", "", "/home"})
    public String home(Model model){

        return "admin/home";
    }
}
