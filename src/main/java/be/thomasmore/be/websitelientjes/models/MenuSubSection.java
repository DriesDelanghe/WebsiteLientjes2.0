package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
public class MenuSubSection {

    @Id
    private int id;
    private String name;
    private String extraInfo;

    @ManyToMany(mappedBy = "menuSubSectionList", fetch = FetchType.LAZY)
    private List<MenuSection> menuSectionList;
    @OneToMany(mappedBy = "menuSubSection", fetch = FetchType.LAZY)
    private List<Product> productList;


    public MenuSubSection() {
    }

    public MenuSubSection(int id) {
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

    public List<MenuSection> getMenuSectionList() {
        return menuSectionList;
    }

    public void setMenuSectionList(List<MenuSection> menuSectionList) {
        this.menuSectionList = menuSectionList;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public List<Product> getProducts() {
        return productList;
    }

    public void setProducts(List<Product> productList) {
        this.productList = productList;
    }
}
