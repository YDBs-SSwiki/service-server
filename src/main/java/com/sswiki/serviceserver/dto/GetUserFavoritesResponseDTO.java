package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

@Getter
@Setter
public class GetUserFavoritesResponseDTO {
    private Integer userId;
    private List<FavoriteItemDTO> favorites;
}