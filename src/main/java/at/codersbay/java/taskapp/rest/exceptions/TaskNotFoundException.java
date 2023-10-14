package at.codersbay.java.taskapp.rest.exceptions;

public class TaskNotFoundException extends EntityNotFoundException {
    private final String defaultMessage = "Task not found";

    public TaskNotFoundException() {
        super("Task not found", null);
    }

    public TaskNotFoundException(String message, Long id) {
        super(message,id);
    }


    public String getDefaultMessage(){
        return defaultMessage;
    }

}

