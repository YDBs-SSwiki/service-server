package com.sswiki.serviceserver.controller;

import com.sswiki.serviceserver.dto.CreateReviewResponseDTO;
import com.sswiki.serviceserver.service.ReviewService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 리뷰 생성 API
     *
     * @param breadId   빵의 ID
     * @param userId    사용자의 ID
     * @param rating    평점 (1-5)
     * @param title     리뷰 제목
     * @param content   리뷰 내용
     * @param image 리뷰 이미지 파일
     * @return 생성된 리뷰 정보
     */
    @PostMapping("/createReview")
    public ResponseEntity<CreateReviewResponseDTO> createReview(
            @RequestParam("breadId") @NotNull Integer breadId,
            @RequestParam("userId") @NotNull Integer userId,
            @RequestParam("rating") @Min(1) @Max(5) Integer rating,
            @RequestParam("title") @NotBlank String title,
            @RequestParam("content") @NotBlank String content,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            CreateReviewResponseDTO responseDTO = reviewService.createReview(
                    breadId, userId, rating, title, content, image
            );
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
