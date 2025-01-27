package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetUserLikesResponseDTO {
    private Integer userId;
    private List<LikesItemDTO> likes;
}