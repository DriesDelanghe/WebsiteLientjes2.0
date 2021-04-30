package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;

@Entity
public class TelephoneNumber {

    @Id
    private int id;
    private String telephoneNumber;
    @ManyToOne
    private Icon icon;
    @OneToOne(fetch = FetchType.LAZY)
    private Domain domain;

    public TelephoneNumber() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
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
}
