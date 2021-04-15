package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;

@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "u_seq", allocationSize = 1)
    @Id
    private int id;
    private String username;
    private String password;
    private String role;
}
