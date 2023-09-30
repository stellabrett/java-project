package at.codersbay.java.taskapp.rest.controller;

import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    private UserService businessService;

    /**
     * add a new User
     * @param user (firstname, lastname, email)
     * @return the saved User
     */
    @PostMapping("addUser")
    public User addUser(@RequestBody User user){
        return businessService.addUser(user);
    }

    /**
     * get all users
     * @return all users
     */
   @GetMapping("/users")
        List<User> getUser(){
            return businessService.getUsers();
        }

///////////////// profile

    //TODO@PostMapping("/profile")




    }






