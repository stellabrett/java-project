package at.codersbay.java.taskapp.rest.restapi;

import java.time.LocalDate;
import java.util.Set;

public class TaskInputParam {

    private Set<Long> userIds;

    private String title;

    private String description;

    private LocalDate deadline;

    private boolean completed;

    public TaskInputParam(Set<Long> userId, String bio, String profilePhoto, LocalDate deadline, boolean completed){
        this.userIds = userId;
        this.title = bio;
        this.description = profilePhoto;
        this.deadline = deadline;
        this.completed = completed;
    }

    public Set<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Long> userIds) {
        this.userIds = userIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
