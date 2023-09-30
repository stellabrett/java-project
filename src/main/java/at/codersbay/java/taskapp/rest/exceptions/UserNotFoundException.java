package at.codersbay.java.taskapp.rest.exceptions;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException (String message, Long userId) {
        super(message,userId);
    }
}

