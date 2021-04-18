package be.thomasmore.be.websitelientjes.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "u_seq", allocationSize = 1)
    @Id
    private int id;
    private String username;
    private String password;
    private String role;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<UserRole> userRole;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserRole> getRole() {
        return userRole;
    }

    public void setRole(List<UserRole> userRole) {
        this.userRole = userRole;
    }
}
