package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Allergie;
import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.models.Product;
import be.thomasmore.be.websitelientjes.repositories.AllergieRepository;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    Logger logger = LoggerFactory.getLogger(AdminAllergieController.class);

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

    @ModelAttribute("allProductsBistro")
    public MenuSection getAllProductsBistro() {
        return menuSectionRepository.findById(1).get();
    }

    @ModelAttribute("allProductsBolo")
    public MenuSection getAllProductsBolo() {
        return menuSectionRepository.findById(2).get();
    }

    @ModelAttribute("allergyList")
    public List<Allergie> getAllergieList() {
        return (List<Allergie>) allergieRepository.findAll();
    }

    @ModelAttribute("allergy")
    public Allergie getAllergy(@PathVariable(required = false) Integer allergyId) {
        if (allergyId != null) {
            Optional<Allergie> allergieOptional = allergieRepository.findById(allergyId);
            if (allergieOptional.isPresent()) {
                return allergieOptional.get();
            }
        }
        return null;
    }

    @ModelAttribute("newAllergy")
    public Allergie newAllergie() {
        return new Allergie();
    }


    @GetMapping("/allergielijst")
    public String allergieLijstPage() {

        return "admin/allergylist";
    }

    @GetMapping("/allergie/{allergyId}")
    public String allergieItemPage() {

        return "admin/allergie";
    }


    @PostMapping("/removeproductfromallergie/{allergyId}")
    public String removeProductFromAllergie(@ModelAttribute("allergy") Allergie allergie,
                                            @RequestParam(name = "productId[]") List<Integer> productIds) {

        List<Product> productsToRemove = (List<Product>) productRepository.findAllById(productIds);
        for (Product p : productsToRemove) {
            p.getAllergies().remove(allergie);
            productRepository.save(p);
        }
        allergie.getProducts().removeAll(productsToRemove);
        allergieRepository.save(allergie);

        return "redirect:/admin/allergie/" + allergie.getId();
    }

    @PostMapping("/addproducttoallergie/{allergyId}")
    public String adProductToAllergie(@ModelAttribute("allergy") Allergie allergie,
                                      @RequestParam(name = "productId[]") List<Integer> productIds) {

        List<Product> productsToAdd = (List<Product>) productRepository.findAllById(productIds);
        for (Product p : productsToAdd) {
            p.getAllergies().add(allergie);
            productRepository.save(p);
        }
        allergie.getProducts().addAll(productsToAdd);
        allergieRepository.save(allergie);

        return "redirect:/admin/allergie/" + allergie.getId();
    }

    @PostMapping("/updateallergie/{allergyId}")
    public String updateAllergie(@Valid @ModelAttribute("allergy") Allergie allergie,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "admin/allergie";
        }
        allergieRepository.save(allergie);

        return "redirect:/admin/allergie/" + allergie.getId();
    }

    @PostMapping("/newallergy")
    public String newAllergiePost(@Valid @ModelAttribute("newAllergy") Allergie allergie,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/allergylist";
        }
        allergieRepository.save(allergie);

        return "redirect:/admin/allergielijst";
    }

    @PostMapping("/removeallergy/{allergyId}")
    public String removeAllergiePost(@PathVariable Integer allergyId) {
        Allergie allergie = allergieRepository.findById(allergyId).get();
        logger.info(allergie.getName());
        List<Product> productList = allergie.getProducts();
        for (Product p : productList) {
            p.getAllergies().remove(allergie);
        }
        allergieRepository.delete(allergie);

        return "redirect:/admin/allergielijst";
    }
}
