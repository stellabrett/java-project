package at.codersbay.java.taskapp.rest.restapi;

import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;

import java.util.List;
import java.util.Set;

public class UserTaskResponse {

 private User user;
 private Set<Task> tasks;

 private Profile profile;

    public UserTaskResponse(User user, Set<Task> tasks, Profile profile) {
        this.user = user;
        this.tasks = tasks;
        this.profile = profile;
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
