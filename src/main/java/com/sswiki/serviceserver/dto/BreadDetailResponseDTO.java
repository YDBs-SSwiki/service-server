package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class BreadDetailResponseDTO {
        private int breadId;
        private String name;
        private String detail;
        private String imageUrl;
        private int price;
        private int count;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        // 매장 정보 리스트
        private List<StoreDTO> stores;

        public BreadDetailResponseDTO(int breadId, String name, String detail, String imageUrl, int price, int count, LocalDateTime createdAt, LocalDateTime updatedAt, List<StoreDTO> stores) {
            this.breadId = breadId;
            this.name = name;
            this.detail = detail;
            this.imageUrl = imageUrl; // 생성자에 이미지 URL 추가
            this.price = price;
            this.count = count;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.stores = stores;
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
