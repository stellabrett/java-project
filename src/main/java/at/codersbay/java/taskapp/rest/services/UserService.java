package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.ProfileDAO;
import at.codersbay.java.taskapp.rest.dao.TaskDAO;
import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.exceptions.PrimaryIdNullOrEmptyException;
import at.codersbay.java.taskapp.rest.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<User> getUsers(){
        return userDAO.findAll();
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


    //TODO rette
    /*
    public User updateUser(Long id) throws PrimaryIdNullOrEmptyException, UserNotFoundException{
        if (id == null){
            throw new PrimaryIdNullOrEmptyException();
        }
        User user = this.userDAO.findById(id).get();
        if (user.getLastname() == null) {
            user.setLastname();
        } else {
            user.getUser().setText(text);
        }

        return this.coursesDao.save(course);


    }*/
    public User updateUser(Long id, User updatedUser) throws UserNotFoundException {
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


}
