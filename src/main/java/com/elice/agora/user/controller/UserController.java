package com.elice.agora.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elice.agora.token.JwtUtil;
import com.elice.agora.token.TokenDto;
import com.elice.agora.user.dto.LoginDTO;
import com.elice.agora.user.dto.MeDTO;
import com.elice.agora.user.dto.SignUpDTO;
import com.elice.agora.user.entity.UserEntity;
import com.elice.agora.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    
    @GetMapping("/me")
    public ResponseEntity<Object> getMyInfo(HttpServletRequest request) {
        try {
            UserEntity user = jwtUtil.getUserFromToken(request);
            
            return ResponseEntity.ok(MeDTO.getFromUserEntity(user));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO signUpDto) {
        try {
            userService.signUp(signUpDto);

            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDto) {
        try {
            TokenDto token = userService.login(loginDto);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("authorization", "Bearer: " + token.accessToken);

            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("accessToken", "Bearer: " + token.accessToken);
            bodyMap.put("refreshToken", "Bearer: " + token.refreshToken);

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(toJsonString(bodyMap));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found: " + e.getMessage());
        }
    }

    private String toJsonString(Map<String, String> map) {
        // Convert the Map to JSON manually
        StringBuilder jsonBuilder = new StringBuilder("{");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            jsonBuilder.append('"').append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
        }
        if (!map.isEmpty()) {
            // Remove the trailing comma
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }
        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }
}
