package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    SymbolRepository symbolRepository;
    @Autowired
    PersonnelRepository personnelRepository;

    @ModelAttribute("personnelList")
    public List<Personnel> getPersonnelList(@ModelAttribute("page") Page page){
        if(page != null){
            return personnelRepository.getByPage(page);
        }
        return null;
    }

    @ModelAttribute("rightArrow")
    public Symbol getArrow() {
        Symbol rightArrow = symbolRepository.getSymbolByReferenceName("rightArrow");

        return rightArrow;
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

    @ModelAttribute("pageListBistro")
    public List<Page> getPageListBistro(@ModelAttribute("domainBistro") Domain domain){
        return pageRepository.getByDomain(domain);
    }
    @ModelAttribute("pageListBolo")
    public List<Page> getPageListBolo(@ModelAttribute("domainBolo") Domain domain){
        return pageRepository.getByDomain(domain);
    }

    @ModelAttribute("page")
    public Page getPage(@PathVariable(required = false) Integer pageId){
        if(pageId != null){
            Optional<Page> optionalPage = pageRepository.findById(pageId);
            if(optionalPage.isPresent()){
                return optionalPage.get();
            }
        }
        return null;
    }

    @ModelAttribute("headerText")
    public List<TextFragment> getHeaderText(@ModelAttribute("page") Page page){
        if(page != null){
            return textFragmentRepository.getByPageAndHeaderText(page, true);
        }
        return null;
    }

    @ModelAttribute("paragraphText")
    public List<TextFragment> getParagraphText(@ModelAttribute("page") Page page){
        if(page != null){
            return textFragmentRepository.getByPageAndHeaderText(page, false);
        }
        return null;
    }

    @GetMapping("/paginaoverzicht")
    public String paginaOverzicht(){
        return "admin/pageslist";
    }

    @GetMapping("/pagina/{pageId}")
    public String pageView(@PathVariable(required = false) Integer pageId,
                           @ModelAttribute("page") Page page){
        return "admin/pages/" + page.getPageName();
    }
}
