package be.thomasmore.be.websitelientjes.controllers.bistro;

import be.thomasmore.be.websitelientjes.controllers.JavaMailSender;
import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequestMapping("/bistro")
@Controller
public class BistroContactController {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    ContactInfoRepository contactInfoRepository;
    @Autowired
    TextFragmentRepository textFragmentRepository;
    @Autowired
    SocialMediaRepository socialMediaRepository;
    @Autowired
    ContactTypeRepository contactTypeRepository;
    @Autowired
    ContactFormRepository contactFormRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    TelephoneNumberRepository telephoneNumberRepository;
    @Autowired
    RedirectEmailRepository redirectEmailRepository;

    Logger logger = LoggerFactory.getLogger(BistroContactController.class);

    @ModelAttribute("domain")
    public Domain getDomain(Model model){
        return domainRepository.getByDomainName("bistro");
    }

    @ModelAttribute("page")
    public Page getPage(@ModelAttribute("domain") Domain domain){
        return pageRepository.getByDomainAndPageName(domain, "contact");
    }

    @ModelAttribute("contactTypeList")
    public List<ContactType> getContactTypeList(@ModelAttribute("domain") Domain domain){
        return contactTypeRepository.getByDomain(domain);
    }

    @ModelAttribute("contactInfoList")
    public List<ContactInfo> getContactInfoList(@ModelAttribute("domain") Domain domain){
        return contactInfoRepository.getByDomain(domain);
    }

    @ModelAttribute("headerText")
    public List<TextFragment> getHeaderText(@ModelAttribute("page") Page page){
        List<TextFragment> textFragmentList = textFragmentRepository.getByPageAndHeaderText(page, true);
        Collections.sort(textFragmentList);
        return textFragmentList;
    }

    @ModelAttribute("paragraphText")
    public List<TextFragment> getParagraphText(@ModelAttribute("page") Page page){
        List<TextFragment> textFragmentList = textFragmentRepository.getByPageAndHeaderText(page, false);
        Collections.sort(textFragmentList);
        return textFragmentList;
    }

    @ModelAttribute("socialMediaList")
    public List<SocialMedia> getSocialMediaList(@ModelAttribute("domain") Domain domain){
        List<SocialMedia> socialMediaList = socialMediaRepository.findByDomain(domain);
        Collections.sort(socialMediaList);
        return socialMediaList;
    }

    @ModelAttribute("address")
    public Address getAddress(@ModelAttribute("domain") Domain domain){
        return addressRepository.getByDomain(domain);
    }

    @ModelAttribute("telephoneNumber")
    public TelephoneNumber getTelephoneNumber(@ModelAttribute("domain") Domain domain){
        return telephoneNumberRepository.getTelephoneNumberByDomain(domain);
    }

    @GetMapping("/contact")
    public String contact(Model model, HttpServletRequest request) {

        model.addAttribute("contactFormObject", new ContactForm());

        logger.info(String.format("Returned contact page to client -- %s", request.getRemoteAddr()));

        return "bistro/contact";
    }

    @PostMapping("/contact")
    public String contactPost(Model model,
                              @Valid @ModelAttribute("contactFormObject") ContactForm contactForm,
                              BindingResult bindingResult,
                              @RequestParam Integer contactTypeId){
        if(bindingResult.hasErrors()){
            model.addAttribute("contactFormObject", contactForm);
            return "bistro/contact";
        }

        contactForm.setQuestion(saveLineBreaks(contactForm.getQuestion()));

        contactForm.setTimestamp(new Date());
        contactForm.setContactType(new ContactType(contactTypeId));
        contactForm.setRead(false);
        contactFormRepository.save(contactForm);

        JavaMailSender sender = new JavaMailSender();
        try{
            if(!redirectEmailRepository.getByContactType(contactForm.getContactType()).isEmpty()) {
                sender.sendmail(redirectEmailRepository.getByContactType(contactForm.getContactType()), contactForm);
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }

        return  "bistro/confirmcontactform";
    }


    public String saveLineBreaks(String text) {
        return text.replaceAll("\n", "<br/>");
    }
}
