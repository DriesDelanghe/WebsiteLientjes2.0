package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Reference {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reference_generator")
    @SequenceGenerator(name = "reference_generator", sequenceName = "ref_seq", allocationSize = 1)
    @Id
    private int id;
    private String siteUrl;
    private String siteName;
    @NotBlank
    private String artistName;
    private String artistUrl;
    @NotBlank
    private String productName;

    public Reference() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistUrl() {
        return artistUrl;
    }

    public void setArtistUrl(String artistUrl) {
        this.artistUrl = artistUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
