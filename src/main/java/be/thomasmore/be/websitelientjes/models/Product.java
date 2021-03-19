package be.thomasmore.be.websitelientjes.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
public class Product {

    @Id
    private int id;
    private String name;
    private double priceInEur;
    private String extraInfo;
    @ManyToMany(mappedBy = "productList")
    private Collection<MenuSubSection> menuSubSectionList;

    public Product() {
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

    public double getPriceInEur() {
        return priceInEur;
    }

    public void setPriceInEur(double priceInEur) {
        this.priceInEur = priceInEur;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Collection<MenuSubSection> getMenuSubSectionList() {
        return menuSubSectionList;
    }

    public void setMenuSubSectionList(Collection<MenuSubSection> menuSubSectionList) {
        this.menuSubSectionList = menuSubSectionList;
    }
}
