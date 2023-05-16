package com.example.firebase.controller;

import com.example.firebase.util.Connection;
import com.example.firebase.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/simpleCrud")
public class UserController {

    @Autowired
    Connection connection;

    @PostMapping("/create")
    public void createNewUser(@RequestBody User user) throws InterruptedException, ExecutionException{
        connection.createNewUser(user);
    }

    @GetMapping("/return/{key}")
    public User retrieveData(@PathVariable String key) {
        return connection.returnUser(key);
    }

    @DeleteMapping("/delete/{key}")
    public void deleteUser(String key){
        connection.deleteUser(key);
    }

}
