package at.codersbay.java.taskapp.rest.dao;

import at.codersbay.java.taskapp.rest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {

}
