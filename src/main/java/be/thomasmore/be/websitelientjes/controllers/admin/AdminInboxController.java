package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.controllers.wrapperclass.*;
import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
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
    @Autowired
    RedirectEmailRepository redirectEmailRepository;

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

    @ModelAttribute("redirectEmailList")
    public List<RedirectEmail> getRedirectEmailList(){
        return (List<RedirectEmail>) redirectEmailRepository.findAll();
    }

    @ModelAttribute("newRedirectEmail")
    public RedirectEmail getNewRedirectEmail(){
        return new RedirectEmail();
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

    @ModelAttribute("contactTypeListBistro")
    public List<ContactType> getContactTypeListBistro(@ModelAttribute("domainBistro") Domain domain){
        return contactTypeRepository.getByDomain(domain);
    }

    @ModelAttribute("unreadList")
    public HashMap<Integer, Integer> getListUnreads(@ModelAttribute("contactTypeList") List<ContactType> contactTypeList){
        HashMap<Integer, Integer> unreadList= new HashMap<>();
        for(ContactType ct : contactTypeList){
            Integer count = contactFormRepository.getUnreadBycontactType(ct).size();
            unreadList.put(ct.getId(), count);
        }
        return unreadList;
    }

    @ModelAttribute("allUnreads")
    public Integer getAllUnreads(){
        return  contactFormRepository.getUnreadBycontactType(null).size();
    }

    @ModelAttribute("contactTypeListBolo")
    public List<ContactType> getContactTypeListBolo(@ModelAttribute("domainBolo") Domain domain){
        return  contactTypeRepository.getByDomain(domain);
    }

    @ModelAttribute("contactTypeWrapper")
    public ContactTypeWrapper getContactWrapper(@ModelAttribute("contactTypeListBistro") List<ContactType> contactTypeListBistro,
                                                @ModelAttribute("contactTypeListBolo") List<ContactType> contactTypeListBolo){
        ContactTypeWrapper wrapper = new ContactTypeWrapper();
        wrapper.setContactTypeListBistro(contactTypeListBistro);
        wrapper.setContactTypeListBolo(contactTypeListBolo);
        return wrapper;
    }

    @ModelAttribute("newContactType")
    public ContactType getNewContactType(){
        return new ContactType();
    }

    @ModelAttribute("contactType")
    public ContactType getContactType(@PathVariable(required = false) Integer contactTypeId){
        if(contactTypeId != null){
            Optional<ContactType> optionalContactType = contactTypeRepository.findById(contactTypeId);
            if(optionalContactType.isPresent()){
                return optionalContactType.get();
            }
        }
        return null;
    }

    @GetMapping({"/inbox", "/inbox/{contactTypeId}"})
    public String inboxPage(@PathVariable(required = false) Integer contactTypeId){

        return "admin/inbox";
    }

    @GetMapping("/inbox/message/{messageId}")
    public String messagePage(@ModelAttribute("message") ContactForm message){
        if(message != null) {
            message.setRead(true);
            logger.info(String.format("set read on message with id %d to -- %s", message.getId(), message.isRead()));
            contactFormRepository.save(message);
        }
        return "admin/inboxmessage";
    }

    @GetMapping("/inbox/instellingen")
    public String inboxSettings(){

        return "admin/inboxsettings";
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

    @PostMapping("/changecontacttypes")
    public String updateContactTypesPost(@ModelAttribute("contactTypeWrapper") ContactTypeWrapper wrapper,
                                         @ModelAttribute("newContactType") ContactType newContactType){

        contactTypeRepository.saveAll(wrapper.getContactTypeListBistro());
        contactTypeRepository.saveAll(wrapper.getContactTypeListBolo());

        if(newContactType.getDomain() != null && !newContactType.getQuestionType().isBlank()){
            contactTypeRepository.save(newContactType);
        }

        return "redirect:/admin/inbox/instellingen";
    }

    @PostMapping("/changecontacttype/{contactTypeId}")
    public String updateContactTypePost(@PathVariable Integer contactTypeId,
                                        @Valid @ModelAttribute("contactType") ContactType contactType,
                                        BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/inboxsettings";
        }

        contactTypeRepository.save(contactType);

        return "redirect:/admin/inbox/instellingen";

    }

    @PostMapping("/removecontacttype/{contactTypeId}")
    public String removeContactTypePost(@PathVariable Integer contactTypeId,
                                        @ModelAttribute("contactType") ContactType contactType,
                                        Model model){
        if(!contactFormRepository.getByContactType(contactType).isEmpty()){
            model.addAttribute("notEmpty", true);
            return "admin/inboxsettings";
        }

        contactTypeRepository.delete(contactType);
        return "redirect:/admin/inbox/instellingen";
    }
}
