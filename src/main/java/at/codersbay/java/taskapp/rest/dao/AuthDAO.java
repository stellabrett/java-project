package at.codersbay.java.taskapp.rest.dao;

import at.codersbay.java.taskapp.rest.entities.GrantedAuthorityImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;

public interface AuthDAO extends JpaRepository <GrantedAuthorityImpl, Long> {

}
