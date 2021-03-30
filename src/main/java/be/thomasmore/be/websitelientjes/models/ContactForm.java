package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
public class ContactForm implements Comparable<ContactForm>{

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_form_generator")
    @SequenceGenerator(name = "contact_form_generator", sequenceName = "cf_seq", allocationSize = 1)
    @Id
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String onderwerp;
    @ManyToOne(fetch = FetchType.LAZY)
    private ContactType contactType;
    @Column(length = 500)
    @NotBlank
    private String question;
    private Date timestamp;

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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getOnderwerp() {
        return onderwerp;
    }

    public void setOnderwerp(String onderwerp) {
        this.onderwerp = onderwerp;
    }

    @Override
    public int compareTo(ContactForm c){
        return c.getTimestamp().compareTo(this.getTimestamp());
    }

}
