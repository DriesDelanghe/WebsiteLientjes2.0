package be.thomasmore.be.websitelientjes.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Collection;

@Entity
public class Product {

    @Id
    private int id;
    private String name;
    private Double priceInEur;
    private String extraInfo;
    @ManyToOne
    private MenuSubSection menuSubSection;
    @ManyToMany
    private Collection<Allergie> allergies;
    @ManyToMany
    private Collection<ProductCategory> categories;

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

    public Double getPriceInEur() {
        return priceInEur;
    }

    public void setPriceInEur(Double priceInEur) {
        this.priceInEur = priceInEur;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public MenuSubSection getMenuSubSection() {
        return menuSubSection;
    }

    public void setMenuSubSection(MenuSubSection menuSubSection) {
        this.menuSubSection = menuSubSection;
    }

    public Collection<Allergie> getAllergies() {
        return allergies;
    }

    public void setAllergies(Collection<Allergie> allergies) {
        this.allergies = allergies;
    }

    public Collection<ProductCategory> getCategories() {
        return categories;
    }

    public void setCategories(Collection<ProductCategory> categories) {
        this.categories = categories;
    }
}
