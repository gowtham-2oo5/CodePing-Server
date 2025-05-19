package com.codeping.server.coreserver.controllers;

import com.codeping.server.coreserver.dto.CPUserDTO;
import com.codeping.server.coreserver.services.CPUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class CPUsersController {

        private final CPUsersService usersService;

        @PostMapping
        @Operation(summary = "Create a new user", description = "Creates a new user with the provided details")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "User created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input"),
                        @ApiResponse(responseCode = "409", description = "User already exists")
        })
        public ResponseEntity<CPUserDTO> createUser(
                        @RequestBody CPUserDTO userDTO) {
                log.info("Creating new user with email: {}", userDTO.getEmail());
                CPUserDTO createdUser = usersService.createUser(userDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update an existing user", description = "Updates the details of an existing user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User updated successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        public ResponseEntity<CPUserDTO> updateUser(
                        @PathVariable UUID id,
                        @RequestBody CPUserDTO userDTO) {
                log.info("Updating user with ID: {}", id);
                CPUserDTO updatedUser = usersService.updateUser(id, userDTO);
                return ResponseEntity.ok(updatedUser);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete a user", description = "Deletes a user by their ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        public ResponseEntity<Void> deleteUser(
                        @PathVariable UUID id) {
                log.info("Deleting user with ID: {}", id);
                usersService.deleteUser(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get user by ID", description = "Retrieves a user's details by their ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User found"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        public ResponseEntity<CPUserDTO> getUserById(
                        @PathVariable UUID id) {
                log.info("Fetching user with ID: {}", id);
                CPUserDTO user = usersService.getUserById(id);
                return ResponseEntity.ok(user);
        }

        @GetMapping
        @Operation(summary = "Get all users", description = "Retrieves a list of all users")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
        })
        public ResponseEntity<List<CPUserDTO>> getAllUsers() {
                log.info("Fetching all users");
                List<CPUserDTO> users = usersService.getAllUsers();
                return ResponseEntity.ok(users);
        }

        @GetMapping("/email/{email}")
        @Operation(summary = "Get user by email", description = "Retrieves a user's details by their email")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User found"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        public ResponseEntity<CPUserDTO> getUserByEmail(
                        @PathVariable String email) {
                log.info("Fetching user with email: {}", email);
                CPUserDTO user = usersService.getUserByEmail(email);
                return ResponseEntity.ok(user);
        }

        @GetMapping("/firebase/{firebaseUid}")
        @Operation(summary = "Get user by Firebase UID", description = "Retrieves a user's details by their Firebase UID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User found"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        public ResponseEntity<CPUserDTO> getUserByFirebaseUid(
                        @PathVariable String firebaseUid) {
                log.info("Fetching user with Firebase UID: {}", firebaseUid);
                CPUserDTO user = usersService.getUserByFirebaseUid(firebaseUid);
                return ResponseEntity.ok(user);
        }
}