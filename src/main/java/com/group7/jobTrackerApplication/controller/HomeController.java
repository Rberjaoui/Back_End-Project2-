package com.group7.jobTrackerApplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for basic application health and test endpoints.
 *
 * <p>This controller provides simple endpoints used to verify that the
 * application is running and that secured routes are accessible.</p>
 *
 * @author Team 7
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestController
public class HomeController {

    /**
     * Returns a basic public response indicating the application is running.
     *
     * @return a simple greeting string
     */
    @GetMapping("/")
    public String home(){
        return "Hello World";
    }

    /**
     * Returns a response for a secured endpoint.
     *
     * <p>This endpoint is typically used to verify that authentication and
     * authorization are working correctly.</p>
     *
     * @return a string indicating access to the secured route
     */
    @GetMapping("/secured")
    public String secured(){
        return "Hello, Secured!";
    }
}