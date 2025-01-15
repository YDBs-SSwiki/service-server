package com.sswiki.serviceserver.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateReviewLikeRequestDTO {
    private Long userId;
    private boolean like;
}
