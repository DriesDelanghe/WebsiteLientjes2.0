package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Allergie;
import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.models.Product;
import be.thomasmore.be.websitelientjes.repositories.AllergieRepository;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class AdminAllergieController {
    @Autowired
    AllergieRepository allergieRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    ProductRepository productRepository;


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

    @ModelAttribute("allergyList")
    public List<Allergie> getAllergieList(){
        return (List<Allergie>) allergieRepository.findAll();
    }

    @ModelAttribute("allergy")
    public Allergie getAllergy(@PathVariable(required = false) Integer allergyId){
        if(allergyId != null){
            Optional<Allergie> allergieOptional = allergieRepository.findById(allergyId);
            if(allergieOptional.isPresent()){
                return allergieOptional.get();
            }
        }
        return null;
    }


    @GetMapping("/allergielijst")
    public String allergieLijstPage(){

        return "admin/allergylist";
    }

    @GetMapping("/allergie/{allergyId}")
    public String allergieItemPage(){

        return "admin/allergie";
    }



    @PostMapping("/removeproductfromallergie/{allergyId}/{productId}")
    public String removeProductFromAllergie(@ModelAttribute("allergy") Allergie allergie,
                                            @PathVariable Integer productId){

        Product product = productRepository.findById(productId).get();
        product.getAllergies().remove(allergie);
        allergie.getProducts().remove(product);
        allergieRepository.save(allergie);
        productRepository.save(product);

        return "redirect:/admin/allergie/" + allergie.getId();
    }
}
