package be.thomasmore.be.websitelientjes.controllers.bolo;

import be.thomasmore.be.websitelientjes.controllers.wrapperclass.TextWrapper;
import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/bolo")
@Controller
public class BoloContactController {

    @Autowired
    SocialMediaRepository socialMediaRepository;
    @Autowired
    ContactInfoRepository contactInfoRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    ContactFormRepository contactFormRepository;
    @Autowired
    ContactTypeRepository contactTypeRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    TextFragmentRepository textFragmentRepository;

    @ModelAttribute("domain")
    public Domain getDomain(){
        return domainRepository.findById(2).get();
    }

    @ModelAttribute("contactInfoList")
    public List<ContactInfo> getContactInfoList(@ModelAttribute("domain") Domain domain){
        return contactInfoRepository.getByDomain(domain);
    }

    @ModelAttribute("socialMediaList")
    public List<SocialMedia> getSocialMediaList(@ModelAttribute("domain") Domain domain){
        return socialMediaRepository.findByDomain(domain);
    }

    @ModelAttribute("contactForm")
    public ContactForm newContactForm(){
        return new ContactForm();
    }

    @ModelAttribute("contactTypeList")
    public List<ContactType> getContactTypeList(@ModelAttribute("domain") Domain domain){
        return contactTypeRepository.getByDomain(domain);
    }

    @ModelAttribute("pageName")
    public String getPagename(){
        return "contact";
    }

    @ModelAttribute("page")
    public Page getPage(){
        return  pageRepository.findById(7).get();
    }

    @ModelAttribute("textWrapper")
    public TextWrapper getTextWrapper(@ModelAttribute("page") Page page){
        TextWrapper wrapper = new TextWrapper();
        wrapper.setHeaderText((ArrayList<TextFragment>) textFragmentRepository.getByPageAndHeaderText(page, true));
        wrapper.setParagraphText((ArrayList<TextFragment>) textFragmentRepository.getByPageAndHeaderText(page, false));
        return wrapper;
    }

    @GetMapping("/contact")
    public String contact(){
        return "bolo/contact";
    }

    @GetMapping("/contactbevestiging")
    public String contactbevestiging(){
        return "bolo/contactconfirmation";
    }

    @PostMapping("/contactform")
    public String contactformPost(@Valid @ModelAttribute("contactForm") ContactForm contactForm,
                                  BindingResult bindingResult,
                                  @RequestParam Integer contactTypeId){
        if(bindingResult.hasErrors()){
            return "bolo/contact";
        }

        contactForm.setContactType(new ContactType(contactTypeId));
        contactForm.setTimestamp(new Date());
        contactForm.setQuestion(saveLineBreaks(contactForm.getQuestion()));
        contactFormRepository.save(contactForm);
        return "redirect:/bolo/contactbevestiging";
    }


    public String saveLineBreaks(String text) {
        return text.replaceAll("\n", "<br/>");
    }
}
