package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.controllers.wrapperclass.ContactTypeWrapper;
import be.thomasmore.be.websitelientjes.models.ContactForm;
import be.thomasmore.be.websitelientjes.models.ContactType;
import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.repositories.ContactFormRepository;
import be.thomasmore.be.websitelientjes.repositories.ContactTypeRepository;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class AdminInboxController {
    @Autowired
    ContactFormRepository contactFormRepository;
    @Autowired
    ContactTypeRepository contactTypeRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;

    Logger logger = LoggerFactory.getLogger(AdminInboxController.class);

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

    @ModelAttribute("contactFormList")
    public List<ContactForm> getContactForms(@PathVariable(required = false) Integer contactTypeId){
        if(contactTypeId == null) {
            List<ContactForm> contactFormList = (List<ContactForm>) contactFormRepository.findAll();
            Collections.sort(contactFormList);
            return contactFormList;
        }
        Optional<ContactType> optionalContactType = contactTypeRepository.findById(contactTypeId);
        if(optionalContactType.isPresent()){
            return contactFormRepository.getByContactType(optionalContactType.get());
        }
        return null;
    }

    @ModelAttribute("message")
    public ContactForm getMessage(@PathVariable(required = false) Integer messageId){
        if(messageId != null){
            Optional<ContactForm> messageOptional = contactFormRepository.findById(messageId);
            if(messageOptional.isPresent()){
                return messageOptional.get();
            }

        }
        return null;
    }

    @ModelAttribute("contactTypeList")
    public List<ContactType> getContactTypeList(){
        return (List<ContactType>) contactTypeRepository.findAll();
    }

    @ModelAttribute("contactTypeWrapper")
    public ContactTypeWrapper getContactWrapper(@ModelAttribute("contactTypeList") List<ContactType> contactTypeList){
        ContactTypeWrapper wrapper = new ContactTypeWrapper();
        wrapper.setContactTypeList(contactTypeList);
        return wrapper;
    }

    @ModelAttribute("newContactType")
    public ContactType getNewContactType(){
        return new ContactType();
    }

    @GetMapping({"/inbox", "/inbox/{contactTypeId}"})
    public String inboxPage(@PathVariable(required = false) Integer contactTypeId){

        return "admin/inbox";
    }

    @GetMapping("/inbox/message/{messageId}")
    public String messagePage(@ModelAttribute("message") ContactForm message){
        message.setRead(true);
        logger.info(String.format("set read on message with id %d to -- %s", message.getId(), message.isRead()));
        contactFormRepository.save(message);
        return "admin/inboxmessage";
    }

    @PostMapping("/removemessage/{messageId}")
    public String removeMessage(@PathVariable Integer messageId){
        ContactForm message = contactFormRepository.findById(messageId).get();
        contactFormRepository.delete(message);

        return "redirect:/admin/inbox";
    }

    @PostMapping("/removemessages")
    public String removeMessages(@RequestParam("messageId") List<Integer> messageIds){
        List<ContactForm> contactFormList = (List<ContactForm>) contactFormRepository.findAllById(messageIds);
        contactFormRepository.deleteAll(contactFormList);

        return "redirect:/admin/inbox";
    }
}
