package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

@RequestMapping("/admin")
@Controller
public class AdminGeneralInfoController {

    @Autowired
    ContactInfoRepository contactInfoRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    IconRepository iconRepository;

    @Value("${upload.images.dir}")
    private String uploadImagesDirString;

    Logger logger = LoggerFactory.getLogger(AdminGeneralInfoController.class);

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

    @ModelAttribute("contactInfoListBistro")
    public List<ContactInfo> contactInfoListBistro(@ModelAttribute("domainBistro") Domain domain) {
        return contactInfoRepository.getByDomain(domain);
    }

    @ModelAttribute("contactInfoListBolo")
    public List<ContactInfo> contactInfoListBolo(@ModelAttribute("domainBolo") Domain domain) {
        return contactInfoRepository.getByDomain(domain);
    }

    @ModelAttribute("domain")
    public Domain getDomain(@PathVariable(required = false) Integer domainId) {
        if (domainId != null) {
            Optional<Domain> optionalDomain = domainRepository.findById(domainId);
            if (optionalDomain.isPresent()) {
                return optionalDomain.get();
            }
        }
        return null;
    }

    @ModelAttribute("newContactInfo")
    public ContactInfo getNewContactInfo() {
        return new ContactInfo();
    }

    @ModelAttribute("contactInfo")
    public ContactInfo getContactInfo(@PathVariable(required = false) Integer contactInfoId){
        if(contactInfoId != null){
            Optional<ContactInfo> optionalContactInfo = contactInfoRepository.findById(contactInfoId);
            if(optionalContactInfo.isPresent()){
                return optionalContactInfo.get();
            }
        }
        return null;
    }

    @ModelAttribute("contactInfoIcons")
    public List<Icon> getGeneralInfoIcons(){
        return iconRepository.getInfoContentIcons();
    }

    @GetMapping({"/contactinfolijst", "/contactinfolijst/{domainId}"})
    public String algemeneInfoLijstGet(@PathVariable(required = false) Integer domainId) {

        return "admin/contactinfolist";
    }

    @GetMapping("/contactinfo/{contactInfoId}")
    public String contactInfoGet(@PathVariable(required = false) Integer contactInfoId){

        return "admin/contactinfo";
    }

    @PostMapping("/newcontactinfo")
    public String newContactInfoPost(@Valid @ModelAttribute("newContactInfo") ContactInfo contactInfo,
                                     BindingResult bindingResult,
                                     @RequestParam Integer domainId,
                                     @ModelAttribute("contactInfoIcons") List<Icon> contactInfoIcons){
        if(bindingResult.hasErrors()){
            return "admin/contactinfolist";
        }

        contactInfo.setDomain(new Domain(domainId));
        contactInfo.setIcon(contactInfoIcons.get(0));
        contactInfoRepository.save(contactInfo);

        return "redirect:/admin/contactinfolijst";
    }

    @PostMapping("/changecontactinfo/{contactInfoId}")
    public String changeContactInfoPost(@PathVariable Integer contactInfoId,
                                        @Valid @ModelAttribute("contactInfo") ContactInfo contactInfo,
                                        BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/contactinfo";
        }

        contactInfoRepository.save(contactInfo);
        return "redirect:/admin/contactinfolijst";

    }

    @PostMapping("/removecontactinfo/{contactInfoId}")
    public String removeContactInfoPost(@ModelAttribute("contactInfo") ContactInfo contactInfo,
                                        @PathVariable Integer contactInfoId){
        contactInfoRepository.delete(contactInfo);
        return "redirect:/admin/contactinfolijst";
    }

    @PostMapping("/contactinfo/iconchange/{contactInfoId}")
    public String updateIcon(@RequestParam Integer iconId,
                             @ModelAttribute("contactInfo") ContactInfo contactInfo,
                             @PathVariable Integer contactInfoId){
        contactInfo.setIcon(new Icon(iconId));
        contactInfoRepository.save(contactInfo);

        return "redirect:/admin/contactinfo/" + contactInfo.getId();
    }
}
