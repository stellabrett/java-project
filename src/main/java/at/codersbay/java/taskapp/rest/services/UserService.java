package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.ProfileDAO;
import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.exceptions.PrimaryIdNullOrEmptyException;
import at.codersbay.java.taskapp.rest.exceptions.UserNotFoundException;
import at.codersbay.java.taskapp.rest.restapi.response.UserProfileTaskResponse;
import at.codersbay.java.taskapp.rest.restapi.response.UserTaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {


    @Autowired
    UserDAO userDAO;

    ProfileDAO profileDAO;

    public UserService(){

    }


    /**
     * This method adds a new user after checking if the email address already exists
     *
     * @param user The new user to be added
     * @return The added user if added successfully
     * @throws DuplicateKeyException if the email address already exists in the database
     */
    public User addUser(User user, Profile profile) throws IllegalArgumentException {
        Optional<User> existingUser = userDAO.findUserByEmail(user.getEmail());

        if (existingUser.isPresent()){
            throw new IllegalArgumentException("The email address is already registered!");

        } if (profile != null) {
                user.setProfile(profile);
                profile.setUser(user);
                profileDAO.save(profile);
            }

            userDAO.save(user);
            return user;
        }


    /**
     * This method reads all user incl. their profiles and tasks
     *
     * @return a list of all users containing profiles and tasks
     * @throws UserNotFoundException when no user is found
     */

    public List<UserProfileTaskResponse> getUsers() throws UserNotFoundException {
        List<User> users = userDAO.findAll();
        List<UserProfileTaskResponse> userProfileTaskResponses = new ArrayList<>();

        for (User user : users) {
            Set<Task> tasks = user.getTasks();
            Profile profile = user.getProfile();
            userProfileTaskResponses.add(new UserProfileTaskResponse(user, tasks, profile));
        }

        if (userProfileTaskResponses.isEmpty()) {
            throw new UserNotFoundException();
        }

        return userProfileTaskResponses;
    }

    /**
     *  This method reads a user from the database based on their ID
     *
     * @param userId the id from the user
     * @return user incl. profile and tasks
     * @throws PrimaryIdNullOrEmptyException when the given id is null
     * @throws UserNotFoundException when the user is not found
     */

    public UserProfileTaskResponse getUserById(Long userId) throws PrimaryIdNullOrEmptyException, UserNotFoundException {
        if (userId == null) {
            throw new PrimaryIdNullOrEmptyException();
        }

        Optional<User> userOptional = userDAO.findById(userId);
        //isEmpty..throw new... geht nicht?
        if (userOptional.isPresent()) {
           User user = userOptional.get();

           Set<Task> tasks = user.getTasks();
           Profile profile = user.getProfile();
           return new UserProfileTaskResponse(user, tasks, profile);
        } else {
            throw new UserNotFoundException();
        }
    }

    /**
     *  This method reads a user from the database based on the email address
     * @param email
     * @return If successful, the user including their profile and tasks otherwise null and
     * @throws PrimaryIdNullOrEmptyException when the given id is null
     * @throws UserNotFoundException when the user is not found
     */

    public UserProfileTaskResponse getUserByEmail(String email) throws PrimaryIdNullOrEmptyException, UserNotFoundException {
        if (email == null) {
            throw new PrimaryIdNullOrEmptyException();
        }

        Optional<User> userOptional = userDAO.findUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<Task> tasks = user.getTasks();
            Profile profile = user.getProfile();

            UserProfileTaskResponse response = new UserProfileTaskResponse(user, tasks, profile);
            return response;
        } else {
            throw new UserNotFoundException();
        }
    }

    /**
     * Updates user information, including associated profile (if any),
     *  based on the passed ID and the updated user data in the JSON object.
     *
     * @param id user id
     * @param updatedUser A JSON object containing the updated user data and optional
     *  contains profile information
     * @return the updated user
     * @throws PrimaryIdNullOrEmptyException when the given id is null
     * @throws UserNotFoundException when the user is not found
     */
    public User updateUser(Long id, User updatedUser) throws PrimaryIdNullOrEmptyException, UserNotFoundException, IllegalArgumentException {
        if ( id == null){
            throw new PrimaryIdNullOrEmptyException();
        }
        if (updatedUser == null ||
                (updatedUser.getFirstname() == null && updatedUser.getLastname()== null && updatedUser.getEmail() == null)){
            throw  new IllegalArgumentException("Please Enter valid user data");
        }
        Optional<User> userOptional = userDAO.findById(id);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();

            existingUser.setFirstname(updatedUser.getFirstname());
            existingUser.setLastname(updatedUser.getLastname());
            existingUser.setEmail(updatedUser.getEmail());

            if (updatedUser.getProfile() != null) {
                Profile updatedProfile = updatedUser.getProfile();
                Profile existingProfile = existingUser.getProfile();

                existingProfile.setBio(updatedProfile.getBio());
                existingProfile.setProfilePhoto(updatedProfile.getProfilePhoto());
            }

            userDAO.save(existingUser);

            return existingUser;
        } else {
            throw new UserNotFoundException();
        }
    }

    /**
     * This method deletes a user based on the given id from the database and  from any tasks
     * @param id user id of the user to be deleted
     * @return True if the user was successfully deleted, or false if the user does not exist
     * @throws PrimaryIdNullOrEmptyException when the id is null or empty
     * @throws UserNotFoundException when the user is not found
     */
    public boolean deleteUser (Long id) throws PrimaryIdNullOrEmptyException, UserNotFoundException {

        if (id == null || id <= 0) {
            throw new PrimaryIdNullOrEmptyException();
        }
    // Konstruktorreferenz-Lambda-Ausdruck, erstellt eine neu Instanz der unfe, besser so als anders? weil eh default message?
        User user = userDAO.findById(id).orElseThrow(UserNotFoundException::new);
        user.getTasks().forEach(task -> task.getUsers().remove(user));

            user.getTasks().clear();
            userDAO.delete(user);
            return true;
        }


}
