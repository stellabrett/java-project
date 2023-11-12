package at.codersbay.java.taskapp.rest.restapi.response;

import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;

import java.util.Set;

public class UserProfileTaskUserResponse {

 private User user;
 private Set<Task> tasks;

 private Profile profile;

 //private AppUser appUser;

 private String errorMessage;
 private String username;



    public UserProfileTaskUserResponse(User user, Set<Task> tasks , Profile profile, String username) {
        this.user = user;
        this.tasks = tasks;
        this.profile = profile;
        //this.appUser= appUser;
        this.username = username;

    }
    public UserProfileTaskUserResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    //public AppUser getAppUser() {return appUser;}

    //public void setAppUser(AppUser appUser) {this.appUser = appUser;}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
