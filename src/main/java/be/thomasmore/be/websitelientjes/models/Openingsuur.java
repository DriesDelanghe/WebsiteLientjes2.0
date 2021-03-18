package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Openingsuur {

    @Id
    private int id;
    private String dag;
    @Temporal(TemporalType.TIME)
    private Date openingsuur;
    @Temporal(TemporalType.TIME)
    private Date sluitingsuur;
    @ManyToOne
    private Domain domain;

    public Openingsuur() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDag() {
        return dag;
    }

    public void setDag(String dag) {
        this.dag = dag;
    }

    public Date getOpeningsuur() {
        return openingsuur;
    }

    public void setOpeningsuur(Date openingsuur) {
        this.openingsuur = openingsuur;
    }

    public Date getSluitingsuur() {
        return sluitingsuur;
    }

    public void setSluitingsuur(Date sluitingsuur) {
        this.sluitingsuur = sluitingsuur;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }
}
