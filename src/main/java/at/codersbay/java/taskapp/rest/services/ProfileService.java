package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.ProfileDAO;
import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.exceptions.EntityNotFoundException;
import at.codersbay.java.taskapp.rest.exceptions.PrimaryIdNullOrEmptyException;
import at.codersbay.java.taskapp.rest.exceptions.ProfileNotFoundException;
import at.codersbay.java.taskapp.rest.exceptions.UserNotFoundException;
import at.codersbay.java.taskapp.rest.restapi.ProfileUserInputParam;
import at.codersbay.java.taskapp.rest.restapi.response.ProfileUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
     * This method creates a new profile and saves it to the database. If a user is specified, the profile is assigned to this
     * @param userId optional user id for which the profile is created
     * @param bio Bio information
     * @param profilePhoto the Url for the profile photo
     * @return boolean true if the profile was created successfully, false if not
     * @throws PrimaryIdNullOrEmptyException when the given id is null
     * @throws UserNotFoundException if the user with the given id, is not found
     */

    public boolean createProfile(Long userId, String bio, String profilePhoto) throws  UserNotFoundException {
        User user = null;
        if(userId != null) {
            user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException());
        }
        Profile profile = new Profile();
        profile.setBio(bio);
        profile.setProfilePhoto(profilePhoto);

        if(user != null){
            user.setProfile(profile);
            userDAO.save(user);
        }else {
            profileDAO.save(profile);
        }
        return true;

    }

    /**
     * this method reads all profiles incl. the associated user from database
     * @return a List of profiles and their associated user
     * @throws EntityNotFoundException when no profiles exist
     */
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

    /**
     * reads a profile from the database based on the user id
     * @param userId the id from the user
     * @return user incl. profile or null when the user was not found
     * @throws PrimaryIdNullOrEmptyException when given id is null
     * @throws UserNotFoundException when user id not found
     */
    public ProfileUserResponse getProfileById(Long userId) throws PrimaryIdNullOrEmptyException, UserNotFoundException{
        if(userId == null){
            throw new PrimaryIdNullOrEmptyException();
        }

        Optional<Profile> profileOptional = profileDAO.findById(userId);

     if (profileOptional.isPresent()){
         Profile profile = profileOptional.get();
         User user = profile.getUser();
         return new ProfileUserResponse(user,profile);
     } else {
         throw new UserNotFoundException();
     }

    }

    public Profile updateProfile(Profile profile){
        profileDAO.save(profile);
        return profile;
    }


    /**
     * searches for the profile based on the passed ID and updates both the profile and the
     * User information based on input parameters
     *
     * @param profileId the id of the profile to update
     * @param param the input parameters, containing the updated profile and user infos.
     * @throws PrimaryIdNullOrEmptyException when given id is null or invalid
     * @throws ProfileNotFoundException if the profile with the given id is not found
     * @throws UserNotFoundException when the associated User of the profile is not found
     */
    @Transactional
    public void updateProfileAndUser(Long profileId, ProfileUserInputParam param)
    throws PrimaryIdNullOrEmptyException, ProfileNotFoundException, UserNotFoundException {
        if(profileId == null){
            throw new PrimaryIdNullOrEmptyException();
        }

        Profile profile = profileDAO.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("profil not found", null));

        User user = profile.getUser();
        if(user == null){
            throw new UserNotFoundException();
        }

        profile.setBio(param.getBio());
        profile.setProfilePhoto(param.getProfilePhoto());

        user.setEmail(param.getEmail());
        user.setFirstname(param.getFirstname());
        user.setLastname(param.getLastname());

        profileDAO.save(profile);
        userDAO.save(user);
    }

    /**
     * searches for the profile based on the passed ID and delete it, optional also the user
     *
     * @param profileId the id of the profile to delete
     * @param deleteUser the input parameters to delete the user, false = delete user, true = do not delete user
     * @throws PrimaryIdNullOrEmptyException when given id is null or invalid
     * @throws ProfileNotFoundException if the profile with the given id is not found
     * @throws UserNotFoundException when the associated User of the profile is not found
     */
    @Transactional
    public void deleteProfileById(Long profileId, boolean deleteUser)
            throws PrimaryIdNullOrEmptyException,ProfileNotFoundException, UserNotFoundException{
        if(profileId == null){
            throw new PrimaryIdNullOrEmptyException();
        }

        Profile profile = profileDAO.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException());

        if(deleteUser){
            User user = profile.getUser();
            if(user != null){
                userDAO.delete(user);
            }
        }
        profileDAO.delete(profile);
    }










}

