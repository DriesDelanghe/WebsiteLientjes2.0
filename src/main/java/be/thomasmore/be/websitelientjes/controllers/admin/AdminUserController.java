package be.thomasmore.be.websitelientjes.controllers.admin;

import be.thomasmore.be.websitelientjes.models.Domain;
import be.thomasmore.be.websitelientjes.models.MenuSection;
import be.thomasmore.be.websitelientjes.repositories.DomainRepository;
import be.thomasmore.be.websitelientjes.repositories.MenuSectionRepository;
import be.thomasmore.be.websitelientjes.repositories.UserRepository;
import be.thomasmore.be.websitelientjes.repositories.UserRoleRepository;
import be.thomasmore.be.websitelientjes.user.User;
import be.thomasmore.be.websitelientjes.user.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class AdminUserController {

    Logger logger = LoggerFactory.getLogger(AdminUserController.class);

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
    public List<User> getUserList() {
        return (List<User>) userRepository.findAll();
    }

    @ModelAttribute("boloRole")
    public UserRole getBoloRole() {
        return userRoleRepository.findById(2).get();
    }

    @ModelAttribute("bistroRole")
    public UserRole getBistroRole() {
        return userRoleRepository.findById(1).get();
    }

    @ModelAttribute("user")
    public User getUser(@PathVariable(required = false) Integer userId) {
        if (userId != null) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            }
        }
        return null;
    }

    @ModelAttribute("roles")
    public List<UserRole> getRoles() {
        return (List<UserRole>) userRoleRepository.findAll();
    }

    @ModelAttribute("roleBistro")
    public UserRole getRoleBistro() {
        return userRoleRepository.findById(1).get();
    }

    @ModelAttribute("roleBolo")
    public UserRole getRoleBolo() {
        return userRoleRepository.findById(2).get();
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser(Principal principal) {
        if (principal != null) {

            if (userRepository.getUserByUsername(principal.getName()) != null) {
                return userRepository.getUserByUsername(principal.getName());
            }
        }
        return null;
    }

    @ModelAttribute("newUser")
    public User getNewUser() {
        return new User();
    }

    @GetMapping("/gebruikers")
    public String gebruikersPage() {
        return "/admin/useroverview";
    }

    @GetMapping({"/gebruiker", "/gebruiker/{userId}"})
    public String userPage() {
        return "admin/userdetail";
    }

    @GetMapping("/profiel")
    public String profilePage() {
        return "admin/profile";
    }

    @GetMapping("/gebruiker/new")
    public String newUserPage() {
        return "admin/newuser";
    }


    @PostMapping("/user/roleupdate/{userId}")
    public String updateRoles(@ModelAttribute("user") User user,
                              @RequestParam(required = false, name = "roleIds[]") List<Integer> roleIds) {

        if (roleIds != null && !roleIds.isEmpty()) {
            List<UserRole> userRoles = (List<UserRole>) userRoleRepository.findAllById(roleIds);
            user.setRole(userRoles);
            userRepository.save(user);
        }
        if (roleIds == null || roleIds.isEmpty()) {
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
                                @RequestParam(required = false) String username,
                                Model model,
                                Principal principal) {

        model.addAttribute("newUsername", username);
        boolean uniqueUserName = true;
        ArrayList<User> allUsers = (ArrayList<User>) userRepository.findAll();
        Iterator<User> it = allUsers.iterator();
        while (it.hasNext() && uniqueUserName) {
            User u = it.next();
            logger.info("checking for: " + u.getUsername());
            if (u.getUsername().equals(username) && u.getId() != user.getId()) {
                uniqueUserName = false;
            }
        }

        if (bindingResult.hasErrors() || (!password1.isBlank() && !password2.isBlank() && !password1.equals(password2)) || !uniqueUserName) {
            if (!password1.equals(password2)) {
                model.addAttribute("passworderror", true);
            }

            if (!uniqueUserName) {
                model.addAttribute("usernameNotUnique", uniqueUserName);
            }

            return "admin/userdetail";
        }


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!password1.isBlank() && !password2.isBlank()) {
            String password = encoder.encode(password1);
            user.setPassword(password);
        }
        user.setUsername(username);
        userRepository.save(user);
        return "redirect:/admin/gebruiker/" + user.getId();
    }

    @PostMapping("/user/remove/{userId}")
    public String removeUser(@ModelAttribute("user") User user) {


        userRepository.delete(user);

        return "redirect:/admin/gebruikers";
    }

    @PostMapping("/user/new")
    public String newUserPost(@ModelAttribute("newUser") User user,
                              @RequestParam(required = false) String password1,
                              @RequestParam(required = false) String password2,
                              @RequestParam(name = "roleIds[]", required = false) List<Integer> roleIds,
                              @RequestParam(required = false) String newUsername,
                              Model model) {

        model.addAttribute("newUsername", newUsername);

        boolean uniqueUserName = true;
        ArrayList<User> allUsers = (ArrayList<User>) userRepository.findAll();
        Iterator<User> it = allUsers.iterator();
        while (it.hasNext() && uniqueUserName) {
            User u = it.next();
            logger.info("checking for: " + u.getUsername());
            if (u.getUsername().equals(newUsername)) {
                uniqueUserName = false;
            }
        }

        if ((!password1.isBlank() && !password2.isBlank() && !password1.equals(password2))
                || password1.isBlank() || password2.isBlank() || roleIds == null || roleIds.isEmpty() || !uniqueUserName || newUsername.isBlank()) {

            if (!password1.equals(password2) || password1.isBlank() || password2.isBlank()) {
                model.addAttribute("passworderror", true);
                logger.info("passwordError");
            }
            if (roleIds == null || roleIds.isEmpty()) {
                model.addAttribute("noRoles", true);
                logger.info("roleError");
            }

            if (!uniqueUserName) {
                model.addAttribute("usernameNotUnique", uniqueUserName);
                logger.info("usernameError");
            }

            if (newUsername.isBlank()) {
                model.addAttribute("blankName", true);
            }

            logger.info("unspecified error");
            return "admin/newuser";
        }

        if (!roleIds.isEmpty()) {
            List<UserRole> userRoles = (List<UserRole>) userRoleRepository.findAllById(roleIds);
            user.setRole(userRoles);
        }
        if (roleIds.isEmpty()) {
            user.setRole(null);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = encoder.encode(password1);
        user.setPassword(password);
        user.setUsername(newUsername);
        userRepository.save(user);
        return "redirect:/admin/gebruiker/" + user.getId();

    }
}
