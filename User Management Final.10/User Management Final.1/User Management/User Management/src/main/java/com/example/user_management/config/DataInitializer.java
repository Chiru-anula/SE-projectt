package com.example.user_management.config;

import com.example.user_management.domain.Role;
import com.example.user_management.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if users already exist to avoid duplicates
        if (userService.getRecentUsers().isEmpty()) {
            // Create sample users
            userService.create("Captain James Rodriguez", "james@boatsafari.com", "+94 77 123 4567", "password123", Role.ADMIN);
            userService.create("Sarah Chen", "sarah@boatsafari.com", "+94 77 234 5678", "password123", Role.GUIDE);
            userService.create("Mike Thompson", "mike@boatsafari.com", "+94 77 345 6789", "password123", Role.GUIDE);
            userService.create("Emma Wilson", "emma@boatsafari.com", "+94 77 456 7890", "password123", Role.PASSENGER);
            
            System.out.println("Sample data initialized: 4 users created");
        } else {
            System.out.println("Database already contains users, skipping initialization");
        }
    }
}