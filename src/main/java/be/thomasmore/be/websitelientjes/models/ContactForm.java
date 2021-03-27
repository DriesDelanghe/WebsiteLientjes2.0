package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class ContactForm {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_form_generator")
    @SequenceGenerator(name = "contact_form_generator", sequenceName = "cf_seq", allocationSize = 1)
    @Id
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @ManyToOne
    @NotNull
    private ContactType contactType;
    @Column(length = 500)
    @NotBlank
    private String question;

    public ContactForm() {
    }

    public ContactForm(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
