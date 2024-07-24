package com.example.spring_dynamodb_crud.controller;

import com.example.spring_dynamodb_crud.model.User;
import com.example.spring_dynamodb_crud.service.DynamoDBCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class DynamoDBCrudController {

    @Autowired
    private DynamoDBCrudService service;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        User userExists = service.readUser(user.getUserId());
        if(userExists != null)
            return ResponseEntity.ok("User Exists");
        String response = service.createUser(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> readUser(@PathVariable String userId) {
        User user = service.readUser(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        String response = service.updateUser(user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        String response = service.deleteUser(userId);
        return ResponseEntity.ok(response);
    }
}
