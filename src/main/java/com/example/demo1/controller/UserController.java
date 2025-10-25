package com.example.demo1.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo1.dto.UserRequest;
import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // private final UserRepository userRepository;
    @Autowired
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

     @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) String name) {
        List<User> users;
        if(name == null || name.isBlank()){
            users= userRepository.findAll();
        }else{
            users = userRepository.findByNameContainingIgnoreCase(name);
        }
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }
@Operation(summary = "Create a new user", description = "Adds a new user with name and email")
    @PostMapping
    // public ResponseEntity<User> createUser(@Valid @RequestParam(required = true) UserRequest request) {
        public ResponseEntity<User> createUser(@RequestParam String name, @RequestParam String email) {
        // Validate request
        UserRequest request = new UserRequest();
        request.setName(name);
        request.setEmail(email);

        // Convert DTO → Entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

@PatchMapping("/{id}")
@Operation(
    summary = "Partial update user",
    description = "Update name and/or email of an existing user"
)
@io.swagger.v3.oas.annotations.parameters.RequestBody(
    description = "Partial update for user (send only fields you want to change)",
    required = true,
    content = @Content(
        schema = @Schema(example = "{\"name\": \"John Updated\", \"email\": \"john.updated@example.com\"}")
    )
)
public ResponseEntity<User> updateUser(
        @PathVariable Long id,
        @org.springframework.web.bind.annotation.RequestBody Map<String, Object> updates) {

    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    User user = optionalUser.get();

    if (updates.containsKey("name")) {
        user.setName((String) updates.get("name"));
    }
    if (updates.containsKey("email")) {
        user.setEmail((String) updates.get("email"));
    }

    User updatedUser = userRepository.save(user);
    return ResponseEntity.ok(updatedUser);
}


    // DELETE user
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
  // DELETE user by name
@DeleteMapping("/name/{name}")
public ResponseEntity<String> deleteUserByName(@PathVariable String name) {
    Optional<User> user = userRepository.findByName(name);

    if (user.isEmpty()) {
        return ResponseEntity.status(404).body("User not found: " + name);
    }

    userRepository.delete(user.get());
    return ResponseEntity.ok("User deleted successfully: " + name);
}


   
}