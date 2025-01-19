package com.sswiki.serviceserver.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.sswiki.serviceserver.entity.User;
import com.sswiki.serviceserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
public class AuthService {
    // application.properties 또는 application.yml 등에 설정된 Google OAuth Client ID
    @Value("${google.client-id}")
    private String googleClientId;

    private final UserRepository userRepository;
    private final GoogleIdTokenVerifier verifier;

    public AuthService(UserRepository userRepository, GoogleIdTokenVerifier verifier) {
        this.userRepository = userRepository;
        this.verifier = verifier;
    }

    public User googleLogin(String idTokenString, String requestedUsername) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new RuntimeException("Invalid ID token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String googleKey = payload.getSubject();
            String email = payload.getEmail();
            // (원래 구글에서 name 가져오던 로직)
            // String googleName = (String) payload.get("name");

            // DB에서 googleKey로 사용자 조회
            Optional<User> foundUser = userRepository.findByGoogleKey(googleKey);

            if (foundUser.isEmpty()) {
                // 신규 가입
                User newUser = new User();
                newUser.setGoogleKey(googleKey);
                // [여기가 중요 포인트]
                // "클라이언트가 보낸 username"이 있다면 우선 사용
                // 만약 없거나 비어있다면, email 등 다른 값으로 대체
                if (requestedUsername != null && !requestedUsername.isBlank()) {
                    newUser.setUsername(requestedUsername);
                } else {
                    newUser.setUsername(email);  // fallback
                }

                newUser.setRole("USER");
                newUser.setEmailAddress(email);

                return userRepository.save(newUser);

            } else {
                // 이미 가입된 경우
                // 여기서 "requestedUsername"을 덮어쓸지 여부는 정책에 따라 다름.
                // 보통은 기존 username을 그대로 두거나, 사용자에게 '프로필 수정' 기능을 제공.
                return foundUser.get();
            }

        } catch (Exception e) {
            throw new RuntimeException("구글 토큰 검증 중 오류: " + e.getMessage(), e);
        }
    }
}
