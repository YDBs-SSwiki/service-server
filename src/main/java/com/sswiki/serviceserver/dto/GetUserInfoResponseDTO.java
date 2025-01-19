package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetUserInfoResponseDTO {
    private Integer userId;
    private String username;
    private String role;
    private String emailAddress;
    private LocalDateTime lastModifiedAt;
    private LocalDateTime createdAt;

    public GetUserInfoResponseDTO(Integer userId, String username, String role, String emailAddress, LocalDateTime lastModifiedAt, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.emailAddress = emailAddress;
        this.lastModifiedAt = lastModifiedAt;
        this.createdAt = createdAt;
    }
}
