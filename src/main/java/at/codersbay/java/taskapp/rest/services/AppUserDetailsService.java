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
    AppUser user = appUserDAO.findByUsername(username);

        if (username == null || username.trim().length() == 0) {
            throw new UsernameNotFoundException("given username was null or empty");
        }

        AppUser appUser = appUserDAO.findByUsername(username);

        if (appUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserDetails userDetails = User.withUsername(user.getUsername())
                .password(user.getPassword())
               // .authorities(user.getRoles())
                .build();

        return userDetails;
    }
}
