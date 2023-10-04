package at.codersbay.java.taskapp.rest.exceptions;

public class PrimaryIdNullOrEmptyException extends Exception {

    private final String defaultMessage = "given ID is null or empty";
    public PrimaryIdNullOrEmptyException() {
        super();
    }

    public PrimaryIdNullOrEmptyException(String message) {
        super(message);
    }

    public String getDefaultMessage(){
        return defaultMessage;
    }
}

