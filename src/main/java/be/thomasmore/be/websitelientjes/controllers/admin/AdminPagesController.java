package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.controllers.wrapperclass.TextWrapper;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class AdminPagesController {

    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    TextFragmentRepository textFragmentRepository;
    @Autowired
    PersonnelRepository personnelRepository;
    @Autowired
    OpeningsuurRepository openingsuurRepository;
    @Autowired
    SocialMediaRepository socialMediaRepository;
    @Autowired
    ContactInfoRepository contactInfoRepository;
    @Autowired
    ContactTypeRepository contactTypeRepository;

    Logger logger = LoggerFactory.getLogger(AdminPagesController.class);

    @ModelAttribute("personnelList")
    public List<Personnel> getPersonnelList(@ModelAttribute("page") Page page) {
        if (page != null) {
            return personnelRepository.getByPage(page);
        }
        return null;
    }

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

    @ModelAttribute("textWrapper")
    public TextWrapper getTextWrapper(@ModelAttribute("page") Page page) {
        if (page != null) {
            TextWrapper wrapper = new TextWrapper();
            wrapper.addHeaders(textFragmentRepository.getByPageAndHeaderText(page, true));
            wrapper.addParagraphs(textFragmentRepository.getByPageAndHeaderText(page, false));

            for (TextFragment t : wrapper.getHeaderText()) {
                t.setTextContent(undoLineBreaks(t.getTextContent()));
            }
            for (TextFragment t : wrapper.getParagraphText()) {
                t.setTextContent(undoLineBreaks(t.getTextContent()));
            }

            return wrapper;
        }

        return null;
    }

    @ModelAttribute("pageListBistro")
    public List<Page> getPageListBistro(@ModelAttribute("domainBistro") Domain domain) {
        return pageRepository.getByDomain(domain);
    }

    @ModelAttribute("pageListBolo")
    public List<Page> getPageListBolo(@ModelAttribute("domainBolo") Domain domain) {
        return pageRepository.getByDomain(domain);
    }

    @ModelAttribute("pageListGeneral")
    public List<Page> getPageListGeneral() {
        return pageRepository.getGeneralPages();
    }

    @ModelAttribute("page")
    public Page getPage(@PathVariable(required = false) Integer pageId) {
        if (pageId != null) {
            Optional<Page> optionalPage = pageRepository.findById(pageId);
            if (optionalPage.isPresent()) {
                return optionalPage.get();
            }
        }
        return null;
    }

    @ModelAttribute("openingsuren")
    public List<Openingsuur> getOpeningsuren(@ModelAttribute("page") Page page) {
        if (page != null) {
            return openingsuurRepository.getByDomain(page.getDomain());
        }
        return null;
    }

    @ModelAttribute("socialmediaList")
    public List<SocialMedia> getSocialmediaList(@ModelAttribute("page") Page page) {
        if (page != null) {
            return socialMediaRepository.findByDomain(page.getDomain());
        }
        return null;
    }

    @ModelAttribute("contactInfoList")
    public List<ContactInfo> getContactInfo(@ModelAttribute("page") Page page) {
        if (page != null) {
            return contactInfoRepository.getByDomain(page.getDomain());
        }
        return null;
    }

    @ModelAttribute("contactTypeList")
    public List<ContactType> getContactTypeList(@ModelAttribute("page") Page page) {
        if (page != null) {
            return contactTypeRepository.getByDomain(page.getDomain());
        }
        return null;
    }

    @ModelAttribute("homepageBistro")
    public Page getHomepageBistro() {
        return pageRepository.findById(1).get();
    }

    @ModelAttribute("homepageBolo")
    public Page getHomepageBolo() {
        return pageRepository.findById(6).get();
    }

    @GetMapping("/paginaoverzicht")
    public String paginaOverzicht() {
        return "admin/pageslist";
    }

    @GetMapping("/pagina/{pageId}")
    public String pageView(@PathVariable(required = false) Integer pageId,
                           @ModelAttribute("page") Page page,
                           @ModelAttribute("domainBolo") Domain domainBolo,
                           Model model) {

        if (page != null) {
            if (page.getId() == 1) {
                return "admin/pages/bistrohome";
            }
            if (page.getId() == 3) {
                return "admin/pages/bistrocontact";
            }

            if (page.getId() == 6) {
                List<Personnel> personnelList = personnelRepository.getByDomain(domainBolo);
                Integer listSize = personnelList.size();
                Integer iterationSize = listSize / 2;
                model.addAttribute("iterationSize", iterationSize);
                model.addAttribute("personnelList", personnelList);
                return "admin/pages/bolohome";
            }

            if (page.getId() == 7) {
                return "admin/pages/bolocontact";
            }

            if (page.getId() == 9) {
                return "admin/pages/landingspage";
            }
        }

        return "admin/pages/nopage";
    }

    @PostMapping("/updatetext/{pageId}")
    public String updateTextPost(@Valid @ModelAttribute("textWrapper") TextWrapper textWrapper,
                                 BindingResult bindingResult,
                                 @PathVariable Integer pageId,
                                 @ModelAttribute("page") Page page,
                                 @ModelAttribute("domainBolo") Domain domainBolo,
                                 Model model) {

        boolean hasBlank = false;
        Iterator<TextFragment> it = textWrapper.getParagraphText().iterator();

        while (it.hasNext() && !hasBlank) {
            TextFragment t = it.next();
            if (t.getTextContent().isBlank()) {
                hasBlank = true;
            }
        }

        Iterator<TextFragment> itH = textWrapper.getHeaderText().iterator();
        while (itH.hasNext() && !hasBlank) {
            TextFragment t = itH.next();
            if (t.getTextContent().isBlank()) {
                hasBlank = true;
            }
        }


        if (bindingResult.hasErrors() || hasBlank) {
            if (hasBlank) {
                model.addAttribute("hasBlank", true);
            }

            if (page.getId() == 1) {
                return "admin/pages/bistrohome";
            }
            if (page.getId() == 3) {
                return "admin/pages/bistrocontact";
            }

            if (page.getId() == 6) {
                List<Personnel> personnelList = personnelRepository.getByDomain(domainBolo);
                Integer listSize = personnelList.size();
                Integer iterationSize = listSize / 2;
                model.addAttribute("iterationSize", iterationSize);
                model.addAttribute("personnelList", personnelList);
                return "admin/pages/bolohome";
            }

            if (page.getId() == 7) {
                return "admin/pages/bolocontact";
            }

            if (page.getId() == 9) {
                return "admin/pages/landingspage";
            }

            return "admin/pages/nopage";
        }


        for (TextFragment t : textWrapper.getHeaderText()) {
            t.setTextContent(saveLineBreaks(t.getTextContent()));
        }
        for (TextFragment t : textWrapper.getParagraphText()) {
            t.setTextContent(saveLineBreaks(t.getTextContent()));
        }

        textFragmentRepository.saveAll(textWrapper.getHeaderText());
        textFragmentRepository.saveAll(textWrapper.getParagraphText());
        return "redirect:/admin/pagina/" + page.getId();
    }

    public String saveLineBreaks(String text) {
        return text.replaceAll("\n", "<br/>");
    }

    public String undoLineBreaks(String text) {
        return text.replaceAll("<br/>", "\n");
    }
}
