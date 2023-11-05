package at.codersbay.java.taskapp.rest.entities;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;


public class AppUser  {

    @Id
    @GeneratedValue(generator = "appUsersSequence")
    @GenericGenerator(
            name = "appUsersSequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "appUsers_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "90000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    private Long id;

    private String username;
    private String password;
    @ManyToMany(mappedBy = "appUsers")
    Set<GrantedAuthorityImpl> authorities;


    public Set<GrantedAuthorityImpl> getAuthorities() {
        return this.authorities;
    }


    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthorities(Set<GrantedAuthorityImpl> authorities) {
        this.authorities = authorities;
    }
}
