package com.bank.api.techtask.controller;

import com.bank.api.techtask.domain.dto.validation.EmailDTO;
import com.bank.api.techtask.domain.dto.validation.PhoneNumberDTO;
import com.bank.api.techtask.domain.model.User;
import com.bank.api.techtask.service.AuthenticationService;
import com.bank.api.techtask.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Controller for managing users.
 */

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public UserController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     * Deletes a user by ID.
     *
     * @param userIdToDelete the ID of the user to delete
     * @return a response entity with a success message
     */
    @DeleteMapping("/delete/{userIdToDelete}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long userIdToDelete) {
        authenticationService.deleteUserById(userIdToDelete);
        return ResponseEntity.ok("User deleted successfully");
    }

    /**
     * Updates a user by ID.
     *
     * @param userIdToUpdate the ID of the user to update
     * @param updatedUser    the updated user data
     * @return a response entity with a success message
     */
    @PutMapping("/update/{userIdToUpdate}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(@PathVariable Long userIdToUpdate,
                                             @Valid @RequestBody User updatedUser) {
        authenticationService.updateUser(userIdToUpdate, updatedUser);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/delete/email")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteUserEmail() {
        userService.deleteUserEmail();
        return ResponseEntity.ok("Email deleted successfully");
    }

    @DeleteMapping("/delete/phone-number")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteUserPhoneNumber() {
        userService.deleteUserPhoneNumber();
        return ResponseEntity.ok("Phone number deleted successfully");
    }

    @PatchMapping("/update/phone-number")
    public ResponseEntity<String> updateUserPhoneNumber(@Valid @RequestBody PhoneNumberDTO phoneNumberDTO) {
        userService.updatePhoneNumber(phoneNumberDTO.getPhoneNumber());
        return ResponseEntity.ok("Phone number updated successfully");
    }

    @PatchMapping("/update/email")
    public ResponseEntity<String> updateUserEmail(@Valid @RequestBody EmailDTO emailDTO) {
        userService.updateEmail(emailDTO.getEmail());
        return ResponseEntity.ok("Email updated successfully");
    }

    @PostMapping("/add/email")
    public ResponseEntity<String> addEmail(@RequestParam Long userId, @RequestParam String email) {
        userService.addEmail(userId, email);
        return ResponseEntity.ok("Email added successfully");
    }

    @PostMapping("/add/phone-number")
    public ResponseEntity<String> addPhoneNumber(@RequestParam Long userId, @RequestParam String phoneNumber) {
        userService.addPhoneNumber(userId, phoneNumber);
        return ResponseEntity.ok("Phone number added successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfBirth,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email) {
        List<User> users = userService.getAllUsers(dateOfBirth, phoneNumber, fullName, email);
        return ResponseEntity.ok(users);
    }

}
