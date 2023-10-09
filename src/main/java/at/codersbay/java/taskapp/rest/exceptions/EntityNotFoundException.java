package at.codersbay.java.taskapp.rest.exceptions;

public class EntityNotFoundException extends Exception{
    private final String defaultMessage = "Entity not found";
    private Long entityId;
    public EntityNotFoundException () {
        super();
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Long entityId){
        super(message);
        this.entityId = entityId;
    }

    public String getDefaultMessage(){
        return defaultMessage;
    }
    public Long getEntityId(){
        return entityId;
    }
}


