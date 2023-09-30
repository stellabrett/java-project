package at.codersbay.java.taskapp.rest.exceptions;

public class EntityNotFoundException extends Exception{
    private Long id;

    public EntityNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}


