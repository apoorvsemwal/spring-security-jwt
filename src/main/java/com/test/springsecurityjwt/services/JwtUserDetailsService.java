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
            // Here we can even create our custom User Class and use that instead of the std User Class, if we want to.
            // Here its the password Hash. Hashing is different from encoding.
            // The difference is that hashing is a one way function, where encryption is a two-way function.
            // when a user submits a password, you don't decrypt your stored hash,
            // instead you perform the same bcrypt operation on the user input and compare the hashes.
            // If they're identical, you accept the authentication.
            return new User("JwtTestEurekaUser", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}