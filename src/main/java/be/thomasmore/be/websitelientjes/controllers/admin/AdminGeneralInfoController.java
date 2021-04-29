package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    TelephoneNumberRepository telephoneNumberRepository;

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

    @ModelAttribute("addressBistro")
    public Address getAddressBistro(@ModelAttribute("domainBistro") Domain domain){
        return addressRepository.getByDomain(domain);
    }

    @ModelAttribute("addressBolo")
    public Address getAddressBolo(@ModelAttribute("domainBolo") Domain domain){
        return addressRepository.getByDomain(domain);
    }
    
    @ModelAttribute("telephoneNumberBistro")
    public TelephoneNumber getTelephoneNumberBistro(@ModelAttribute("domainBistro") Domain domain){
        return telephoneNumberRepository.getTelephoneNumberByDomain(domain);
    }
    
    @ModelAttribute("telephoneNumberBolo")
    public TelephoneNumber getTelephoneNumberBolo(@ModelAttribute("domainBolo") Domain domain){
        return telephoneNumberRepository.getTelephoneNumberByDomain(domain);
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

    @PostMapping("/contactinfo/addresschangebistro")
    public String changeAddressBistro(@Valid @ModelAttribute("addressBistro") Address address,
                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/contactinfolist";
        }

        addressRepository.save(address);
        return "redirect:/admin/contactinfolijst";
    }

    @PostMapping("/contactinfo/addresschangebolo")
    public String changeAddressBolo(@Valid @ModelAttribute("addressBolo") Address address,
                                      BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/contactinfolist";
        }

        addressRepository.save(address);
        return "redirect:/admin/contactinfolijst";
    }
    
    @PostMapping("/contactinfo/telephonechangebistro")
    public String changeTelephoneBistro(@Valid @ModelAttribute("telephoneNumberBistro") TelephoneNumber telephoneNumber,
                                        BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/contactinfolist";
        }
        
        telephoneNumberRepository.save(telephoneNumber);
        return "redirect:/admin/contactinfolijst";
    }

    @PostMapping("/contactinfo/telephonechangebolo")
    public String changeTelephoneBolo(@Valid @ModelAttribute("telephoneNumberBolo") TelephoneNumber telephoneNumber,
                                        BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/contactinfolist";
        }

        telephoneNumberRepository.save(telephoneNumber);
        return "redirect:/admin/contactinfolijst";
    }
}
