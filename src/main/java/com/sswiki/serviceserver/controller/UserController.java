package com.sswiki.serviceserver.controller;

import com.sswiki.serviceserver.dto.BreadDetailResponseDTO;
import com.sswiki.serviceserver.dto.BreadSummaryResponseDTO;
import com.sswiki.serviceserver.dto.GetAllBreadsResponseDTO;
import com.sswiki.serviceserver.dto.GetUserReviewsResponseDTO;
import com.sswiki.serviceserver.entity.Bread;
import com.sswiki.serviceserver.entity.User;
import com.sswiki.serviceserver.service.BreadService;
import com.sswiki.serviceserver.service.UserService;
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

    // 유저의 리뷰 조회 API
    @GetMapping("/{userID}/reviews")
    public GetUserReviewsResponseDTO getUserReviews(@PathVariable Integer userID) {
        return userService.getUserReviews(userID);
    }
}