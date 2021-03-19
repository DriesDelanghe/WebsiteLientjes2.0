package be.thomasmore.be.websitelientjes.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;
import java.util.List;

@Entity
public class MenuSubSection {

    @Id
    private int id;
    private String name;
    private String extraInfo;
    @ManyToMany
    private List<Product> productList;
    @ManyToMany(mappedBy = "menuSubSectionList")
    private List<MenuSection> menuSectionList;


    public MenuSubSection() {
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

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<MenuSection> getMenuSectionList() {
        return menuSectionList;
    }

    public void setMenuSectionList(List<MenuSection> menuSectionList) {
        this.menuSectionList = menuSectionList;
    }
}
