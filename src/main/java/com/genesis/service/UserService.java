package com.genesis.service;

import com.genesis.model.UserEntity;
import com.genesis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Set<String> validPersonIds;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.validPersonIds = loadValidPersonIds();
    }

    // Načtení personId z resources/dataPersonId.txt
    private Set<String> loadValidPersonIds() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("dataPersonId.txt"))
                )
        )) {
            return reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Nepodařilo se načíst dataPersonId.txt", e);
        }
    }

    // Vytvoření nového uživatele
    public UserEntity createUser(String name, String surname, String personId) {
        // kontrola, zda personID existuje v seznamu
        if (!validPersonIds.contains(personId)) {
            throw new IllegalArgumentException("Zadané personID není platné!");
        }

        // kontrola, jestli už není použito v DB
        if (userRepository.existsByPersonId(personId)) {
            throw new IllegalArgumentException("PersonID již existuje v databázi!");
        }

        // vygenerování UUID
        String uuid = UUID.randomUUID().toString();

        UserEntity user = new UserEntity(name, surname, personId, uuid);
        return userRepository.save(user);
    }

    // Vrátí všechny uživatele
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Vrátí jednoho uživatele podle ID
    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Úprava uživatele – ID se čte z body požadavku
    public UserEntity updateUser(UserEntity userData) {
        UserEntity user = userRepository.findById(userData.getId())
                .orElseThrow(() -> new IllegalArgumentException("Uživatel nenalezen!"));

        user.setName(userData.getName());
        user.setSurname(userData.getSurname());

        return userRepository.save(user);
    }

    // Smazání uživatele
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Uživatel neexistuje!");
        }
        userRepository.deleteById(id);
    }
}
