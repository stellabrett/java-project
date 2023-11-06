package at.codersbay.java.taskapp.rest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;



@Table(name = "users")

@Entity
public class User {
    @Id
    @GeneratedValue(generator = "userSequence")
    @GenericGenerator(
            name = "userSequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    private Long id;
    private String firstname;
    private String lastname;


    private String email;

    @JsonIgnore
    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_tasks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
   @JsonIgnore
   // @JsonView(Views.WithTasks.class)
    Set<Task> tasks;


    public User() {
    }

    public User(Long id, String firstname, String lastname, String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

}








