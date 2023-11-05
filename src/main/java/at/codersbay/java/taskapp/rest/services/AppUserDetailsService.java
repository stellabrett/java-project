package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.AppUserDAO;
import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.AppUser;
import at.codersbay.java.taskapp.rest.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
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

        if (username == null || username.trim().length() == 0) {
            throw new UsernameNotFoundException("given username was null or empty");
        }

        AppUser appUser = appUserDAO.findByUsername(username);

        if (appUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return appUser;
    }
}
