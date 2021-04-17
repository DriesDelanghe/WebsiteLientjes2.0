package be.thomasmore.be.websitelientjes.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LandingController {

    @GetMapping("/")
    public String landing(Principal principal){

        if(principal != null){
            return "redirect:/admin/home";
        }

        return "redirect:/bistro/home";
    }
}
