package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class RedirectEmail {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "redirect_email_generator")
    @SequenceGenerator(name = "redirect_email_generator", sequenceName = "re_seq", allocationSize = 1)
    @Id
    private Integer Id;
    @Email
    private String email;
    @NotBlank
    private String name;
    @ManyToMany
    private List<ContactType> contactTypeList;

    public RedirectEmail() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ContactType> getContactTypeList() {
        return contactTypeList;
    }

    public void setContactTypeList(List<ContactType> contactTypeList) {
        this.contactTypeList = contactTypeList;
    }
}
