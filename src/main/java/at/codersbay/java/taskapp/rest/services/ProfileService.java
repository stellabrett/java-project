package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.ProfileDAO;
import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.exceptions.EntityNotFoundException;
import at.codersbay.java.taskapp.rest.exceptions.PrimaryIdNullOrEmptyException;
import at.codersbay.java.taskapp.rest.exceptions.UserNotFoundException;
import at.codersbay.java.taskapp.rest.restapi.ProfileInputParam;
import at.codersbay.java.taskapp.rest.restapi.ProfileUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProfileService {


    @Autowired
    ProfileDAO profileDAO;

    @Autowired
    UserDAO userDAO;

    public ProfileService() {
    }


//zu testzwecken
    public Profile addProfile(Profile profile){
        profileDAO.save(profile);
        return profile;
    }

    /**
     *
     * @param userId
     * @param bio
     * @param profilePhoto
     * @return boolean
     * @throws PrimaryIdNullOrEmptyException
     * @throws UserNotFoundException
     */

    public boolean createProfile(Long userId, String bio, String profilePhoto) throws PrimaryIdNullOrEmptyException, UserNotFoundException {
        if(userId == null){
            throw new PrimaryIdNullOrEmptyException("given id is null");
        }

        User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found", userId));

        Profile profile = new Profile();
        profile.setBio(bio);
        profile.setProfilePhoto(profilePhoto);

        user.setProfile(profile);

        userDAO.save(user);
        return true;

    }

    public List<ProfileUserResponse> getProfiles()throws EntityNotFoundException{
        List<Profile> profiles = profileDAO.findAll();
        List<ProfileUserResponse> profileUserResponse = new ArrayList<>();
        for (Profile profile : profiles){
            User user = profile.getUser();
            profileUserResponse.add(new ProfileUserResponse(user, profile));
        }

        if(profileUserResponse.isEmpty()){
            throw new EntityNotFoundException();
        }

        return  profileUserResponse;

    }





}

