package at.codersbay.java.taskapp.rest.restapi.response;

import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;

import java.util.Set;

public class TaskUserResponse {
    private Task task;
 private Set<User> user;



 private String errorMessage;


    public TaskUserResponse(Task task, Set<User> user ,String errorMessage) {
        this.user = user;
        this.task = task;
        this.errorMessage = errorMessage;
    }

    public TaskUserResponse(){

    }
    public Set<User> getUser() {
        return user;
    }

    public void setUser(Set<User> user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
