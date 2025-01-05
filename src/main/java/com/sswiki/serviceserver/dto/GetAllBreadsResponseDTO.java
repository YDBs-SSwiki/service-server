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
        private int price;

        public BreadDTO(int breadId, String name, int price) {
            this.breadId = breadId;
            this.name = name;
            this.price = price;
        }

    }
}
