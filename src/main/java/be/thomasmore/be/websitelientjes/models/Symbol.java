package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Symbol {

    @Id
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    private Image image;
    private String referenceName;
    @ManyToMany(fetch = FetchType.LAZY)
    Collection<Page> pages;

    public Symbol() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
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
}
