package at.codersbay.java.taskapp.rest.exceptions;

public class InvalidDeadlineException extends Exception{
    private final String defaultMessage = "Deadline is not valid";

    public InvalidDeadlineException() {
        super();
    }

    public InvalidDeadlineException(String message) {
        super(message);
    }

    public InvalidDeadlineException(String message, Long entityId){
        super(message);

    }

    public String getDefaultMessage(){
        return defaultMessage;
    }

}


