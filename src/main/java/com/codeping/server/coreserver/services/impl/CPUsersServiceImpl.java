package com.codeping.server.coreserver.services.impl;

import com.codeping.server.coreserver.dto.CPUserDTO;
import com.codeping.server.coreserver.models.CPUsers;
import com.codeping.server.coreserver.repos.CPUsersRepository;
import com.codeping.server.coreserver.services.CPUsersService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CPUsersServiceImpl implements CPUsersService {

    private final CPUsersRepository usersRepository;

    @Override
    @Transactional
    public CPUserDTO createUser(CPUserDTO userDTO) {
        log.info("Creating new user with email: {}", userDTO.getEmail());
        CPUsers user = userDTO.toEntity();
        CPUsers savedUser = usersRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        return CPUserDTO.fromEntity(savedUser);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public CPUserDTO updateUser(UUID id, CPUserDTO userDTO) {
        log.info("Updating user with ID: {}", id);
        CPUsers existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        // Update fields
        existingUser.setName(userDTO.getName());
        existingUser.setShortBio(userDTO.getShortBio());
        existingUser.setLinkedinUrl(userDTO.getLinkedinUrl());
        existingUser.setGithubUrl(userDTO.getGithubUrl());
        existingUser.setResumeUrl(userDTO.getResumeUrl());
        existingUser.setProfilePicUrl(userDTO.getProfilePicUrl());

        CPUsers updatedUser = usersRepository.save(existingUser);
        log.info("User updated successfully with ID: {}", id);
        return CPUserDTO.fromEntity(updatedUser);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(UUID id) {
        log.info("Deleting user with ID: {}", id);
        if (!usersRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        usersRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public CPUserDTO getUserById(UUID id) {
        log.info("Fetching user with ID: {}", id);
        CPUsers user = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        return CPUserDTO.fromEntity(user);
    }

    @Override
    @Cacheable(value = "users", key = "'all'")
    public List<CPUserDTO> getAllUsers() {
        log.info("Fetching all users");
        return usersRepository.findAll().stream()
                .map(CPUserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public CPUserDTO getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        CPUsers user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        return CPUserDTO.fromEntity(user);
    }

    @Override
    @Cacheable(value = "users", key = "#firebaseUid")
    public CPUserDTO getUserByFirebaseUid(String firebaseUid) {
        log.info("Fetching user with Firebase UID: {}", firebaseUid);
        CPUsers user = usersRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with Firebase UID: " + firebaseUid));
        return CPUserDTO.fromEntity(user);
    }
}