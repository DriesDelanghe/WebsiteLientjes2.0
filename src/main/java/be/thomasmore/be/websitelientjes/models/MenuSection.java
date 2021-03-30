package be.thomasmore.be.websitelientjes.models;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@DynamicInsert
@Entity
public class MenuSection {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_section_generator")
    @SequenceGenerator(name = "menu_section_generator", sequenceName = "msec_seq", allocationSize = 1)
    @Id
    private int id;
    @NotBlank
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Domain domain;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<MenuSubSection> menuSubSectionList;
    @ManyToOne(fetch = FetchType.LAZY)
    private Image image;

    public MenuSection() {
    }

    public MenuSection(int id) {
        this.id = id;
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

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public List<MenuSubSection> getMenuSubSectionList() {
        return menuSubSectionList;
    }

    public void setMenuSubSectionList(List<MenuSubSection> menuSubSectionList) {
        this.menuSubSectionList = menuSubSectionList;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
