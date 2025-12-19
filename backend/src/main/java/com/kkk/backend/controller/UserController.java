package com.kkk.backend.controller;

import com.kkk.backend.dto.RegisterRequest;
import com.kkk.backend.entity.User;
import com.kkk.backend.repository.UserRepository;
import com.kkk.backend.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) return "用户名或密码不能为空";
        if (userRepository.findByUsername(request.getUsername()).isPresent()) return "用户名已存在";

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 加密
        user.setContact(request.getContact());
        user.setRole("user");
        user.setCreateTime(LocalDateTime.now());

        userRepository.save(user);
        return "注册成功";
    }

    @PostMapping("/login")
    public Object login(@RequestBody RegisterRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "用户名或密码不能为空");
            return error;
        }

        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "用户名不存在");
            return error;
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "密码错误");
            return error;
        }

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("username", user.getUsername());
        result.put("role", user.getRole());
        return result;
    }

}


