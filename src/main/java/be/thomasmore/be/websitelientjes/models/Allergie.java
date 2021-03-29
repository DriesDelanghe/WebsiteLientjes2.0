package be.thomasmore.be.websitelientjes.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Allergie {

    @Id
    private int id;
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