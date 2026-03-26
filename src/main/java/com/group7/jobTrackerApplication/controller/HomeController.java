package com.group7.jobTrackerApplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling basic home and security test endpoints.
 *
 * @author Raphael Berjaoui
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestController
public class HomeController {

    /**
     * Returns a simple hello world message.
     *
     * @return a hello world string
     */
    @GetMapping("/")
    public String home(){
        return "Hello World";
    }

    /**
     * Returns a message confirming the user has access to a secured endpoint.
     *
     * @return a secured hello string
     */
    @GetMapping("/secured")
    public String secured(){
        return "Hello, Secured!";
    }
}