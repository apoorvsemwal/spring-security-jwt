package com.test.springsecurityjwt.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<String> firstPage() {
        return new ResponseEntity<>("Hello this API was called after authentication.", HttpStatus.OK);
    }

}
