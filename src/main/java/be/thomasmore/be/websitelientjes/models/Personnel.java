package be.thomasmore.be.websitelientjes.models;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
public class Personnel {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personnel_generator")
    @SequenceGenerator(name = "personnel_generator", sequenceName = "per_seq", allocationSize = 1)
    @Id
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String functionDescription;
    @NotBlank
    private String extraInfo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Image image;
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Page> pages;
    @ManyToOne(fetch = FetchType.LAZY)
    private Domain domain;


    public Personnel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Collection<Page> getPages() {
        return pages;
    }

    public void setPages(Collection<Page> pages) {
        this.pages = pages;
    }

    public String getFunctionDescription() {
        return functionDescription;
    }

    public void setFunctionDescription(String functionDescription) {
        this.functionDescription = functionDescription;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }
}
