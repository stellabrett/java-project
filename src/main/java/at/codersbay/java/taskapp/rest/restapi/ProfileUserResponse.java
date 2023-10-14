package at.codersbay.java.taskapp.rest.restapi;

import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;

import java.util.List;
import java.util.Set;

public class ProfileUserResponse {

 private User user;
 private Profile profile;
 private String errorMessage;


    public ProfileUserResponse(User user,  Profile profile) {
        this.user = user;
        this.profile = profile;
    }

    public ProfileUserResponse(String errorMessage) {
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
