package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.ImageRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.ProductRepository;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
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
        try {
            Optional<MenuSection> menuSectionOptional = menuSectionRepository.findById(menuSectionId);
            if (menuSectionOptional.isPresent()) {
                return menuSectionOptional.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ModelAttribute("menuImages")
    public List<Image> getMenuImages() {
        return imageRepository.getAllMenuImages();
    }

    @ModelAttribute("product")
    public Product getProduct(@PathVariable(required = false) Integer productId){
        if(productId != null){
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if(optionalProduct.isPresent()){
                return optionalProduct.get();
            }
        }
        return null;
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
                                  @Valid @ModelAttribute("product")Product product,
                                  BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "redirect:/admin/menusectie/" + menuSectionId;
        }

        productRepository.save(product);

        return "redirect:/admin/menusectie/" + menuSectionId;
    }

}
