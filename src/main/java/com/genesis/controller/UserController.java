package com.genesis.controller;

import com.genesis.model.UserEntity;
import com.genesis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST - vytvoření nového uživatele
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        UserEntity created = userService.createUser(user.getName(), user.getSurname(), user.getPersonId());
        return ResponseEntity.status(201).body(created);
    }

    // GET - načtení všech uživatelů (s volitelným ?detail=true)
    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) boolean detail) {
        List<UserEntity> users = userService.getAllUsers();

        if (detail) {
            // Vrátí všechny údaje včetně personID a uuid
            return ResponseEntity.ok(users);
        } else {
            // Vrátí pouze id, name, surname
            var simpleList = users.stream()
                    .map(u -> new UserSimpleDTO(u.getId(), u.getName(), u.getSurname()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(simpleList);
        }
    }

    // GET - načtení jednoho uživatele
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id,
                                         @RequestParam(required = false) boolean detail) {
        return userService.getUserById(id)
                .map(user -> {
                    if (detail) {
                        return ResponseEntity.ok(user);
                    } else {
                        return ResponseEntity.ok(new UserSimpleDTO(user.getId(), user.getName(), user.getSurname()));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT - úprava uživatele (ID z body)
    @PutMapping
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user) {
        UserEntity updated = userService.updateUser(user);
        return ResponseEntity.ok(updated);
    }

    // DELETE - smazání uživatele
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Vnitřní třída pro jednoduché DTO bez citlivých údajů
    private record UserSimpleDTO(Long id, String name, String surname) { }
}
