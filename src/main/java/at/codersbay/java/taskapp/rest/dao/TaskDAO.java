package at.codersbay.java.taskapp.rest.dao;

import at.codersbay.java.taskapp.rest.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDAO extends JpaRepository<Task, Long> {
}
