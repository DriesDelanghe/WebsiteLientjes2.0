package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Page {

    @Id
    private Integer id;
    private String pageName;
    private String pageReference;
    @OneToMany(fetch = FetchType.LAZY)
    private Collection<TextFragment> textFragments;
    @ManyToMany(mappedBy = "pages", fetch = FetchType.LAZY)
    private Collection<Personnel> personnel;
    @ManyToOne(fetch = FetchType.LAZY)
    private Domain domain;
    @ManyToOne(fetch = FetchType.LAZY)
    private Image image;

    public Page() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public Collection<TextFragment> getTextFragments() {
        return textFragments;
    }

    public void setTextFragments(Collection<TextFragment> textFragments) {
        this.textFragments = textFragments;
    }

    public Collection<Personnel> getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Collection<Personnel> personnel) {
        this.personnel = personnel;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getPageReference() {
        return pageReference;
    }

    public void setPageReference(String pageReference) {
        this.pageReference = pageReference;
    }
}
