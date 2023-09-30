package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.ProfileDAO;
import at.codersbay.java.taskapp.rest.dao.TaskDAO;
import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<User> getUsers(){
        return userDAO.findAll();
    }




}
