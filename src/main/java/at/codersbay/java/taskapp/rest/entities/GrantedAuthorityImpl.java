package at.codersbay.java.taskapp.rest.entities;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "authorities")
public class GrantedAuthorityImpl implements GrantedAuthority {

    @Id()
    @GeneratedValue(generator = "authoritiesSequence")
    @GenericGenerator(
            name = "authoritiesSequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "authorities_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    private String authority;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "authorities_users",
            joinColumns = @JoinColumn(name = "authorityId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<AppUser> appUsers;

    public GrantedAuthorityImpl() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<AppUser> getAppUsers() {
        return appUsers;
    }

    public void setAppUsers(Set<AppUser> appUsers) {
        this.appUsers = appUsers;
    }
}
