package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class SetFavoriteResponseDTO {
    Integer userId;
    Integer breadId;
    boolean favoriteSet;
    LocalDate createdAt;
}
