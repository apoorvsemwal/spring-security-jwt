package com.test.springsecurityjwt.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

//It implements the Spring Security UserDetailsService interface.
//It overrides the loadUserByUsername for fetching user details from the database using the username.
//The Spring Security Authentication Manager calls this method for getting the user details from the database
//when authenticating the user details provided by the user.
//Here we are getting the user details from a hardcoded User List.
//Ideally here we should have retrieved the User Details from a Database.

//Also the password for a user is stored in encrypted format using BCrypt.
//Here using the Online Bcrypt Generator you can generate the Bcrypt for a password.

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("JwtTestEurekaUser".equals(username)) {
            //Here we can even create our custom User Class and use that instead of the std User Class, if we want to.
            return new User("JwtTestEurekaUser", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}