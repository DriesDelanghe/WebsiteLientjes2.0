package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Entity
public class Product {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
    @SequenceGenerator(name = "product_generator", sequenceName = "prod_seq", allocationSize = 1)
    @Id
    private int id;
    @NotBlank
    private String name;
    @NotNull
    @Column(precision=10, scale=2)
    private BigDecimal priceInEur;
    private String extraInfo;
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuSubSection menuSubSection;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Allergie> allergies;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<ProductCategory> categories;

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

    public BigDecimal getPriceInEur() {
        return priceInEur;
    }

    public void setPriceInEur(BigDecimal priceInEur) {
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

    public List<Allergie> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<Allergie> allergies) {
        this.allergies = allergies;
    }

    public List<ProductCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ProductCategory> categories) {
        this.categories = categories;
    }
}
