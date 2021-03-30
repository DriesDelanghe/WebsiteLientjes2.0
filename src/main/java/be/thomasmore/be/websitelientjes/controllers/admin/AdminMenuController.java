package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.*;
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
import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class AdminMenuController{

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
        return domainRepository.getByDomainName("bistro");
    }

    @ModelAttribute("domainBolo")
    public Domain getDomainBolo() {
        return domainRepository.getByDomainName("bolo");
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
        if(menuSectionId != null) {
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
    public List<Allergie> getAllergieList(){
        return (List<Allergie>) allergieRepository.findAll();
    }

    @ModelAttribute("categoryList")
    public List<ProductCategory> categoryList(){
        return (List<ProductCategory>) categoryRepository.findAll();
    }
    @ModelAttribute("newProduct")
    public Product newProduct(){
        return new Product();
    }

    @ModelAttribute("newSubSection")
    public MenuSubSection newSubsection(){
        return new MenuSubSection();
    }

    @ModelAttribute("newSection")
    public MenuSection newSection(){
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

        return "admin/menulijst";
    }

    @GetMapping({"/menusectie", "/menusectie/{menuSectionId}"})
    public String menuSectie(Model model, @PathVariable(required = false) Integer menuSectionId) {
        model.addAttribute("changesSaved", false);
        return "admin/menusectie";
    }

    @PostMapping("/menusectie/{menuSectionId}")
    public String menuSectiePost(@PathVariable Integer menuSectionId,
                                 Model model,
                                 @Valid @ModelAttribute("menuSection") MenuSection menuSection,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/admin/menusectie/" + menuSectionId;
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
                               Model model) throws Exception{
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
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/admin/menusectie/" + menuSectionId;
        }

        menuSectionRepository.save(menuSection);
        model.addAttribute("changesSaved", true);
        return "redirect:/admin/menusectie/" + menuSectionId;
    }

    @PostMapping("/editproduct/{menuSectionId}/{productId}")
    public String editProductPost(@ModelAttribute("menuSection") MenuSection menuSection,
                                  @PathVariable Integer productId,
                                  @PathVariable Integer menuSectionId,
                                  @RequestParam String productName,
                                  @RequestParam BigDecimal productPrice,
                                  @RequestParam String productExtraInfo,
                                  @RequestParam(name = "category[]", required = false) List<Integer> categoryIdList,
                                  @RequestParam(name = "allergy[]", required = false) List<Integer> allergyIdList){

        Product product = productRepository.findById(productId).get();

        if(product.getName() != productName){
            product.setName(productName);
        }
        if(product.getPriceInEur() != productPrice){
            product.setPriceInEur(productPrice);
        }
        if(product.getExtraInfo() != productExtraInfo){
            product.setExtraInfo(productExtraInfo);
        }

        if(allergyIdList != null) {
            List<Allergie> allergieList = (List<Allergie>) allergieRepository.findAllById(allergyIdList);
            allergieList.forEach(allergie -> logger.info(String.format("Category -- %s", allergie.getName())));
            product.setAllergies(allergieList);
        }
        if(allergyIdList == null){
            product.getAllergies().removeAll(product.getAllergies());
        }
        if(categoryIdList != null) {
            List<ProductCategory> categoryList = (List<ProductCategory>) categoryRepository.findAllById(categoryIdList);
            categoryList.forEach(category -> logger.info(String.format("Category -- %s", category.getName())));
            product.setCategories(categoryList);
        }
        if(categoryIdList == null){
            product.getCategories().removeAll(product.getCategories());
        }
        productRepository.save(product);

        return "redirect:/admin/menusectie/" + menuSectionId;
    }

    @PostMapping("/menu/subsectionnamechange/{menuSectionId}/{subSectionId}")
    public String editSubSection(@PathVariable Integer menuSectionId,
                                 @PathVariable Integer subSectionId,
                                 @RequestParam String subSectionName){

        MenuSubSection menuSubSection = menuSubSectionRepository.findById(subSectionId).get();

        if(menuSubSection.getName() != subSectionName){
            menuSubSection.setName(subSectionName);
        }

        menuSubSectionRepository.save(menuSubSection);

        return "redirect:/admin/menusectie/" + menuSectionId;
    }

    @PostMapping("/newproduct/{menuSectionId}/{subSectionId}")
    public String newMenuProduct(@Valid @ModelAttribute("newProduct") Product product,
                                 BindingResult bindingResult,
                                 @PathVariable Integer menuSectionId,
                                 @PathVariable Integer subSectionId){

        if(bindingResult.hasErrors()){
            return "redirect:/admin/menusectie/" + menuSectionId;
        }

        MenuSubSection menuSubSection = new MenuSubSection(subSectionId);

        product.setMenuSubSection(menuSubSection);

        productRepository.save(product);

        return "redirect:/admin/menusectie/" + menuSectionId;
    }

    @PostMapping("/newsubsection/{menuSectionId}")
    public String newMenuSubSection(@Valid @ModelAttribute("newSubSection") MenuSubSection menuSubSection,
                                    BindingResult bindingResult,
                                    @PathVariable Integer menuSectionId,
                                    @RequestParam String newSubSectionName){
        logger.info("---- start adding new subsection ----");
        if(bindingResult.hasErrors() || newSubSectionName == null || newSubSectionName.isBlank()){
            return "redirect:/admin/menusectie/" + menuSectionId;
        }

        menuSubSection.setName(newSubSectionName);
        MenuSection section = menuSectionRepository.findById(menuSectionId).get();
        MenuSection allproductsSection = menuSectionRepository.findById(8).get();
        menuSubSectionRepository.save(menuSubSection);

        section.getMenuSubSectionList().add(menuSubSection);
        allproductsSection.getMenuSubSectionList().add(menuSubSection);

        menuSectionRepository.save(section);
        menuSectionRepository.save(allproductsSection);

        logger.info("---- end adding new subsection ----");
        return "redirect:/admin/menusectie/" + menuSectionId;
    }

    @PostMapping("/newsectionondomain/{domainId}")
    public String newSectionOnDomain( @PathVariable Integer domainId,
                                      @ModelAttribute("newSection") MenuSection menuSection,
                                      BindingResult bindingResult,
                                      Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("newSection", menuSection);
            return "redirect:/admin/menulijst" + domainId;
        }

        menuSection.setDomain(new Domain(domainId));
        menuSectionRepository.save(menuSection);

        return "redirect:/admin/menulijst/" + domainId;
    }

    @PostMapping("/newsection/{domainId}")
    public String newSectionNoDomain(@PathVariable Integer domainId,
                                     @ModelAttribute("newSection") MenuSection menuSection,
                                     BindingResult bindingResult,
                                     Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("newSection", menuSection);
            return "redirect:/admin/menulijst/";
        }

        menuSection.setDomain(new Domain(domainId));
        menuSectionRepository.save(menuSection);

        return "redirect:/admin/menulijst";
    }
}
