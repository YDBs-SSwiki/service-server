package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateBreadResponseDTO {
    private Integer breadId;
    private String name;
    private String detail;
    private Integer price;
    private Integer count;
    private List<Integer> storeIds;  // 스토어 ID 목록
    private String imageUrl;
    private String createdAt;
    private String updatedAt;
}
