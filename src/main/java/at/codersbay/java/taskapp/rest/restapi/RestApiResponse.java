package at.codersbay.java.taskapp.rest.restapi;

public class RestApiResponse {

    String message;
    Object object;

    public  RestApiResponse(String message, Object object)  {
        this.message = message;
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}


