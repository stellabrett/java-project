package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.ProfileDAO;
import at.codersbay.java.taskapp.rest.dao.TaskDAO;
import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.exceptions.EntityNotFoundException;
import at.codersbay.java.taskapp.rest.exceptions.PrimaryIdNullOrEmptyException;
import at.codersbay.java.taskapp.rest.exceptions.UserNotFoundException;
import at.codersbay.java.taskapp.rest.restapi.UserTaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
     * add a new User to db
     * @param user
     * @return saved Users
     */
    public User addUser(User user){
        userDAO.save(user);
        return user;
    }

    /**
     *
     * @return a list of all users incl. their profiles and tasks
     */
   /* public List<User> getUsers(){
        return userDAO.findAll();
    }*/

    public List<UserTaskResponse> getUsers() throws EntityNotFoundException {
        List<User> users = userDAO.findAll();
        List<UserTaskResponse> userTaskResponses = new ArrayList<>();

        for (User user : users) {
            Set<Task> tasks = user.getTasks();
            Profile profile = user.getProfile();
            userTaskResponses.add(new UserTaskResponse(user, tasks, profile));
        }

        if (userTaskResponses.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return userTaskResponses;
    }



    public User getUserById(Long userId) throws PrimaryIdNullOrEmptyException, UserNotFoundException {
        if (userId == null) {
            throw new PrimaryIdNullOrEmptyException();
        }

        Optional<User> userOptional = userDAO.findById(userId);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException();
        }
    }

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
     * update user and profile
     * @param id prom PathVariable
     * @param updatedUser from Request body
     * @return user
     * @throws PrimaryIdNullOrEmptyException
     * @throws UserNotFoundException
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
     * delete user, and delete user from their tasks
     * @param id
     * @return boolean
     * @throws PrimaryIdNullOrEmptyException
     * @throws UserNotFoundException
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
