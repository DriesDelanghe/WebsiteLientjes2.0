package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;

@Entity
public class ImageGoogle implements Comparable<ImageGoogle> {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_google_generator")
    @SequenceGenerator(name = "image_google_generator", sequenceName = "img_ggl_seq", allocationSize = 1)
    @Id
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Image image;
    @ManyToOne(fetch = FetchType.LAZY)
    private Domain domain;
    private Integer orderNr;

    public ImageGoogle() {
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

    public void setImage(Image image) {
        this.image = image;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Integer getOrderNr() {
        return orderNr;
    }

    public void setOrderNr(Integer orderNr) {
        this.orderNr = orderNr;
    }

    @Override
    public int compareTo(ImageGoogle t) {
        return this.getOrderNr().compareTo(t.getOrderNr());
    }
}
