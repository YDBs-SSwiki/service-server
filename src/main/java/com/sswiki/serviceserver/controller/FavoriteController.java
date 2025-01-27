package com.sswiki.serviceserver.controller;

import com.sswiki.serviceserver.dto.DeleteFavoriteRequestDTO;
import com.sswiki.serviceserver.dto.DeleteFavoriteResponseDTO;
import com.sswiki.serviceserver.dto.SetFavoriteRequestDTO;
import com.sswiki.serviceserver.dto.SetFavoriteResponseDTO;
import com.sswiki.serviceserver.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // POST /favorites
    @PostMapping
    public SetFavoriteResponseDTO addFavorite(@RequestBody SetFavoriteRequestDTO favoriteRequest) {
        return favoriteService.addFavorite(favoriteRequest);
    }

    @DeleteMapping("/delete")
    public DeleteFavoriteResponseDTO deleteFavorite(@RequestBody DeleteFavoriteRequestDTO requestDTO) {
        return favoriteService.deleteFavorite(requestDTO);
    }
}
