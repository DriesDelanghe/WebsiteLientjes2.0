package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Reference;
import be.thomasmore.be.websitelientjes.repositories.ReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @ModelAttribute("contributer")
    public Reference getContributer(@PathVariable(required = false) Integer contributerId){
        if(contributerId != null) {
            return referenceRepository.findById(contributerId).get();
        }
        return  null;
    }
    @ModelAttribute("newContributer")
    public Reference getNewContributer(){
        return new Reference();
    }

    @GetMapping("contributers")
    public String contributerPage(){
        return "admin/contributers";
    }

    @GetMapping("/contributerdetail/{contributerId}")
    public String contributerDetailPage(@PathVariable Integer contributerId){
        return "admin/contributerdetail";
    }

    @PostMapping("/updatecontributer/{contributerId}")
    public String updateContributer(@Valid @ModelAttribute("contributer") Reference reference,
                                    BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/contributerdetail";
        }

        referenceRepository.save(reference);
        return "redirect:/admin/contributers";
    }

    @PostMapping("/newcontributer")
    public String newContributer(@Valid @ModelAttribute("newContributer") Reference reference,
                                 BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/contributers";
        }

        referenceRepository.save(reference);
        return "redirect:/admin/contributers";
    }

    @PostMapping("/removecontributer/{contributerId}")
    public String removeContributer(@PathVariable Integer contributerId,
                                    @ModelAttribute("contributer") Reference reference){
        referenceRepository.delete(reference);
        return "redirect:/admin/contributers";
    }
}
