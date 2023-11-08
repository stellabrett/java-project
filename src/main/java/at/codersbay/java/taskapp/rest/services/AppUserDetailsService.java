package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.AppUserDAO;
import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserDAO appUserDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(username == null || username.trim().length() == 0) {
            throw new UsernameNotFoundException("given username was null or empty");
        }

        try {
            System.out.println(username);
            return this.appUserDAO.findByUsername(username);

        } catch(Throwable t) {
            throw new UsernameNotFoundException("something went wrong, sorry!");
        }
    }
}
