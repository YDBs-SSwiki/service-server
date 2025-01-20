package com.sswiki.serviceserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SearchBreadsResponseDTO {

    private List<SearchResultDTO> searchResults;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SearchResultDTO {
        private Integer breadId;
        private String name;
    }
}
