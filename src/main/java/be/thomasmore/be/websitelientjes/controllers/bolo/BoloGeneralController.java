package be.thomasmore.be.websitelientjes.controllers.bolo;

import be.thomasmore.be.websitelientjes.controllers.wrapperclass.TextWrapper;
import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/bolo")
public class BoloGeneralController {

    @Autowired
    PageRepository pageRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    PersonnelRepository personnelRepository;
    @Autowired
    OpeningsuurRepository openingsuurRepository;
    @Autowired
    SocialMediaRepository socialMediaRepository;
    @Autowired
    ReferenceRepository referenceRepository;
    @Autowired
    TextFragmentRepository textFragmentRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    ImageGoogleRepository imageGoogleRepository;
    @Autowired
    TelephoneNumberRepository telephoneNumberRepository;


    @ModelAttribute("referenceList")
    public List<Reference> getReferenceList(@ModelAttribute("domain") Domain domain){
        return referenceRepository.getByDomain(domain);
    }

    @ModelAttribute("imageLocations")
    public String getImageString(@ModelAttribute("domain") Domain domain){
        List<ImageGoogle> imageGoogleList = imageGoogleRepository.getByDomain(domain);
        String imageLocations = "[";
        for(ImageGoogle ig : imageGoogleList){
            imageLocations += "\"https://www.lientjes.be" + ig.getImage().getImageLocation() + "\"";
            if(ig != imageGoogleList.get(imageGoogleList.size() -1)){
                imageLocations += ", \n";
            }
        }
        imageLocations += "]";
        return imageLocations;
    }

    @ModelAttribute("openingsurenStructured")
    public String getOpeningsurenStructured(@ModelAttribute("domain") Domain domain){
        List<Openingsuur> openingsuurList = openingsuurRepository.getByDomain(domain);
        String openinsurenStructured = "[";
        DateFormat df = new SimpleDateFormat("HH:mm");
        for (Openingsuur op : openingsuurList){

            switch (op.getDag()) {
                case "maandag":
                    op.setDag("Monday");
                    break;
                case "dinsdag":
                    op.setDag("Tuesday");
                    break;
                case "woensdag":
                    op.setDag("Wednesday");
                    break;
                case("donderdag"):
                    op.setDag("Thursday");
                    break;
                case("vrijdag"):
                    op.setDag("Friday");
                    break;
                case("zaterdag"):
                    op.setDag("Saturday");
                    break;
                case("zondag"):
                    op.setDag("Sunday");
                    break;
            }

            if(op.getOpeningsuur() != null) {
                if(!openinsurenStructured.equals("[")){
                    openinsurenStructured += ",";
                }

                openinsurenStructured += "\n{ " +
                        "\n \"@type\" : \"OpeningHoursSpecification\" ," +
                        "\n \"dayOfWeek\" : \"" + op.getDag() + "\"," +
                        "\n \"opens\" : \"" + df.format(op.getOpeningsuur()) + "\"," +
                        "\n \"closes\" : \"" + df.format(op.getSluitingsuur()) + "\"" +
                        "\n}";
            }
        }
        openinsurenStructured += "]";
        return openinsurenStructured;
    }

    @ModelAttribute("domain")
    public Domain getdomain(){
        return domainRepository.findById(2).get();
    }

    @ModelAttribute("address")
    public Address getAddress(@ModelAttribute("domain") Domain domain){
        return addressRepository.getByDomain(domain);
    }

    @ModelAttribute("telephoneNumber")
    public TelephoneNumber getTelephoneNumber(@ModelAttribute("domain") Domain domain){
        return telephoneNumberRepository.getTelephoneNumberByDomain(domain);
    }

    @ModelAttribute("personnelList")
    public List<Personnel> getPersonnelList(@ModelAttribute("domain") Domain domain){
        return personnelRepository.getByDomain(domain);
    }

    @ModelAttribute("openingsuren")
    public List<Openingsuur> getOpeningsuren(@ModelAttribute("domain") Domain domain){
        return openingsuurRepository.getByDomain(domain);
    }

    @ModelAttribute("socialMediaList")
    public List<SocialMedia> getSocialMediaList(@ModelAttribute("domain") Domain domain){
        return socialMediaRepository.findByDomain(domain);
    }

    @ModelAttribute("page")
    public Page getPage(){
        return pageRepository.findById(6).get();
    }

    @ModelAttribute("textWrapper")
    public TextWrapper getTextWrapper(@ModelAttribute("page") Page page){
        TextWrapper wrapper = new TextWrapper();
        wrapper.setHeaderText((ArrayList<TextFragment>) textFragmentRepository.getByPageAndHeaderText(page, true));
        wrapper.setParagraphText((ArrayList<TextFragment>) textFragmentRepository.getByPageAndHeaderText(page, false));
        return wrapper;

    }

    @GetMapping({"", "/", "/home"})
    public String home(Model model,
                       @ModelAttribute("domain") Domain domain,
                       @ModelAttribute("personnelList") List<Personnel> personnelList){

        model.addAttribute("pageName", "home");
        Integer listSize = personnelList.size();
        Integer iterationSize = listSize/2;

        model.addAttribute("page", pageRepository.getByDomainAndPageId(domain, 6));
        model.addAttribute("iterationSize", iterationSize);
        return "bolo/home";
    }

    @GetMapping("/references")
    public String reference(Model model){
        model.addAttribute("pageName", "reference");
        return "bolo/references";
    }
}
