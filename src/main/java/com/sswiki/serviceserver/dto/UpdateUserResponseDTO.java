package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateUserResponseDTO {
    private Integer userId;
    private String username;
    private LocalDateTime lastModifiedAt;

    public UpdateUserResponseDTO(Integer userId, String username, LocalDateTime lastModifiedAt) {
        this.userId = userId;
        this.username = username;
        this.lastModifiedAt = lastModifiedAt;
    }
}
