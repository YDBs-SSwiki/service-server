package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BreadSummaryResponseDTO {
    private int breadId;
    private String name;
    private String imageUrl; // 이미지 URL 필드 추가
    private String detail;

    public BreadSummaryResponseDTO(int breadId, String name, String imageUrl, String detail) {
        this.breadId = breadId;
        this.name = name;
        this.imageUrl = imageUrl; // 생성자에 이미지 URL 추가
        this.detail = detail;
    }
}
