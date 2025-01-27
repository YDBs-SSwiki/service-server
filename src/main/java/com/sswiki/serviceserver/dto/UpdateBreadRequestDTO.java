package com.sswiki.serviceserver.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UpdateBreadRequestDTO {
    private String name;
    private String detail;
    private Integer price;
    private Integer count;
    private List<Integer> storeIds;
}