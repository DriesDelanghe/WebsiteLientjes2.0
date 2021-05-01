package be.thomasmore.be.websitelientjes.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Openingsuur {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "openingsuur_generator")
    @SequenceGenerator(name = "openingsuur_generator", sequenceName = "op_seq", allocationSize = 1)
    @Id
    private Integer id;
    private String dag;
    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private Date openingsuur;
    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private Date sluitingsuur;
    @ManyToOne(fetch = FetchType.LAZY)
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
