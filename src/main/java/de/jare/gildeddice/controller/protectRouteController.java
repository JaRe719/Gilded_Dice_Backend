package de.jare.gildeddice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/protectRoute")
public class protectRouteController {

    @GetMapping()
    public ResponseEntity<String> getUsername(Authentication authentication){
        return ResponseEntity.ok(authentication.getName());
    }
}
