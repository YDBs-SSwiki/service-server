package com.sswiki.serviceserver.controller;

import com.sswiki.serviceserver.entity.User;
import com.sswiki.serviceserver.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleAuth(@RequestBody Map<String, String> requestBody,
                                        HttpServletRequest servletRequest) {
        String idToken = requestBody.get("idToken");
        String requestedUsername = requestBody.get("username"); // 새로 추가

        if (idToken == null) {
            return ResponseEntity.badRequest().body("idToken is missing");
        }

        // 구글 토큰 검증 + DB 처리
        User user = authService.googleLogin(idToken, requestedUsername);

        // 세션 생성
        HttpSession session = servletRequest.getSession(true);
        session.setAttribute("loggedInUserId", user.getUserId());

        return ResponseEntity.ok(user);
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return ResponseEntity.ok("Successfully logged out");
    }
}
