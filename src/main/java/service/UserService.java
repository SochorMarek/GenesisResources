package com.genesis.service;

import com.genesis.model.UserEntity;
import com.genesis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Vytvoření nového uživatele
    public UserEntity createUser(String name, String surname, String personId) {
        // kontrola duplicity personID
        if (userRepository.existsByPersonId(personId)) {
            throw new IllegalArgumentException("PersonID already exists!");
        }

        // vygenerování UUID
        String uuid = UUID.randomUUID().toString();

        UserEntity user = new UserEntity(name, surname, personId, uuid);
        return userRepository.save(user);
    }

    // ✅ Načtení všech uživatelů
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Načtení jednoho uživatele podle ID
    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // ✅ Úprava uživatele
    public UserEntity updateUser(Long id, String name, String surname) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setName(name);
        user.setSurname(surname);
        return userRepository.save(user);
    }

    // ✅ Smazání uživatele
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}