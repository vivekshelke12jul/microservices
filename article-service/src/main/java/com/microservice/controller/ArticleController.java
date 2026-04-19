package com.microservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

    @GetMapping("/articles")
    public String getArticles(
            @RequestHeader("X-User-Name") String username,
            @RequestHeader("X-User-Roles") String roles
    ) {
        System.out.println("Username: " + username);
        System.out.println("Roles: " + roles);
        return "Articles";
    }
}
