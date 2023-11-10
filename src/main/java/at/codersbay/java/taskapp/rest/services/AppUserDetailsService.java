package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.AppUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@Component
public class AppUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AppUserDAO.class);

    @Autowired
    private AppUserDAO appUserDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Searching for AppUser with username: {}", username);

        if(username == null || username.trim().length() == 0) {
            throw new UsernameNotFoundException("given username was null or empty");
        }

        try {
            System.out.println(username);
            logger.info("Found AppUser: {}", username);
            return this.appUserDAO.findByUsername(username);

        } catch(Throwable t) {
            throw new UsernameNotFoundException("something went wrong, sorry!");
        }
    }
}
