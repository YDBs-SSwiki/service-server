package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserRequestDTO {
    private String username;

    public UpdateUserRequestDTO(String username) {
        this.username = username;
    }
}
