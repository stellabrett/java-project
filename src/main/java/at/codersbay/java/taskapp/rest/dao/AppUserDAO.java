package at.codersbay.java.taskapp.rest.dao;

import at.codersbay.java.taskapp.rest.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserDAO extends JpaRepository<AppUser, Long> {

    @Query("SELECT appUser FROM AppUser appUser where appUser.username = :username")
    public AppUser findByUsername(String username);
}
