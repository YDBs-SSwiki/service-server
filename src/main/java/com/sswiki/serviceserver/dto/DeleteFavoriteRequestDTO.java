package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteFavoriteRequestDTO {
    private Integer userId;
    private Integer breadId;
}
