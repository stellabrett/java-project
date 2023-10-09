package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.TaskDAO;
import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.exceptions.EntityNotFoundException;
import at.codersbay.java.taskapp.rest.exceptions.PrimaryIdNullOrEmptyException;
import at.codersbay.java.taskapp.rest.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TaskService {

    @Autowired
    TaskDAO taskDAO;

    @Autowired
    UserDAO userDAO;

    public TaskService(){

    }

    public boolean createTask(Set<Long> userIds, String title, String description, LocalDate deadline, boolean completed )
    throws PrimaryIdNullOrEmptyException,UserNotFoundException {
        if(userIds == null || userIds.isEmpty()){
            throw new PrimaryIdNullOrEmptyException("user Ids are null or empty");
        }
       // Set<User> user = new HashSet<>();

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline(deadline);
        task.setCompleted(completed);

        for (Long userId : userIds){
            User user = userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found", userId));
            user.getTasks().add(task);
        }
        //Set<Task> userTask = user.getTasks();
        //userTask.add(task);
        //user.getTasks().add(task);
        taskDAO.save(task);

        return true;

    }

    /**
     * Get all Tasks
     * @return all Tasks incl associated user
     */
    public List<Task> getTasks() {return  taskDAO.findAll();}

    /**
     *
     * @param id
     * @return task
     * @throws PrimaryIdNullOrEmptyException
     * @throws EntityNotFoundException
     */
    public Task getTaskById(Long id) throws PrimaryIdNullOrEmptyException, EntityNotFoundException {
        if (id == null) {
            throw new PrimaryIdNullOrEmptyException();
        }

        Optional<Task> taskOptional = taskDAO.findById(id);

        if (taskOptional.isPresent()) {
            return taskOptional.get();
        }
        throw new EntityNotFoundException();

    }

    /**
     * delete Task  by  Id
     * @param id
     * @return boolean
     * @throws PrimaryIdNullOrEmptyException
     * @throws EntityNotFoundException
     */

    public boolean deleteTask(Long id) throws PrimaryIdNullOrEmptyException, EntityNotFoundException {

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
            throw new EntityNotFoundException("could not found task with given id.", id);
        }

        try {
            task = this.taskDAO.findById(id).get();
        } catch (NoSuchElementException nsee) {
            return true;
        }

        return false;
    }




}
