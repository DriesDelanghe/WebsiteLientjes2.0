package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;

@Entity
public class ContactType {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_type_generator")
    @SequenceGenerator(name = "contact_type_generator", sequenceName = "ct_seq", allocationSize = 1)
    @Id
    private Integer id;
    private String questionType;
    @ManyToOne
    private Domain domain;

    public ContactType() {
    }

    public ContactType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }
}
