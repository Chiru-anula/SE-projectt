package com.example.user_management.web;

import com.example.user_management.domain.AppUser;
import com.example.user_management.domain.Role;
import com.example.user_management.service.UserService;
import com.example.user_management.web.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserUiController {

    private final UserService userService;

    public UserUiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("recentUsers", userService.getRecentUsers());
        model.addAttribute("active", "home");
        model.addAttribute("pageTitle", "Boat Safari - User Management");
        return "home";
    }

    @GetMapping("/ui")
    public String dashboard(Model model) {
        model.addAttribute("recentUsers", userService.getRecentUsers());
        model.addAttribute("active", "dashboard");
        model.addAttribute("pageTitle", "Dashboard");
        return "ui/dashboard";
    }

    @GetMapping("/ui/create")
    public String createForm(Model model) {
        model.addAttribute("active", "create");
        model.addAttribute("pageTitle", "Create User");
        return "ui/create-user";
    }

    @PostMapping("/ui/create")
    public String createSubmit(@RequestParam Map<String, String> form) {
        String name = form.get("name");
        String email = form.get("email");
        String phone = form.get("phone");
        String password = form.get("password");
        String confirm = form.get("confirmPassword");
        String roleStr = form.get("role");
        if (password == null || !password.equals(confirm)) {
            return "redirect:/ui/create";
        }
        Role role = roleStr == null ? Role.PASSENGER : Role.valueOf(roleStr);
        AppUser user = userService.create(name, email, phone, password, role);
        return "redirect:/ui/profile?q=" + user.getId();
    }

    @PostMapping("/api/users")
    @ResponseBody
    public ResponseEntity<UserDto> createUserApi(@RequestParam Map<String, String> form) {
        String name = form.get("name");
        String email = form.get("email");
        String phone = form.get("phone");
        String password = form.get("password");
        String confirm = form.get("confirmPassword");
        String roleStr = form.get("role");
        
        if (password == null || !password.equals(confirm)) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Role role = roleStr == null ? Role.PASSENGER : Role.valueOf(roleStr);
            AppUser user = userService.create(name, email, phone, password, role);
            return ResponseEntity.ok(UserDto.from(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<UserDto> updateUserApi(@PathVariable Long id, @RequestParam Map<String, String> form) {
        try {
            Optional<AppUser> existingUser = userService.findById(id);
            if (existingUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            String name = form.get("name");
            String email = form.get("email");
            String phone = form.get("phone");
            String roleStr = form.get("role");
            
            Role role = null;
            if (roleStr != null && !roleStr.trim().isEmpty()) {
                role = Role.valueOf(roleStr);
            }
            
            AppUser updatedUser = userService.updateUser(id, name, email, phone, role);
            return ResponseEntity.ok(UserDto.from(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/ui/profile")
    public String profile(@RequestParam(name = "q", required = false) String query, Model model) {
        Optional<AppUser> user = Optional.empty();
        if (query != null && !query.isBlank()) {
            try {
                user = userService.findById(Long.parseLong(query));
            } catch (NumberFormatException ignored) {
                user = userService.findByEmail(query.trim());
            }
        }
        model.addAttribute("user", user.orElse(null));
        model.addAttribute("active", "profile");
        model.addAttribute("pageTitle", "Profile");
        return "ui/profile";
    }

    @GetMapping("/ui/permissions")
    public String permissionsForm(@RequestParam(name = "id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id).orElse(null));
        model.addAttribute("active", "permissions");
        model.addAttribute("pageTitle", "Permissions");
        return "ui/permissions";
    }

    @PostMapping("/ui/permissions")
    public String permissionsSubmit(@RequestParam Map<String, String> form) {
        Long id = Long.valueOf(form.get("id"));
        Role role = Role.valueOf(form.get("role"));
        userService.updateRole(id, role);
        return "redirect:/ui/profile?q=" + id;
    }

    @GetMapping("/ui/delete")
    public String deleteConfirm(@RequestParam(name = "id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id).orElse(null));
        model.addAttribute("active", "delete");
        model.addAttribute("pageTitle", "Delete Account");
        return "ui/delete";
    }

    @PostMapping("/ui/delete")
    public String deleteSubmit(@RequestParam Map<String, String> form) {
        Long id = Long.valueOf(form.get("id"));
        userService.delete(id);
        return "redirect:/ui";
    }

    // JSON API endpoints
    @GetMapping("/api/users")
    @ResponseBody
    public ResponseEntity<List<UserDto>> getAllUsersApi() {
        List<AppUser> users = userService.getRecentUsers();
        List<UserDto> userDtos = users.stream()
                .map(UserDto::from)
                .toList();
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/api/users/search")
    @ResponseBody
    public ResponseEntity<UserDto> searchUserApi(@RequestParam(name = "q") String query) {
        Optional<AppUser> user = Optional.empty();
        if (query != null && !query.isBlank()) {
            try {
                user = userService.findById(Long.parseLong(query));
            } catch (NumberFormatException ignored) {
                user = userService.findByEmail(query.trim());
            }
        }
        return user.map(u -> ResponseEntity.ok(UserDto.from(u)))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


