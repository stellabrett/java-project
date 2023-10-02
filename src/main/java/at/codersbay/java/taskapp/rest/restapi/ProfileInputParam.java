package at.codersbay.java.taskapp.rest.restapi;

public class ProfileInputParam {

    private Long userId;

    private String bio;

    private String profilePhoto;

    public ProfileInputParam(Long userId, String bio, String profilePhoto){
        this.userId = userId;
        this.bio = bio;
        this.profilePhoto= profilePhoto;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
