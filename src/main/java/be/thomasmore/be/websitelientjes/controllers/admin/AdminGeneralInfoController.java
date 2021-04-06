package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.*;
import be.thomasmore.be.websitelientjes.repositories.ContactInfoRepository;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.ImageRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
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
import java.io.FileWriter;
import java.util.*;

@RequestMapping("/admin")
@Controller
public class AdminGeneralInfoController {

    @Autowired
    ContactInfoRepository contactInfoRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
    @Autowired
    ImageRepository imageRepository;
    @Value("${upload.images.dir}")
    private String uploadImagesDirString;

    Logger logger = LoggerFactory.getLogger(AdminGeneralInfoController.class);

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

    @ModelAttribute("contactInfoListBistro")
    public List<ContactInfo> contactInfoListBistro(@ModelAttribute("domainBistro") Domain domain) {
        return contactInfoRepository.getByDomain(domain);
    }

    @ModelAttribute("contactInfoListBolo")
    public List<ContactInfo> contactInfoListBolo(@ModelAttribute("domainBolo") Domain domain) {
        return contactInfoRepository.getByDomain(domain);
    }

    @ModelAttribute("domain")
    public Domain getDomain(@PathVariable(required = false) Integer domainId) {
        if (domainId != null) {
            Optional<Domain> optionalDomain = domainRepository.findById(domainId);
            if (optionalDomain.isPresent()) {
                return optionalDomain.get();
            }
        }
        return null;
    }

    @ModelAttribute("newContactInfo")
    public ContactInfo getNewContactInfo() {
        return new ContactInfo();
    }

    @ModelAttribute("contactInfoImageList")
    public List<Image> getContactInfoImageList(){
        return imageRepository.getAllContactInfoImages();
    }

    @ModelAttribute("contactInfo")
    public ContactInfo getContactInfo(@PathVariable(required = false) Integer contactInfoId){
        if(contactInfoId != null){
            Optional<ContactInfo> optionalContactInfo = contactInfoRepository.findById(contactInfoId);
            if(optionalContactInfo.isPresent()){
                return optionalContactInfo.get();
            }
        }
        return null;
    }

    @GetMapping({"/contactinfolijst", "/contactinfolijst/{domainId}"})
    public String algemeneInfoLijstGet(@PathVariable(required = false) Integer domainId) {

        return "admin/contactinfolist";
    }

    @GetMapping("/contactinfo/{contactInfoId}")
    public String contactInfoGet(@PathVariable(required = false) Integer contactInfoId){

        return "admin/contactinfo";
    }

    @PostMapping("/newcontactinfo")
    public String newContactInfoPost(@Valid @ModelAttribute("newContactInfo") ContactInfo contactInfo,
                                     BindingResult bindingResult,
                                     @RequestParam Integer domainId,
                                     @ModelAttribute("contactInfoImageList") List<Image> contactInfoImageList){
        if(bindingResult.hasErrors()){
            return "admin/contactinfolist";
        }

        contactInfo.setDomain(new Domain(domainId));
        contactInfo.setImage(contactInfoImageList.get(0));
        contactInfoRepository.save(contactInfo);

        return "redirect:/admin/algemeneinfolijst";
    }

    @PostMapping("/contactinfo/imagechange/{contactInfoId}")
    public String changeImageContactInfo(@ModelAttribute("contactInfo") ContactInfo contactInfo,
                                         @RequestParam Integer imageId,
                                         @PathVariable Integer contactInfoId) {
        if (imageId != null && contactInfo.getImage().getId() != imageId) {
            contactInfo.setImage(new Image(imageId));
        }

        contactInfoRepository.save(contactInfo);

        return "redirect:/admin/socialmedia/" + contactInfoId;
    }

    @PostMapping("/contactinfo/newimage/{contactInfoId}")
    public String newImagePersonnel(@ModelAttribute("contactInfo") ContactInfo contactInfo,
                                    @PathVariable Integer contactInfoId,
                                    @NotNull @RequestParam MultipartFile newImage,
                                    Model model) throws Exception {

        try {
            String newImageName = newImage.getOriginalFilename();
            String path = uploadImagesDirString + "/images/contactinfo";
            logger.info(newImageName);

            String[] nameparts = newImageName.split("\\.");

            logger.info(nameparts[nameparts.length - 1]);

            if (!nameparts[nameparts.length - 1].toLowerCase(Locale.ROOT).equals("svg")) {
                model.addAttribute("falseFileFormat", true);
                return "admin/contactinfo";
            }

            if (!newImageName.isEmpty()) {
                File imageFileDir = new File(path);
                if (!imageFileDir.exists()) imageFileDir.mkdirs();
                File imageFile = new File(path, newImageName);
                int i = 1;
                while(imageFile.exists()){
                    newImageName = newImageName.replaceAll("\\.svg", "").replaceAll("[\\d]+", "").replaceAll("\\(", "").replaceAll("\\)", "");
                    newImageName += "" + i;
                    newImageName += ".svg";
                    imageFile = new File(path, newImageName);
                    i++;
                }
                newImage.transferTo(imageFile);
                logger.info(imageFile.getPath());

                if (imageFile.canRead() && imageFile.canWrite()) {
                    ArrayList<String> dataParts = new ArrayList<>();
                    Scanner myScanner = new Scanner(imageFile);
                    while (myScanner.hasNext()) {
                        String data = myScanner.next();
                        dataParts.addAll(Arrays.asList(data.split("\\s")));
                    }

                    String newData = "";
                    i = 0;
                    for (String s : dataParts) {
                        if (s.contains("fill")) {
                            s = "";
                        }

                        if (s.equals("<svg")) {
                            s += " fill=\"#eee\"";
                        }
                        logger.info(String.format("datapart %d -- %s", i, s));
                        i++;
                        if (!s.isBlank()) {
                            newData += s + " ";
                        }
                    }
                    logger.info(newData);
                    FileWriter myWriter = new FileWriter(imageFile.getAbsolutePath(), false);
                    myWriter.append(newData);
                    myWriter.close();
                }

                Optional<Image> imageOptional = imageRepository.getByImageLocation(imageFile.getPath());
                if (!imageOptional.isPresent()) {
                    Image image = new Image("/images/contactinfo/" + newImageName);
                    contactInfo.setImage(image);
                    imageRepository.save(image);
                } else {
                    contactInfo.setImage(imageOptional.get());
                }

            }
        } catch (FileSizeLimitExceededException fileSizeLimitExceededException) {
            model.addAttribute("FileSizeException", true);
            return "admin/contactinfo";
        } catch (Exception e) {
            e.printStackTrace();
            return "admin/contactinfo";
        }

        contactInfoRepository.save(contactInfo);
        return "redirect:/admin/contactinfo/" + contactInfoId;
    }

    @PostMapping("/changecontactinfo/{contactInfoId}")
    public String changeContactInfoPost(@PathVariable Integer contactInfoId,
                                        @Valid @ModelAttribute("contactInfo") ContactInfo contactInfo,
                                        BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "admin/contactinfo";
        }

        contactInfoRepository.save(contactInfo);
        return "redirect:/admin/contactinfolijst";

    }

    @PostMapping("/removecontactinfo/{contactInfoId}")
    public String removeContactInfoPost(@ModelAttribute("contactInfo") ContactInfo contactInfo,
                                        @PathVariable Integer contactInfoId){
        contactInfoRepository.delete(contactInfo);
        return "redirect:/admin/contactinfolijst";
    }
}
