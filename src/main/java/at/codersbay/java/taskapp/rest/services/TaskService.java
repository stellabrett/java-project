package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.TaskDAO;
import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.Task;
import at.codersbay.java.taskapp.rest.entities.User;
import at.codersbay.java.taskapp.rest.exceptions.PrimaryIdNullOrEmptyException;
import at.codersbay.java.taskapp.rest.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

}
