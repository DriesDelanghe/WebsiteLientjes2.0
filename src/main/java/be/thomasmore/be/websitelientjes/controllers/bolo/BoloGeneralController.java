package be.thomasmore.be.websitelientjes.controllers.bolo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bolo")
public class BoloGeneralController {

    @GetMapping({"", "/", "/home"})
    public String home(Model model){

        return "bolo/home";
    }
}
