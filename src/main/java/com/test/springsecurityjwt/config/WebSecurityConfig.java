package com.test.springsecurityjwt.config;

import com.test.springsecurityjwt.filters.JwtRequestFilter;
import com.test.springsecurityjwt.utils.JwtAuthenticationEntryPointUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// It extends the WebSecurityConfigurerAdapter
// It is a convenience class that allows customization to both WebSecurity and HttpSecurity.
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPointUtil jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    // Here we configure our AuthenticationManager so that it knows how to load user and also tell it the
    // default password encoder and decoder. Here its - BCryptPasswordEncoder

    // The name of the configureGlobal method is not important. It could be anything.
    // However, it is important to only configure AuthenticationManagerBuilder in a class annotated
    // with either @EnableWebSecurity, @EnableGlobalMethodSecurity, or @EnableGlobalAuthentication.
    // Its like Autowiring at setter level - where this(like setter) method is called automatically with
    // AuthenticationManagerBuilder instance. And then we configure it inside here.

    // This same thing could have been done by doing an Override for configure(AuthenticationManagerBuilder auth)
    // The exact same way we are doing below for configure(HttpSecurity httpSecurity).
    // These are just different flavours of doing configurations.
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //We have it Autowired it in JwtAuthenticationController
    //Earlier versions of spring this was not required and AuthenticationManager Bean was available by default.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // We do not want authentication to happen for /authenticate API.
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // We don't need CSRF for this example
        httpSecurity.csrf().disable()
                // Dont authenticate this particular request
                .authorizeRequests().antMatchers("/authenticate").permitAll().
                // All other requests need to be authenticated
                        anyRequest().authenticated().and().
                // Next part is once after clients start sending JWT we want to tell spring security to not create the session
                // Make sure we use stateless session; session won't be used to
                // store user's state.
                        exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Since we told spring security to not create a session we need to tell it what to do for each incoming request
        // and how to setup the security context for each request.
        // Add a filter to validate the tokens with every request.
        // Filter added before UsernamePasswordAuthenticationFilter.
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
