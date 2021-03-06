package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Allergie {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allergy_generator")
    @SequenceGenerator(name = "allergy_generator", sequenceName = "al_seq", allocationSize = 1)
    @Id
    private Integer id;
    @NotBlank
    private String name;
    @ManyToMany(mappedBy = "allergies", fetch = FetchType.LAZY)
    private List<Product> products;

    public Allergie() {
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


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}