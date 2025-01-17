package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetAllBreadsResponseDTO {

    private List<BreadDTO> breads;

    public GetAllBreadsResponseDTO(List<BreadDTO> breads) {
        this.breads = breads;
    }

    @Setter
    @Getter
    public static class BreadDTO {
        private int breadId;
        private String name;
        private String imageUrl; // 이미지 URL 필드 추가
        private int price;

        // 매장 정보 리스트
        private List<StoreDTO> stores;

        public BreadDTO(int breadId, String name, String imageUrl, int price) {
            this.breadId = breadId;
            this.name = name;
            this.imageUrl = imageUrl; // 생성자에 이미지 URL 추가
            this.price = price;
        }
    }

    // 매장 정보를 담을 DTO
    @Setter
    @Getter
    public static class StoreDTO {
        private int storeId;
        private String storeName;
        private String address;
        private String phoneNumber;

        public StoreDTO(int storeId, String storeName, String address, String phoneNumber) {
            this.storeId = storeId;
            this.storeName = storeName;
            this.address = address;
            this.phoneNumber = phoneNumber;
        }
    }
}
