package at.codersbay.java.taskapp.rest.controller;

import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.exceptions.*;
import at.codersbay.java.taskapp.rest.restapi.*;
import at.codersbay.java.taskapp.rest.restapi.response.ProfileUserResponse;
import at.codersbay.java.taskapp.rest.restapi.response.RestApiResponse;
import at.codersbay.java.taskapp.rest.restapi.response.TaskUserResponse;
import at.codersbay.java.taskapp.rest.restapi.response.UserTaskResponse;
import at.codersbay.java.taskapp.rest.services.ProfileService;
import at.codersbay.java.taskapp.rest.services.TaskService;
import at.codersbay.java.taskapp.rest.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
//TODO add verknüpfen mit profil, ERROR NonUniqueResultException
    /**
     * API endpoint to add a new User
     *
     * HTTP request method: POST
     * Path: /addUser
     *
     * @param user (firstname, lastname, email)
     * @return HTTP status 200 (OK) and the saved user
     * HTTP status 500 (Bad Request) if the user already exists
     *
     */
    @PostMapping("/users")
    public ResponseEntity<?> addUser (@RequestBody User user) throws IllegalArgumentException {
        try {
            User newUser = userService.addUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The email address is already registered");
        }
    }



    /**
     * API endpoint to get a list of all users incl. their profiles and tasks
     *
     * HTTP request method: GET
     * Path: /users
     *
     * @return HTTP status 200 (OK) and all users,
     * HTTP status 404 (NOT FOUND) if no user was found
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserTaskResponse>> getUsers() {
        try {
            List<UserTaskResponse> userTaskResponses = userService.getUsers();
            return new ResponseEntity<>(userTaskResponses, HttpStatus.OK);
        } catch (UserNotFoundException unfe) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * API endpoint to find a user by id, including their profile and tasks
     *
     * HTTP request method: GET
     * Path: /users/{id}
     *
     * @param id user Id
     * @return HTTP status 200 (OK) and the user,
     * HTTP status 500 (Bad Request) and a message,
     * HTTP status 404 (Not found) and a message
     */
    @GetMapping("/users/{id}")
    public ResponseEntity <UserTaskResponse> getUserById(@PathVariable Long id) {
        try {
            UserTaskResponse userTaskResponse = userService.getUserById(id);
            return new ResponseEntity<>(userTaskResponse, HttpStatus.OK);
        } catch (PrimaryIdNullOrEmptyException pinoee) {
            return new ResponseEntity<>(new UserTaskResponse(pinoee.getDefaultMessage()), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException unfe) {
            return new ResponseEntity<>(new UserTaskResponse(unfe.getDefaultMessage()), HttpStatus.NOT_FOUND);
        }
    }



    /**
     * API endpoint to find a user by email, including their profile and tasks
     *
     * HTTP request method: GET
     * Path: /users/userByEmail/{email}
     *
     * @param email the email address of the user
     * @return HTTP status 200 (OK) and the user,
     * HTTP status 500 (Bad Request) and a message,
     * HTTP status 404 (Not found) and a message
     */

    @GetMapping("/users/userByEmail/{email}")
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
     * API endpoint to update a user based on the given id,  including associated profile, if there is one.
     *
     * HTTP request method: PUT
     * Path: /users/{id}
     *
     * @param id the id of the user to update
     * @param updatedUser a JSON containing the updated infos and optional the profile infos
     * @return A ResponseEntity with a RestApiResponse object containing status and a message
     */

    @PutMapping("/users/{id}")
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
     * API endpoint to delete a user and deletes him from his tasks
     *
     * HTTP request method: PUT
     * Path: user/{id}
     *
     * @param id the id of the user to delete
     * @return HTTP status 200 (OK) true and a message,
     *      * HTTP status 500 (Bad Request) and a message,
     *      * HTTP status 404 (Not found) false, and a message
     */

    //TODO fängt nicht ab wenn keine Id im postman geschickt wird?
    @DeleteMapping("user/{id}")
    ResponseEntity<RestApiResponse> deleteUser(@PathVariable(required = false) Long id) {
        HttpStatus status = null;
        String message = "";
        boolean result = false;

        if(id == null){
            status = HttpStatus.BAD_REQUEST;
            message = " please enter your id";
        }else {

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
     * API endpoint to get all Tasks including the associated users
     *
     * HTTP request method: DELETE
     * Path: /tasks
     *
     * @return  HTTP status 200 (OK) and all tasks
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskUserResponse>> getTasks() {

            List<TaskUserResponse> taskUserResponses = taskService.getTasks();
            return new ResponseEntity<>(taskUserResponses, HttpStatus.OK);

    }

    //TODO Profile werden angezeigt...bitte kein @JSONIgnore mehr
    /**
     * API endpoint to get all tasks and their associated users
     *
     * HTTP request method: GET
     * Path: /task/{id}
     *
     * @param id th task id
     * @return HTTP status 200 (OK) and the task
     * HTTP status 404 (Not Found)a message and null,
     * HTTP status 500 (Bad Request) a message and null
     */
    @GetMapping("/task/{id}")
    public ResponseEntity<TaskUserResponse> getTaskById(@PathVariable Long id) {
        TaskUserResponse response = new TaskUserResponse();

        try {
            response = taskService.getTaskById(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PrimaryIdNullOrEmptyException pinoee) {
            response.setErrorMessage(pinoee.getDefaultMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (TaskNotFoundException enfe) {
            response.setErrorMessage(enfe.getDefaultMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    //TODO warum das doofe EntityNotFound

    /**
     * Api endpoint for deleting a task based on the id
     *
     * HTTP request method: DELETE
     * Path: /task/{id}
     *
     * @param id the task id to be deleted
     *
     * @return HTTP status 200 (OK) message and true if the task was deleted
     * HTTP status 404 (Not Found) message and false
     * HTTP status 500 (Bad Request) a message and false
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

        } catch (TaskNotFoundException tnfe) {
            message = tnfe.getDefaultMessage();
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
     * @return HTTP status 200 (OK) and the updated task
     * HTTP status 404 (Not Found) Task not found
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






