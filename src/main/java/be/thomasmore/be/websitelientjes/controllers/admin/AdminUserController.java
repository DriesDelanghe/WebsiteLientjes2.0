package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.UserRepository;
import be.thomasmore.be.websitelientjes.repositories.UserRoleRepository;
import be.thomasmore.be.websitelientjes.user.User;
import be.thomasmore.be.websitelientjes.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class AdminUserController {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    MenuSectionRepository menuSectionRepository;
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


    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;

    @ModelAttribute("userList")
    public List<User> getUserList(){
        return (List<User>) userRepository.findAll();
    }

    @ModelAttribute("boloRole")
    public UserRole getBoloRole(){
        return userRoleRepository.findById(2).get();
    }

    @ModelAttribute("bistroRole")
    public UserRole getBistroRole(){
        return userRoleRepository.findById(1).get();
    }

    @ModelAttribute("user")
    public User getUser(@PathVariable(required = false) Integer userId){
        if(userId != null){
            Optional<User> optionalUser = userRepository.findById(userId);
            if(optionalUser.isPresent()){
                return optionalUser.get();
            }
        }
        return null;
    }

    @ModelAttribute("roles")
    public List<UserRole> getRoles(){
        return (List<UserRole>) userRoleRepository.findAll();
    }

    @ModelAttribute("roleBistro")
    public UserRole getRoleBistro(){
        return userRoleRepository.findById(1).get();
    }

    @ModelAttribute("roleBolo")
    public UserRole getRoleBolo(){
        return userRoleRepository.findById(2).get();
    }

    @GetMapping("/gebruikers")
    public String gebruikersPage(){
        return "/admin/useroverview";
    }

    @GetMapping({"/gebruiker", "/gebruiker/{userId}"})
    public String userPage(){
        return "admin/userdetail";
    }

    @PostMapping("/user/roleupdate/{userId}")
    public String updateRoles(@ModelAttribute("user") User user,
                              @RequestParam(required = false, name = "roleIds[]") List<Integer> roleIds){

        if(roleIds != null && !roleIds.isEmpty()) {
            List<UserRole> userRoles = (List<UserRole>) userRoleRepository.findAllById(roleIds);
            user.setRole(userRoles);
            userRepository.save(user);
        }
        if(roleIds == null || roleIds.isEmpty()){
            user.setRole(null);
            userRepository.save(user);
        }

        return "redirect:/admin/gebruiker/" + user.getId();
    }

    @PostMapping("/user/updatedetails/{userId}")
    public String updateDetails(@Valid @ModelAttribute("user") User user,
                                BindingResult bindingResult,
                                @RequestParam(required = false) String password1,
                                @RequestParam(required = false) String password2,
                                Model model){

        if(bindingResult.hasErrors() || !password1.equals(password2)){
            if(!password1.equals(password2)){
                model.addAttribute("passworderror", true);
            }
            return "admin/userdetail";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = encoder.encode(password1);

        user.setPassword(password);
        userRepository.save(user);
        return "redirect:/admin/gebruiker/" + user.getId();
    }
}
