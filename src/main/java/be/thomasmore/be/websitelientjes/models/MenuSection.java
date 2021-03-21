package be.thomasmore.be.websitelientjes.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class MenuSection {

    @Id
    private int id;
    private String name;
    @ManyToOne
    private Domain domain;
    @ManyToMany
    private List<MenuSubSection> menuSubSectionList;

    public MenuSection() {
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
}