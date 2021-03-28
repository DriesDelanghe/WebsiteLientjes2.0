package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Image {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_generator")
    @SequenceGenerator(name = "image_generator", sequenceName = "img_seq", allocationSize = 1)
    @Id
    private int id;
    @NotBlank
    private String ImageLocation;

    public Image() {
    }

    public Image(String imageLocation) {
        ImageLocation = imageLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageLocation() {
        return ImageLocation;
    }

    public void setImageLocation(String imageLocation) {
        ImageLocation = imageLocation;
    }
}
