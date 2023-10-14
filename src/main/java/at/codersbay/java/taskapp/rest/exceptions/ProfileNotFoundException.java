package at.codersbay.java.taskapp.rest.exceptions;

public class ProfileNotFoundException extends EntityNotFoundException {
    private final String defaultMessage = "Profile not found";

    public ProfileNotFoundException() {
        super("Profile not found", null);
    }

    public ProfileNotFoundException(String message, Long id) {
        super(message,id);
    }


    public String getDefaultMessage(){
        return defaultMessage;
    }

}

