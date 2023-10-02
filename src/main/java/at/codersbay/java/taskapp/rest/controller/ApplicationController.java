package at.codersbay.java.taskapp.rest.controller;

import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.exceptions.PrimaryIdNullOrEmptyException;
import at.codersbay.java.taskapp.rest.exceptions.UserNotFoundException;
import at.codersbay.java.taskapp.rest.restapi.ProfileInputParam;
import at.codersbay.java.taskapp.rest.restapi.RestApiResponse;
import at.codersbay.java.taskapp.rest.services.ProfileService;
import at.codersbay.java.taskapp.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    /**
     * add a new User
     * @param user (firstname, lastname, email)
     * @return the saved User
     */
    @PostMapping("addUser")
    public User addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    /**
     * get all users incl. their profiles and tasks
     * @return all users
     */
   @GetMapping("/users")
        List<User> getUser(){
            return userService.getUsers();
        }

///////////////// profile


    @PostMapping("/profile")
    public Profile addProfile(@RequestBody Profile profile) {return profileService.addProfile(profile);}


    /**
     * create a new profile for a existing user
     * @param param userId, bio, profilePhoto as an url
     * @return user incl. profile
     */
    @PostMapping("/createProfile")
    public ResponseEntity<RestApiResponse> createProfile(@RequestBody ProfileInputParam param) {

       HttpStatus status = null;
       String message = "";
       Object responseObject = null;


       try{
            boolean result = profileService.createProfile(param.getUserId(), param.getBio(), param.getProfilePhoto());
           status = HttpStatus.OK;
           message = "Profile created successfully";
           responseObject = result;
       }catch (PrimaryIdNullOrEmptyException pinoee){
           message = "please enter a user ID ";
           status = HttpStatus.BAD_REQUEST;

       }catch (UserNotFoundException unfe){
           message = "user not found";
           status = HttpStatus.NOT_FOUND;

       }

        RestApiResponse response = new RestApiResponse(message, responseObject);

       return  new ResponseEntity<>(response, status);
    }



    }






