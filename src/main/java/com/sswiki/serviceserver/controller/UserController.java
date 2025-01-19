package com.sswiki.serviceserver.controller;

import com.sswiki.serviceserver.dto.*;
import com.sswiki.serviceserver.entity.Bread;
import com.sswiki.serviceserver.entity.User;
import com.sswiki.serviceserver.service.BreadService;
import com.sswiki.serviceserver.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userID}")
    public GetUserInfoResponseDTO getUserInfo(@PathVariable Integer userID) {
        return userService.getUserInfo(userID);
    }

    // 유저의 리뷰 조회 API
    @GetMapping("/{userID}/reviews")
    public GetUserReviewsResponseDTO getUserReviews(@PathVariable Integer userID) {
        return userService.getUserReviews(userID);
    }

    @GetMapping("/{userID}/favorites")
    public List<GetUserFavoritesResponseDTO> getUserFavorites(@PathVariable Integer userID) {
        return userService.getUserFavorites(userID);
    }

    @PostMapping("/{userID}/update")
    public UpdateUserResponseDTO updateUser(@PathVariable Integer userID, @RequestBody UpdateUserRequestDTO requestDTO) {
        return userService.updateUser(userID, requestDTO);
    }
}