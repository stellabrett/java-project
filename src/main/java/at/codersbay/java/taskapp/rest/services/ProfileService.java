package at.codersbay.java.taskapp.rest.services;

import at.codersbay.java.taskapp.rest.dao.ProfileDAO;
import at.codersbay.java.taskapp.rest.dao.UserDAO;
import at.codersbay.java.taskapp.rest.entities.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {


    @Autowired
    UserDAO userDAO;

    public ProfileService() {
    }




}

