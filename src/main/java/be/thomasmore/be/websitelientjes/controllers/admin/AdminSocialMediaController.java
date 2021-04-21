package be.thomasmore.be.websitelientjes.controllers.admin;


import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.util.*;

@RequestMapping("/admin")
@Controller
public class AdminSocialMediaController {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    SocialMediaRepository socialMediaRepository;
    @Autowired
    IconRepository iconRepository;

    @Value("${upload.images.dir}")
    private String uploadImagesDirString;

    Logger logger = LoggerFactory.getLogger(AdminSocialMediaController.class);

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

    @ModelAttribute("menuSectionListBolo")
    public List<MenuSection> getMenuSectionListBolo(@ModelAttribute("domainBolo") Domain domainBolo) {
        return menuSectionRepository.getByDomain(domainBolo);
    }

    @ModelAttribute("socialMediaBistro")
    public List<SocialMedia> getSocialMediaBistro(@ModelAttribute("domainBistro") Domain domain) {
        return socialMediaRepository.findByDomain(domain);
    }

    @ModelAttribute("socialMediaBolo")
    public List<SocialMedia> getSocialMediaBolo(@ModelAttribute("domainBolo") Domain domain) {
        return socialMediaRepository.findByDomain(domain);
    }

    @ModelAttribute("domain")
    public Domain getDomainById(@PathVariable(required = false) Integer domainId) {
        if (domainId != null) {
            Optional<Domain> domainOptional = domainRepository.findById(domainId);
            if (domainOptional.isPresent()) {
                return domainOptional.get();
            }
        }
        return null;
    }

    @ModelAttribute("socialMedia")
    public SocialMedia getSocialMedia(@PathVariable(required = false) Integer socialmediaId) {
        if (socialmediaId != null) {
            Optional<SocialMedia> optionalSocialMedia = socialMediaRepository.findById(socialmediaId);
            if (optionalSocialMedia.isPresent()) {
                return optionalSocialMedia.get();
            }
        }
        return null;
    }

    @ModelAttribute("newSocialMedia")
    public SocialMedia newSocialMediaAttribute() {
        return new SocialMedia();
    }

    @ModelAttribute("socialMediaIcons")
    public List<Icon> getSocialMediaIcons(){
        return iconRepository.getSocialMediaIcons();
    }


    @GetMapping({"/socialmedialijst", "/socialmedialijst/{domainId}"})
    public String getSocialMediaLijst(Model model) {
        return "admin/socialmedialist";
    }

    @GetMapping("/socialmedia/{socialmediaId}")
    public String getSocialMediaPage(Model model, @ModelAttribute("socialMedia") SocialMedia socialMedia) {
        return "admin/socialmedia";
    }

    @PostMapping("/orderchangebistro")
    public String orderChangeBistro(@RequestParam List<Integer> orderReference,
                                    @ModelAttribute("socialMediaBistro") List<SocialMedia> socialMediaList,
                                    Model model) {
        Set<Integer> set = new HashSet<Integer>(orderReference);
        if (set.size() < orderReference.size()) {
            model.addAttribute("duplicates", true);
            return "admin/socialmedialist";
        }

        for (SocialMedia sm : socialMediaList) {
            int index = socialMediaList.indexOf(sm);
            logger.info(String.format("setting order of social media at index %d to %d", index, orderReference.get(index)));
            sm.setOrderReference(orderReference.get(index));
        }

        socialMediaRepository.saveAll(socialMediaList);

        return "redirect:/admin/socialmedialijst";
    }

    @PostMapping("/orderchangebolo")
    public String orderChangeBolo(@RequestParam List<Integer> orderReference,
                                  @ModelAttribute("socialMediaBolo") List<SocialMedia> socialMediaList,
                                  Model model) {
        Set<Integer> set = new HashSet<Integer>(orderReference);
        if (set.size() < orderReference.size()) {
            model.addAttribute("duplicates", true);
            return "admin/socialmedialist";
        }

        for (SocialMedia sm : socialMediaList) {
            int index = socialMediaList.indexOf(sm);
            sm.setOrderReference(orderReference.get(index));
        }

        socialMediaRepository.saveAll(socialMediaList);

        return "redirect:/admin/socialmedialijst";
    }

    @PostMapping("/changesocialmedia/{socialmediaId}")
    public String changeSocialMedia(@Valid @ModelAttribute("socialMedia") SocialMedia socialMedia,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/socialmedia";
        }

        socialMediaRepository.save(socialMedia);

        return "redirect:/admin/socialmedialijst";
    }

    @PostMapping("/newsocialmedia")
    public String newSocialMedia(@Valid @ModelAttribute("newSocialMedia") SocialMedia socialMedia,
                                 BindingResult bindingResult,
                                 @RequestParam Integer domainId,
                                 @ModelAttribute("socialMediaBistro") List<SocialMedia> socialMediaBistro,
                                 @ModelAttribute("socialMediaBolo") List<SocialMedia> socialMediaBolo) {

        if (bindingResult.hasErrors()) {
            return "admin/socialmedialist";
        }
        socialMedia.setDomain(new Domain(domainId));
        List<Icon> iconList = iconRepository.getSocialMediaIcons();
        socialMedia.setIcon(iconList.get(0));
        if (domainId == 1) {
            socialMedia.setOrderReference(socialMediaBistro.size() + 1);
        }
        if (domainId == 2) {
            socialMedia.setOrderReference(socialMediaBolo.size() + 1);
        }

        socialMediaRepository.save(socialMedia);

        return "redirect:/admin/socialmedialijst";
    }

    @PostMapping("/removesocialmedia/{socialmediaId}")
    public String removeSocialMediaPost(@ModelAttribute("socialMedia") SocialMedia socialMedia,
                                        @PathVariable Integer socialmediaId){

        socialMediaRepository.delete(socialMedia);

        return "redirect:/admin/socialmedialijst";
    }

    @PostMapping("/socialmedia/iconchange/{socialmediaId}")
    public String updateIconSocialMedia(@ModelAttribute("socialMedia") SocialMedia socialMedia,
                                        @PathVariable Integer socialmediaId,
                                        @RequestParam Integer iconId){
        socialMedia.setIcon(new Icon(iconId));
        socialMediaRepository.save(socialMedia);
        return "redirect:/admin/socialmedia/" + socialMedia.getId();
    }

}
