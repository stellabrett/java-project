package at.codersbay.java.taskapp.rest.controller;

import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.exceptions.*;
import at.codersbay.java.taskapp.rest.restapi.*;
import at.codersbay.java.taskapp.rest.services.ProfileService;
import at.codersbay.java.taskapp.rest.services.TaskService;
import at.codersbay.java.taskapp.rest.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private TaskService taskService;


    /////////////////////////////// User

    /**
     * add a new User
     *
     * @param user (firstname, lastname, email)
     * @return the saved User
     */
    @PostMapping("addUser")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * get all users incl. their profiles and tasks
     *
     * @return all users
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserTaskResponse>> getUsers() {
        try {
            List<UserTaskResponse> userTaskResponses = userService.getUsers();
            return new ResponseEntity<>(userTaskResponses, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    //TODO show tasks

    /**
     * @param id
     * @return a user incl.profile and tasks
     */
    @GetMapping("/userById/{id}")
    public ResponseEntity<RestApiResponse> getUserById(@PathVariable Long id) {
        HttpStatus status = null;
        String message = "";
        User user = null;

        try {
            user = this.userService.getUserById(id);
            status = HttpStatus.OK;

        } catch (PrimaryIdNullOrEmptyException pinoee) {
            message = pinoee.getDefaultMessage();
            status = HttpStatus.BAD_REQUEST;

        } catch (UserNotFoundException unfe) {
            message = unfe.getDefaultMessage();
            status = HttpStatus.NOT_FOUND;
        }
        RestApiResponse response = new RestApiResponse(message, user);
        return new ResponseEntity<>(response, status);
    }


    //TODO tasks

    /**
     * finds a user by email
     *
     * @param email
     * @return a user incl. profile und  tasks
     */
    @GetMapping("/userByEmail/{email}")
    public ResponseEntity<RestApiResponse> getUserByEmail(@PathVariable String email) {
        HttpStatus status = null;
        String message = "";
        User user = null;

        try {
            user = this.userService.getUserByEmail(email);
            status = HttpStatus.OK;
            message = "e voila";

        } catch (PrimaryIdNullOrEmptyException pinoee) {
            message = pinoee.getDefaultMessage();
            status = HttpStatus.BAD_REQUEST;

        } catch (UserNotFoundException unfe) {
            message = unfe.getDefaultMessage();
            status = HttpStatus.NOT_FOUND;
        }
        RestApiResponse response = new RestApiResponse(message, user);
        return new ResponseEntity<>(response, status);
    }


    /**
     * update user information and their profile
     *
     * @param id
     * @param updatedUser
     * @return status message
     */

    @PutMapping("/user/{id}")
    public ResponseEntity<RestApiResponse> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser
    ) {
        HttpStatus status = null;
        String message = "";
        User resultUser = null;

        try {
            User updated = this.userService.updateUser(id, updatedUser);
            if (updated != null) {

                status = HttpStatus.OK;
                message = "e voila";
                resultUser = updated;
            } else {
                status = HttpStatus.BAD_REQUEST;
                message = "update user failed";
            }
        } catch (UserNotFoundException unfe) {
            message = unfe.getDefaultMessage();
            status = HttpStatus.NOT_FOUND;
        } catch (PrimaryIdNullOrEmptyException pinoee) {
            message = pinoee.getDefaultMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        RestApiResponse response = new RestApiResponse(message, resultUser);
        return new ResponseEntity<>(response, status);
    }

    /**
     * Delete user by id
     *
     * @param id
     * @return true and a message
     */
    @DeleteMapping("user/{id}")
    ResponseEntity<RestApiResponse> deleteUser(@PathVariable Long id) {
        HttpStatus status = null;
        String message = "";
        boolean result = false;

        try {
            result = userService.deleteUser(id);
            status = HttpStatus.OK;
            message = "User succsessfully deleted";

        } catch (UserNotFoundException unfe) {
            message = unfe.getDefaultMessage();
            status = HttpStatus.NOT_FOUND;

        } catch (PrimaryIdNullOrEmptyException pinoee) {
            message = pinoee.getDefaultMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        RestApiResponse response = new RestApiResponse(message, result);
        return new ResponseEntity<>(response, status);
    }




///////////////// profile


    @PostMapping("/profile")
    public Profile addProfile(@RequestBody Profile profile) {
        return profileService.addProfile(profile);
    }


    /**
     * create a new profile for a existing user
     *
     * @param param userId, bio, profilePhoto as an url
     * @return user incl. profile
     */
    @PostMapping("/createProfile")
    public ResponseEntity<RestApiResponse> createProfile(@RequestBody ProfileInputParam param) {

        HttpStatus status = null;
        String message = "";
        Object responseObject = null;


        try {
            boolean result = profileService.createProfile(param.getUserId(), param.getBio(), param.getProfilePhoto());
            status = HttpStatus.OK;
            message = "Profile created successfully";
            responseObject = result;
        } catch (PrimaryIdNullOrEmptyException pinoee) {
            message = "please enter a user ID ";
            status = HttpStatus.BAD_REQUEST;

        } catch (UserNotFoundException unfe) {
            message = "user not found";
            status = HttpStatus.NOT_FOUND;

        }

        RestApiResponse response = new RestApiResponse(message, responseObject);

        return new ResponseEntity<>(response, status);
    }

    /**
     * get all profiles incl. the associated user
     * @return na das selbe...
     */
    @GetMapping("/profiles")
    public ResponseEntity<List<ProfileUserResponse>> getProfiles() {
        try {
            List<ProfileUserResponse> profileUserResponses = profileService.getProfiles();
            return new ResponseEntity<>(profileUserResponses, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * API endpoint to query a profile by user Id
     *
     * HTTP request method: GET
     * Path: /profiles/{id}
     *
     * @param id users Id
     * @return HTTP status 200 (OK) and the queried profile incl. associated user,
     * HTTP status 404 (Not Found) if the user was not found
     * HTTP status 500 (Bad Request) if the given id is null
     */
    @GetMapping("/profiles/{id}")
    public ResponseEntity <ProfileUserResponse> getProfileById(@PathVariable Long id) {
        try {
            ProfileUserResponse profileUserResponses = profileService.getProfileById(id);
            return new ResponseEntity<>(profileUserResponses, HttpStatus.OK);
        } catch (PrimaryIdNullOrEmptyException pinoee) {
            return new ResponseEntity<>(new ProfileUserResponse(pinoee.getDefaultMessage()), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException unfe) {
            return new ResponseEntity<>(new ProfileUserResponse(unfe.getDefaultMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Api endpoint to update an existing profile and associated user data based on the given id
     * HTTP request method: PUT
     * Path: /profiles/{id}
     *
     * @param id profileId
     * @param param the input parameters,that containing the updated profile and user infos.
     * @return HTTP status 200 (OK) and a success message
     * HTTP status 404 (Not Found) if the user was not found , error message
     * HTTP status 500 (Bad Request) if the given id is null, error message
     */
    @PutMapping("/profiles/{id}")
    public ResponseEntity<String> updateProfileAndUser(@PathVariable Long id, @RequestBody ProfileUserInputParam param) {
        try {
            profileService.updateProfileAndUser(id, param);
            return new ResponseEntity<>("Profile and User updated successfully", HttpStatus.OK);
        } catch (ProfileNotFoundException pnfe) {
            return new ResponseEntity<>(pnfe.getDefaultMessage(), HttpStatus.NOT_FOUND);
        } catch (PrimaryIdNullOrEmptyException pinoee) {
            return new ResponseEntity<>(pinoee.getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }catch (UserNotFoundException unfe){
            return new ResponseEntity<>(unfe.getDefaultMessage(), HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Api endpoint to delete a profile and optional the associated user
     *
     * HTTP request method: DELETE
     * Path: /profiles/{id}
     *
     * @param id profileId
     * @param deleteUser the input parameter, true for deleting the user, false for not deleting the user
     * @return HTTP status 200 (OK) and a success message
     * HTTP status 404 (Not Found) if the profile was not found , error message
     * HTTP status 500 (Bad Request) if the given id is null, error message
     */
    @DeleteMapping("/profiles/{id}")
    public ResponseEntity<String>deleteProfileById(
            @PathVariable Long id,
            @RequestParam(value = "deleteUser", required = false, defaultValue = "false" ) boolean deleteUser){
            try {
            profileService.deleteProfileById(id, deleteUser);
            return new ResponseEntity<>("successfully deleted", HttpStatus.OK);
            }catch (ProfileNotFoundException pnfe) {
                return new ResponseEntity<>(pnfe.getDefaultMessage(), HttpStatus.NOT_FOUND);
            } catch (PrimaryIdNullOrEmptyException pinoee) {
                return new ResponseEntity<>(pinoee.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }catch (UserNotFoundException unfe){
                return new ResponseEntity<>(unfe.getDefaultMessage(), HttpStatus.NOT_FOUND);
            }

    }



//////////////// tasks

    /**
     * @param param
     * @return
     */
    @PostMapping("/task")
    public ResponseEntity<RestApiResponse> createTask(@RequestBody TaskInputParam param) {
        HttpStatus status = null;
        String message = "";
        boolean result = false;

        try {
            boolean created = taskService.createTask(
                    param.getUserIds(),
                    param.getTitle(),
                    param.getDescription(),
                    param.getDeadline(),
                    param.isCompleted()
            );

            if (created) {
                status = HttpStatus.OK;
                message = "Task created successfully";
                result = true;

            } else {
                status = HttpStatus.BAD_REQUEST;
                message = "failed";

            }
        } catch (PrimaryIdNullOrEmptyException pinoee) {
            message = "please enter a user ID ";
            status = HttpStatus.BAD_REQUEST;

        } catch (UserNotFoundException unfe) {
            message = "user not found";
            status = HttpStatus.NOT_FOUND;

        }

        RestApiResponse response = new RestApiResponse(message, result);

        return new ResponseEntity<>(response, status);
    }

    /**
     * get all tasks
     * @return  all tasks incl associated User
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks() {
        List<Task> tasks = taskService.getTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * get Task by Id and the associated User
     * @param id
     * @return a message and the object with the task and the associated User
     */
    @GetMapping("/task/{id}")
    public ResponseEntity<RestApiResponse> getTaskById(@PathVariable Long id){
        HttpStatus status = null;
        String message = "";
        Task task= null;

        try{
            task = taskService.getTaskById(id);
            status = HttpStatus.OK;
            message = " ein task bitteeeschen";
        } catch (PrimaryIdNullOrEmptyException pinoee){
            status = HttpStatus.BAD_REQUEST;
            message = pinoee.getDefaultMessage();
        }catch (EntityNotFoundException enfe){
            status = HttpStatus.NOT_FOUND;
            message = enfe.getDefaultMessage();
        }

        RestApiResponse response = new RestApiResponse(message, task);
        return new ResponseEntity<>(response, status);
    }


    /**
     * delete Task by Id
     * @param id
     * @return message and boolean
     */
    @DeleteMapping("task/{id}")
    ResponseEntity<RestApiResponse> deleteTask(@PathVariable Long id) {
        HttpStatus status = null;
        String message = "";
        boolean result = false;

        try {
            result = taskService.deleteTask(id);
            status = HttpStatus.OK;
            message = "Task succsessfully deleted";

        } catch (EntityNotFoundException enfe) {
            message = enfe.getDefaultMessage();
            status = HttpStatus.NOT_FOUND;

        } catch (PrimaryIdNullOrEmptyException pinoee) {
            message = pinoee.getDefaultMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        RestApiResponse response = new RestApiResponse(message, result);
        return new ResponseEntity<>(response, status);
    }

    //TODO ERROR HAndling
    /**
     * Api endpoint for updating a task by its it and optional its users
     *
     * HTTP request method: PUT
     * Path: /tasks/{id}
     *
     * @param taskId task id
     * @param param the input parameters, containing the updated task and user Ids
     * @return
     */
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskUserInputParam param
    ) {
        try {
            Task updatedTask = taskService.updateTask(taskId, param);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (TaskNotFoundException tnfe) {
            return new ResponseEntity<>(tnfe.getDefaultMessage(), HttpStatus.NOT_FOUND);
        } catch (UserNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getDefaultMessage(), HttpStatus.NOT_FOUND);
        } catch (PrimaryIdNullOrEmptyException pinoee) {
            return new ResponseEntity<>(pinoee.getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}






