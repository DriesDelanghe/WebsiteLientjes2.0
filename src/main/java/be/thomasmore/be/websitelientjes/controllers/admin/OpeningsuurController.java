package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.controllers.wrapperclass.OpeningsuurWrapper;
import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.models.Openingsuur;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.OpeningsuurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class OpeningsuurController {

    @Autowired
    OpeningsuurRepository openingsuurRepository;

    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    DomainRepository domainRepository;

    Logger logger = LoggerFactory.getLogger(OpeningsuurController.class);

    @ModelAttribute("domainBistro")
    public Domain getDomainBistro() {
        return domainRepository.findById(1).get();
    }

    @ModelAttribute("domainBolo")
    public Domain getDomainBolo() {
        return domainRepository.findById(2).get();
    }

    @ModelAttribute("menuSectionListBistro")
    public List<MenuSection> getMenuSectionListBistro(@ModelAttribute("domainBistro") Domain domainBistro) {
        return menuSectionRepository.getByDomain(domainBistro);
    }
    @ModelAttribute("menuSectionListBistro")
    public List<MenuSection> getMenuSectionListBolo(@ModelAttribute("domainBolo") Domain domainBolo) {
        return menuSectionRepository.getByDomain(domainBolo);
    }


    @ModelAttribute("openingsuurWrapper")
    public OpeningsuurWrapper getOpeningsuurListWrapper(@ModelAttribute("domainBistro") Domain domainBistro,
                                                      @ModelAttribute("domainBolo") Domain domainBolo){
        OpeningsuurWrapper wrapper = new OpeningsuurWrapper();
        wrapper.setOpeningsuurListBistro(openingsuurRepository.getByDomain(domainBistro));
        wrapper.setOpeningsuurListBolo(openingsuurRepository.getByDomain(domainBolo));

        return wrapper;
    }

    @ModelAttribute("domain")
    public Domain getDomain(@PathVariable(required = false) Integer domainId){
        if(domainId != null){
            Optional<Domain> optionalDomain = domainRepository.findById(domainId);
            if(optionalDomain.isPresent()){
                return optionalDomain.get();
            }
        }
        return null;
    }

    @GetMapping({"/openingsuurlijst", "/openingsuurlijst/{domainId}"})
    public String openingsuurlijstGet(){
        return "admin/openingsuurlijst";
    }

    @PostMapping("/changehours/{domainId}")
    public String changeHours(@PathVariable Integer domainId,
                              @ModelAttribute("openingsuurWrapper") OpeningsuurWrapper wrapper,
                              @RequestParam("geslotenIds") List<Integer> geslotenIds){

        if(domainId == 1) {
            for(Openingsuur op : wrapper.getOpeningsuurListBistro()){
                if(geslotenIds.contains(op.getId())){
                    op.setOpeningsuur(null);
                    op.setSluitingsuur(null);
                }
            }
            openingsuurRepository.saveAll(wrapper.getOpeningsuurListBistro());
            logger.info("updating hours bistro");
            return "redirect:/admin/openingsuurlijst#bistro";
        }
        if(domainId == 2) {
            for(Openingsuur op : wrapper.getOpeningsuurListBolo()){
                if(geslotenIds.contains(op.getId())){
                    op.setOpeningsuur(null);
                    op.setSluitingsuur(null);
                }
            }
            openingsuurRepository.saveAll(wrapper.getOpeningsuurListBolo());
            logger.info("updating hours bolo");

            return "redirect:/admin/openingsuurlijst#bolo";
        }


        return "redirect:/admin/openingsuurlijst";
    }
}
