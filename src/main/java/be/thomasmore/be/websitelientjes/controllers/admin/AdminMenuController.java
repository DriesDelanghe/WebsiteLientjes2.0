package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.models.Image;
import be.thomasmore.be.websitelientjes.repositories.*;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class AdminMenuController {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MenuSubSectionRepository menuSubSectionRepository;
    @Autowired
    AllergieRepository allergieRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Value("${upload.images.dir}")
    private String uploadImagesDirString;

    Logger logger = LoggerFactory.getLogger(AdminMenuController.class);

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

    @ModelAttribute("menuSection")
    public MenuSection getMenuSection(@PathVariable(required = false) Integer menuSectionId) {
        if (menuSectionId != null) {
            try {
                Optional<MenuSection> menuSectionOptional = menuSectionRepository.findById(menuSectionId);
                if (menuSectionOptional.isPresent()) {
                    return menuSectionOptional.get();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @ModelAttribute("menuImages")
    public List<Image> getMenuImages() {
        return imageRepository.getAllMenuImages();
    }

    @ModelAttribute("allergieList")
    public List<Allergie> getAllergieList() {
        return (List<Allergie>) allergieRepository.findAll();
    }

    @ModelAttribute("categoryList")
    public List<ProductCategory> categoryList() {
        return (List<ProductCategory>) categoryRepository.findAll();
    }

    @ModelAttribute("newProduct")
    public Product newProduct(@ModelAttribute("newProduct") Product product) {
        if(product != null){
            return product;
        }
        return new Product();
    }

    @ModelAttribute("newSubSection")
    public MenuSubSection newSubsection() {
        return new MenuSubSection();
    }

    @ModelAttribute("newSection")
    public MenuSection newSection() {
        MenuSection newSection = new MenuSection();
        newSection.setImage(imageRepository.findById(1).get());
        logger.info("image id new Section -- " + newSection.getImage().getId());
        logger.info(String.format("image location new section -- '%s'", newSection.getImage().getImageLocation()));
        return newSection;
    }


    @GetMapping({"/menulijst", "/menulijst/{id}"})
    public String productLijst(Model model, @PathVariable(required = false) Integer id) {

        model.addAttribute("menuSectionList", null);
        model.addAttribute("domain", null);
        try {
            if (id != null) {
                Optional<Domain> domainOptional = domainRepository.findById(id);
                if (domainOptional.isPresent()) {
                    Domain domain = domainOptional.get();
                    List<MenuSection> menuSectionList = menuSectionRepository.getByDomain(domain);
                    model.addAttribute("menuSectionList", menuSectionList);
                    model.addAttribute("domain", domain);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/menulijst";
        }

        logger.info(String.format("menuSectionList is null -- '%s'", model.getAttribute("menuSectionList") == null));
        logger.info(String.format("menuSectionListBistro is null -- '%s'", model.getAttribute("menuSectionListBistro") == null));
        logger.info(String.format("menuSectionListBolo is null -- '%s'", model.getAttribute("menuSectionListBolo") == null));
        return "admin/menulijst";
    }

    @GetMapping({"/menusectie", "/menusectie/{menuSectionId}", "/menusectie/{menuSectionId}/{subSectionId}"})
    public String menuSectie(Model model,
                             @PathVariable(required = false) Integer subSectionId,
                             @PathVariable(required = false) Integer menuSectionId) {

        Integer targetId = (Integer) model.getAttribute("targetId");
        logger.info(String.format("value target id -- '%d'", targetId));

        if(subSectionId != null){
            logger.info("I am the problem child");
            model.addAttribute("targetId", subSectionId);
            logger.info(String.format("targetid"), subSectionId);
            return  "admin/menusectie";
        }
        return "admin/menusectie";
    }

    @GetMapping("/nieuwproduct")
    public String newProductGet(){

        return "admin/newproduct";
    }






    @PostMapping("/menusectie/{menuSectionId}")
    public String menuSectiePost(@PathVariable Integer menuSectionId,
                                 Model model,
                                 @Valid @ModelAttribute("menuSection") MenuSection menuSection,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "admin/menusectie" ;
        }

        logger.info("domain id -- " + menuSection.getId());
        menuSectionRepository.save(menuSection);
        model.addAttribute("changesSaved", true);
        return "redirect:/admin/menusectie/" + menuSectionId;
    }

    @PostMapping("/menu/imagechange/{menuSectionId}")
    public String changeImageMenu(@ModelAttribute("menuSection") MenuSection menuSection,
                                  @RequestParam Integer imageId,
                                  @PathVariable Integer menuSectionId,
                                  Model model) {

        if (imageId != null && menuSection.getImage().getId() != imageId) {
            menuSection.setImage(new Image(imageId));
        }

        menuSectionRepository.save(menuSection);
        model.addAttribute("changesSaved", true);
        return "redirect:/admin/menusectie/" + menuSectionId;
    }

    @PostMapping("/menu/newimage/{menuSectionId}")
    public String newImageMenu(@ModelAttribute("menuSection") MenuSection menuSection,
                               @PathVariable Integer menuSectionId,
                               @NotNull @RequestParam MultipartFile newImage,
                               Model model) throws Exception {
        model.addAttribute("FileSizeException", null);
        try {
            String newImageName = newImage.getOriginalFilename();
            String path = uploadImagesDirString + "/images/food";

            if (!newImageName.isEmpty()) {
                File imageFileDir = new File(path);
                if (!imageFileDir.exists()) imageFileDir.mkdirs();
                File imageFile = new File(path, newImageName);
                logger.info("filesize: " + newImage.getBytes().toString());
                newImage.transferTo(imageFile);
                logger.info(imageFile.getPath());
                Optional<Image> imageOptional = imageRepository.getByImageLocation(imageFile.getPath());
                if (!imageOptional.isPresent()) {
                    Image image = new Image("/images/food/" + newImageName);
                    menuSection.setImage(image);
                    imageRepository.save(image);
                } else {
                    menuSection.setImage(imageOptional.get());
                }
            }
        } catch (FileSizeLimitExceededException fileSizeLimitExceededException) {
            model.addAttribute("FileSizeException", true);
            return "admin/menusectie";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/menusectie";
        }

        menuSectionRepository.save(menuSection);
        model.addAttribute("changesSaved", true);
        return "redirect:/admin/menusectie/" + menuSectionId;
    }

    @PostMapping("/editproduct/{menuSectionId}/{subSectionId}/{productId}")
    public String editProductPost(@ModelAttribute("menuSection") MenuSection menuSection,
                                  @PathVariable Integer productId,
                                  @PathVariable Integer menuSectionId,
                                  @PathVariable Integer subSectionId,
                                  @RequestParam String productName,
                                  @RequestParam BigDecimal productPrice,
                                  @RequestParam String productExtraInfo,
                                  @RequestParam(name = "category[]", required = false) List<Integer> categoryIdList,
                                  @RequestParam(name = "allergy[]", required = false) List<Integer> allergyIdList) {

        Product product = productRepository.findById(productId).get();

        if (product.getName() != productName) {
            product.setName(productName);
        }
        if (product.getPriceInEur() != productPrice) {
            product.setPriceInEur(productPrice);
        }
        if (product.getExtraInfo() != productExtraInfo) {
            product.setExtraInfo(productExtraInfo);
        }

        if (allergyIdList != null) {
            List<Allergie> allergieList = (List<Allergie>) allergieRepository.findAllById(allergyIdList);
            allergieList.forEach(allergie -> logger.info(String.format("Category -- %s", allergie.getName())));
            product.setAllergies(allergieList);
        }
        if (allergyIdList == null) {
            product.getAllergies().removeAll(product.getAllergies());
        }
        if (categoryIdList != null) {
            List<ProductCategory> categoryList = (List<ProductCategory>) categoryRepository.findAllById(categoryIdList);
            categoryList.forEach(category -> logger.info(String.format("Category -- %s", category.getName())));
            product.setCategories(categoryList);
        }
        if (categoryIdList == null) {
            product.getCategories().removeAll(product.getCategories());
        }
        productRepository.save(product);

        return "redirect:/admin/menusectie/" + menuSectionId + "/" + subSectionId + "#" + subSectionId;
    }

    @PostMapping("/menu/subsectionnamechange/{menuSectionId}/{subSectionId}")
    public String editSubSection(@PathVariable Integer menuSectionId,
                                 @PathVariable Integer subSectionId,
                                 @RequestParam String subSectionName,
                                 @RequestParam(required = false) String extraInfo) {

        MenuSubSection menuSubSection = menuSubSectionRepository.findById(subSectionId).get();

        if (!menuSubSection.getName().equals(subSectionName)) {
            menuSubSection.setName(subSectionName);
        }
        menuSubSection.setExtraInfo(extraInfo);

        menuSubSectionRepository.save(menuSubSection);

        return "redirect:/admin/menusectie/" + menuSectionId + "/" + subSectionId + "#" + subSectionId;
    }

    @PostMapping("/newproduct/{menuSectionId}/{subSectionId}")
    public String newMenuProduct(@Valid @ModelAttribute("newProduct") Product product,
                                 BindingResult bindingResult,
                                 @PathVariable Integer menuSectionId,
                                 @PathVariable Integer subSectionId) {

        if (bindingResult.hasErrors()) {
            return "redirect:/admin/menusectie/" + subSectionId + "#" + subSectionId;
        }

        MenuSubSection menuSubSection = new MenuSubSection(subSectionId);

        product.setMenuSubSection(menuSubSection);

        productRepository.save(product);

        return "redirect:/admin/menusectie/" + menuSectionId + "#" + subSectionId;
    }

    @PostMapping("/newsubsection/{menuSectionId}")
    public String newMenuSubSection(@Valid @ModelAttribute("newSubSection") MenuSubSection menuSubSection,
                                    BindingResult bindingResult,
                                    @PathVariable Integer menuSectionId,
                                    @RequestParam String newSubSectionName) {
        logger.info("---- start adding new subsection ----");
        if (bindingResult.hasErrors() || newSubSectionName == null || newSubSectionName.isBlank()) {
            return "admin/menusectie";
        }

        menuSubSection.setName(newSubSectionName);
        menuSubSectionRepository.save(menuSubSection);

        MenuSection section = menuSectionRepository.findById(menuSectionId).get();

        if (section.getDomain().getId() == 1) {
            MenuSection allproductsSection = menuSectionRepository.findById(1).get();
            allproductsSection.getMenuSubSectionList().add(menuSubSection);
            menuSectionRepository.save(allproductsSection);
        }
        if (section.getDomain().getId() == 2) {
            MenuSection allproductsSection = menuSectionRepository.findById(2).get();
            allproductsSection.getMenuSubSectionList().add(menuSubSection);
            menuSectionRepository.save(allproductsSection);
        }
        section.getMenuSubSectionList().add(menuSubSection);

        menuSectionRepository.save(section);

        logger.info("---- end adding new subsection ----");
        return "redirect:/admin/menusectie/" + menuSectionId + "/" + menuSubSection.getId() + "#" + menuSubSection.getId();
    }

    @PostMapping("/newsectionondomain/{domainId}")
    public String newSectionOnDomain(@PathVariable Integer domainId,
                                     @Valid @ModelAttribute("newSection") MenuSection menuSection,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("newSection", menuSection);
            return "admin/menulijst";
        }

        menuSection.setDomain(new Domain(domainId));
        menuSectionRepository.save(menuSection);

        return "redirect:/admin/menulijst/" + domainId;
    }

    @PostMapping("/newsection/{domainId}")
    public String newSectionNoDomain(@PathVariable Integer domainId,
                                     @Valid @ModelAttribute("newSection") MenuSection menuSection,
                                     BindingResult bindingResult,
                                     Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("newSection", menuSection);
            return "admin/menulijst";
        }

        menuSection.setDomain(new Domain(domainId));
        menuSectionRepository.save(menuSection);

        return "redirect:/admin/menulijst";
    }

    @PostMapping("/removeproduct/{menuSectionId}/{subSectionId}/{productId}")
    public String removeProduct(@PathVariable Integer productId,
                                @PathVariable Integer subSectionId,
                                @PathVariable Integer menuSectionId) {
        Product product = productRepository.findById(productId).get();
        productRepository.delete(product);

        return "redirect:/admin/menusectie/" + menuSectionId + "/" + subSectionId + "#" + subSectionId;
    }

    @PostMapping("/removesubsection/{menuSectionId}/{subSectionId}")
    public String removeSubSection(@PathVariable Integer menuSectionId,
                                   @PathVariable Integer subSectionId,
                                   Model model) {

        MenuSubSection subSection = menuSubSectionRepository.findById(subSectionId).get();
        if (!subSection.getProducts().isEmpty()) {
            model.addAttribute("subSectionNotEmpty", true);
            return "admin/menusectie";
        }
        MenuSection menuSection = menuSectionRepository.findById(menuSectionId).get();
        menuSection.getMenuSubSectionList().remove(subSection);
        menuSectionRepository.save(menuSection);

        if (menuSection.getDomain().getId() == 1) {
            MenuSection alleProducten = menuSectionRepository.findById(1).get();
            alleProducten.getMenuSubSectionList().remove(subSection);
            menuSectionRepository.save(alleProducten);
        }
        if (menuSection.getDomain().getId() == 2) {
            MenuSection alleProducten = menuSectionRepository.findById(2).get();
            alleProducten.getMenuSubSectionList().remove(subSection);
            menuSectionRepository.save(alleProducten);
        }

        menuSubSectionRepository.delete(subSection);
        return "redirect:/admin/menusectie/" + menuSectionId;
    }

    @PostMapping("/removesection/{menuSectionId}")
    public String removeMenuSection(@ModelAttribute("menuSection")MenuSection menuSection,
                                    @PathVariable Integer menuSectionId){

        if(!menuSection.getMenuSubSectionList().isEmpty()){
            return "admin/menusectie";
        }
        menuSectionRepository.delete(menuSection);

        return "redirect:/admin/menulijst";
    }

    @PostMapping("/newproductpost")
    public String completeNewProductPost(@Valid @ModelAttribute("newProduct") Product product,
                                         BindingResult bindingResult,
                                         @RequestParam(required = false) Integer subSectionId,
                                         Model model){

        if(bindingResult.hasErrors() || subSectionId == null){
            model.addAttribute("newProduct", product);
            return "admin/newproduct";
        }
        MenuSubSection subSection = menuSubSectionRepository.findById(subSectionId).get();
        product.setMenuSubSection(subSection);

        int sectionId = 1;

        for(MenuSection menuSection : subSection.getMenuSectionList()){
            if(menuSection.getId() != 1 && menuSection.getId() !=2){
                sectionId = menuSection.getId();
            }
        }

        productRepository.save(product);
        menuSubSectionRepository.save(subSection);

        return "redirect:/admin/menusectie/" + sectionId + "/" + subSectionId + "#" + subSectionId;
    }
}
