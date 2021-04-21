package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class ContactInfo {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_info_generator")
    @SequenceGenerator(name = "contact_info_generator", sequenceName = "ci_seq", allocationSize = 1)
    @Id
    private int id;
    @NotBlank
    private String infoName;
    @NotBlank
    private String infoContent;
    @ManyToOne(fetch = FetchType.LAZY)
    private Domain domain;
    @ManyToOne
    private Icon icon;

    public ContactInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
