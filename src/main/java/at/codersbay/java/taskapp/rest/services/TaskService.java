package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.TaskDAO;
import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.Profile;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.exceptions.*;
import at.codersbay.java.taskapp.rest.restapi.ProfileUserInputParam;
import at.codersbay.java.taskapp.rest.restapi.TaskUserInputParam;
import at.codersbay.java.taskapp.rest.restapi.response.TaskUserResponse;
import at.codersbay.java.taskapp.rest.restapi.response.UserTaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class TaskService {

    @Autowired
    TaskDAO taskDAO;

    @Autowired
    UserDAO userDAO;

    public TaskService() {

    }

    /**
     * This method creates a task and assigns it to the specified users
     *
     * @param userIds
     * @param title
     * @param description
     * @param deadline
     * @param completed
     * @return true if the task was successfully created and assigned to users, otherwise false
     * @throws PrimaryIdNullOrEmptyException when the id is null
     * @throws UserNotFoundException when the user is not found
     */
    public boolean createTask(Set<Long> userIds, String title, String description, LocalDate deadline, boolean completed)
            throws PrimaryIdNullOrEmptyException, UserNotFoundException {
        if (userIds == null || userIds.isEmpty()) {
            throw new PrimaryIdNullOrEmptyException("user Ids are null or empty");
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline(deadline);
        task.setCompleted(completed);

        for (Long userId : userIds) {
            User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found", userId));
            user.getTasks().add(task);
        }
        taskDAO.save(task);

        return true;

    }

    /**
     * Retrieves a list of all tasks along with their associated users
     *
     * @return  list of TaskUserResponse objects containing tasks, users, and error messages (if any)
     */
    public List<TaskUserResponse> getTasks() {
        List<Task> tasks = taskDAO.findAll();
        List<TaskUserResponse> taskResponses = new ArrayList<>();

        for (Task task : tasks) {
            Set<User> users = task.getUsers();
            TaskUserResponse response = new TaskUserResponse(task, users,null);
            taskResponses.add(response);
        }

        return taskResponses;
    }


    /**
     * This method finds a task in the database using the passed ID including all associated users
     * @param id task id
     * @return task and associated user
     * @throws PrimaryIdNullOrEmptyException when the id is null
     * @throws TaskNotFoundException when the task based on this id is not found
     */
    public TaskUserResponse getTaskById(Long id) throws PrimaryIdNullOrEmptyException, TaskNotFoundException {
        if (id == null) {
            throw new PrimaryIdNullOrEmptyException();
        }

        Optional<Task> taskOptional = taskDAO.findById(id);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Set<User> users = task.getUsers();

            return new TaskUserResponse(task, users, null);
        }
        throw new TaskNotFoundException();
    }

    /**
     * This method deletes a task based on the passed id
     *
     * @param id the id of the task to be deleted
     * @return boolean true if the task is deleted, false if not
     * @throws PrimaryIdNullOrEmptyException when the id is null
     * @throws TaskNotFoundException when the Task is not found
     */

    public boolean deleteTask(Long id) throws PrimaryIdNullOrEmptyException, TaskNotFoundException {

        if (id == null) {
            throw new PrimaryIdNullOrEmptyException("given id is null.");
        }

        Task task = null;

        try {
            task = this.taskDAO.findById(id).get();

            if (task.getUsers() != null && task.getUsers().size() > 0) {
                for (User user : task.getUsers()) {
                    user.setTasks(null);
                    this.userDAO.save(user);
                }
            }

            this.taskDAO.delete(task);
        } catch (NoSuchElementException nsee) {
            throw new TaskNotFoundException("could not found task with given id.", id);
        }

        return false;
    }

    /**
     * This method update a task based on the id and the JSON with the updated Infos
     *
     * @param taskId the id of the task to be updated
     * @param param a JSON Object with the updated task
     * @return If successful, the updated task
     * @throws PrimaryIdNullOrEmptyException when the given id is null
     * @throws TaskNotFoundException when the task is not found
     * @throws UserNotFoundException when the user is not found
     */

    @Transactional
    public Task updateTask(Long taskId, TaskUserInputParam param)
            throws PrimaryIdNullOrEmptyException, TaskNotFoundException, UserNotFoundException {
        if (taskId == null) {
            throw new PrimaryIdNullOrEmptyException();
        }

        Task existingTask = taskDAO.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("task not found", null));

        existingTask.setTitle(param.getTitle());
        existingTask.setDescription(param.getDescription());
        existingTask.setDeadline(param.getDeadline());
        existingTask.setCompleted(param.isCompleted());

        if (param.getUserIds() != null && param.getUserIds().isEmpty()) {
            Set<Long> newUserIds = param.getUserIds();
            Set<User> updatedUsers = new HashSet<>();

            for (Long userId : newUserIds) {
                User user = userDAO.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException());


                user.getTasks().add(existingTask);
                updatedUsers.add(user);
            }
            existingTask.setUsers(updatedUsers);
        }
        return taskDAO.save(existingTask);

    }
}




