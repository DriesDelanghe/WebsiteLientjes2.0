package be.thomasmore.be.websitelientjes.models;

import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class SocialMedia implements Comparable<SocialMedia> {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "social_media_generator")
    @SequenceGenerator(name = "social_media_generator", sequenceName = "sm_seq", allocationSize = 1)
    @Id
    private Integer id;
    @NotBlank
    @URL
    private String socialMediaUrl;
    @ManyToOne
    private Icon icon;
    private Integer orderReference;
    @ManyToOne(fetch = FetchType.LAZY)
    private Domain domain;

    public SocialMedia() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSocialMediaUrl() {
        return socialMediaUrl;
    }

    public void setSocialMediaUrl(String socialMediaUrl) {
        this.socialMediaUrl = socialMediaUrl;
    }

    public Integer getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(Integer orderReference) {
        this.orderReference = orderReference;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    @Override
    public int compareTo(SocialMedia sm) {
        return this.getOrderReference().compareTo(sm.getOrderReference());
    }
}
