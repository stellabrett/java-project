package at.codersbay.java.taskapp.rest.exceptions;

public class UserNotFoundException extends EntityNotFoundException {
    private final String defaultMessage = "User not found";

    public UserNotFoundException() {
        super("User not found", null);
    }

    public UserNotFoundException (String message, Long id) {
        super(message,id);
    }


    public String getDefaultMessage(){
        return defaultMessage;
    }

}

