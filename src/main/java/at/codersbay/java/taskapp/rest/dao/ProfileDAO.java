package at.codersbay.java.taskapp.rest.dao;

import at.codersbay.java.taskapp.rest.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileDAO extends JpaRepository<Profile, Long> {
}
