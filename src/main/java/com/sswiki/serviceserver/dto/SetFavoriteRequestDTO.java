package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SetFavoriteRequestDTO {
    Integer userId;
    Integer breadId;
}
