package at.codersbay.java.taskapp.rest.restapi.response;

import at.codersbay.java.taskapp.rest.entities.AppUser;
import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;

import java.util.Set;

public class UserProfileTaskResponse {

 private User user;
 private Set<Task> tasks;

 private Profile profile;

 private AppUser appUser;

 private String errorMessage;



    public UserProfileTaskResponse(User user, Set<Task> tasks ,Profile profile, AppUser appUser) {
        this.user = user;
        this.tasks = tasks;
        this.profile = profile;
        this.appUser= appUser;

    }
    public UserProfileTaskResponse(String errorMessage) {
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

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
