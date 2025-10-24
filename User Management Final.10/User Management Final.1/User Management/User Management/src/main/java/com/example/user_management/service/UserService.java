package com.example.user_management.service;

import com.example.user_management.domain.AppUser;
import com.example.user_management.domain.Role;
import com.example.user_management.repo.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUser> getRecentUsers() {
        return repository.findTop10ByOrderByIdDesc();
    }

    public Optional<AppUser> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<AppUser> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public AppUser create(String name, String email, String phone, String rawPassword, Role role) {
        AppUser u = new AppUser();
        u.setName(name);
        u.setEmail(email);
        u.setPhone(phone);
        u.setPasswordHash(passwordEncoder.encode(rawPassword));
        u.setRole(role == null ? Role.PASSENGER : role);
        return repository.save(u);
    }

    public AppUser updateRole(Long id, Role role) {
        AppUser u = repository.findById(id).orElseThrow();
        u.setRole(role);
        return repository.save(u);
    }

    public AppUser updateUser(Long id, String name, String email, String phone, Role role) {
        AppUser u = repository.findById(id).orElseThrow();
        if (name != null && !name.trim().isEmpty()) {
            u.setName(name.trim());
        }
        if (email != null && !email.trim().isEmpty()) {
            u.setEmail(email.trim());
        }
        if (phone != null) {
            u.setPhone(phone.trim().isEmpty() ? null : phone.trim());
        }
        if (role != null) {
            u.setRole(role);
        }
        return repository.save(u);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}


