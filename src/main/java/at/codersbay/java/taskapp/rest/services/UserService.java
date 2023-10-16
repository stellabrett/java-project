package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.exceptions.PrimaryIdNullOrEmptyException;
import at.codersbay.java.taskapp.rest.exceptions.UserNotFoundException;
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

    public UserService(){

    }


    /**
     * This method adds a new user after checking if the email address already exists
     *
     * @param newUser The new user to be added
     * @return The added user if added successfully
     * @throws DuplicateKeyException if the email address already exists in the database
     */
    public User addUser(User newUser) throws IllegalArgumentException {
        Optional<User> existingUser = userDAO.findUserByEmail(newUser.getEmail());
        if(existingUser.isPresent()){
            throw new IllegalArgumentException("this email address already exist");
        }else {
            userDAO.save(newUser);
            return newUser;
        }
    }

    /**
     * This method reads all user incl. their profiles and tasks
     *
     * @return a list of all users containing profiles and tasks
     * @throws UserNotFoundException when no user is found
     */

    public List<UserTaskResponse> getUsers() throws UserNotFoundException {
        List<User> users = userDAO.findAll();
        List<UserTaskResponse> userTaskResponses = new ArrayList<>();

        for (User user : users) {
            Set<Task> tasks = user.getTasks();
            Profile profile = user.getProfile();
            userTaskResponses.add(new UserTaskResponse(user, tasks));
        }

        if (userTaskResponses.isEmpty()) {
            throw new UserNotFoundException();
        }

        return userTaskResponses;
    }

    /**
     *  This method reads a user from the database based on their ID
     *
     * @param userId the id from the user
     * @return user incl. profile and tasks
     * @throws PrimaryIdNullOrEmptyException when the given id is null
     * @throws UserNotFoundException when the user is not found
     */

    public UserTaskResponse getUserById(Long userId) throws PrimaryIdNullOrEmptyException, UserNotFoundException {
        if (userId == null) {
            throw new PrimaryIdNullOrEmptyException();
        }

        Optional<User> userOptional = userDAO.findById(userId);

        if (userOptional.isPresent()) {
           User user = userOptional.get();

           Set<Task> tasks = user.getTasks();
           return new UserTaskResponse(user, tasks );
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

    public User getUserByEmail(String email) throws PrimaryIdNullOrEmptyException, UserNotFoundException {
        if (email == null) {
            throw new PrimaryIdNullOrEmptyException();
        }

        Optional<User> userOptional = userDAO.findUserByEmail(email);

        if (userOptional.isPresent()) {
            return userOptional.get();
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
    public User updateUser(Long id, User updatedUser) throws PrimaryIdNullOrEmptyException, UserNotFoundException {
        if ( id == null){
            throw new PrimaryIdNullOrEmptyException();
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
     * @throws PrimaryIdNullOrEmptyException when the id is null
     * @throws UserNotFoundException when the user is not found
     */
    public boolean deleteUser (Long id) throws PrimaryIdNullOrEmptyException, UserNotFoundException {

        if (id == null) {
            throw new PrimaryIdNullOrEmptyException();
        }

        Optional<User> userOptional = userDAO.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.getTasks().clear();
            userDAO.delete(user);
            return true;
        }else{
            throw new UserNotFoundException();
        }

    }



}
