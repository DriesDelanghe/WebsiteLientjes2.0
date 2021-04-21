package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;

@Entity
public class Icon {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "icon_generator")
    @SequenceGenerator(name = "icon_generator", sequenceName = "ic_seq", allocationSize = 1)
    @Id
    private Integer id;
    private String iconCode;
    private String referenceName;

    public Icon() {
    }

    public Icon(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }
}
