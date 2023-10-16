package at.codersbay.java.taskapp.rest.restapi.response;

import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;

import java.util.List;
import java.util.Set;

public class UserTaskResponse {

 private User user;
 private Set<Task> tasks;


 private String errorMessage;


    public UserTaskResponse(User user, Set<Task> tasks ) {
        this.user = user;
        this.tasks = tasks;

    }
    public UserTaskResponse(String errorMessage) {
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



}
