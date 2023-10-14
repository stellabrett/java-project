package at.codersbay.java.taskapp.rest.restapi;

public class ProfileUserInputParam {
    private String email;
    private String firstname;
    private String lastname;
    private String bio;

    private String profilePhoto;

    public ProfileUserInputParam(){

    }

    public ProfileUserInputParam(String email, String firstname, String lastname, String bio, String profilePhoto) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bio = bio;
        this.profilePhoto= profilePhoto;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
