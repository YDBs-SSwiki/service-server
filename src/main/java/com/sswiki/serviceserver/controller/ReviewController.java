package com.sswiki.serviceserver.controller;

import com.sswiki.serviceserver.dto.*;
import com.sswiki.serviceserver.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(
        originPatterns = "http://localhost:52904", // 혹은 "https://example.com" 등
        allowCredentials = "true"
)
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

    @PostMapping("/{reviewId}/likes")
    public ResponseEntity<UpdateReviewLikeResponseDTO> toggleLike(
            @PathVariable Long reviewId,
            @RequestBody UpdateReviewLikeRequestDTO likeRequest
    ) {
        UpdateReviewLikeResponseDTO response = reviewService.toggleLike(reviewId, likeRequest.getUserId(), likeRequest.isLike());
        return ResponseEntity.ok(response);
    }


    /**
     * 리뷰 삭제 API
     * - 리뷰 작성자만 삭제 가능
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Integer reviewId,
            HttpSession session
    ) {
        // 1) 세션에서 로그인된 사용자 ID 가져오기
        Integer loggedInUserId = (Integer) session.getAttribute("loggedInUserId");
        if (loggedInUserId == null) {
            // 로그인하지 않았다면 401(Unauthorized) 응답
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        try {
            // 2) ReviewService를 통해 삭제 로직 처리
            reviewService.deleteReview(reviewId, loggedInUserId);
            // 3) 성공 시 200 OK
            return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
        } catch (RuntimeException e) {
            // 본인이 아닌데 삭제 시도 or 리뷰가 없음 등 예외 처리
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        } catch (Exception e) {
            // 그 외 에러
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 삭제 중 오류가 발생했습니다.");
        }
    }

    @PostMapping(
            value = "/{reviewId}/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> updateReview(
            @PathVariable Integer reviewId,
            @RequestPart("review") @Valid UpdateReviewRequestDTO requestDTO,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            HttpSession session
    ) {
        // 세션에서 로그인된 사용자 ID 가져오기
        Integer loggedInUserId = (Integer) session.getAttribute("loggedInUserId");
        if (loggedInUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        try {
            // 서비스 호출 시 imageFile도 함께 전달
            UpdateReviewResponseDTO responseDTO = reviewService.updateReview(
                    reviewId,
                    loggedInUserId,
                    requestDTO,
                    imageFile
            );

            return ResponseEntity.ok(responseDTO);

        } catch (RuntimeException e) {
            // 작성자 불일치, 리뷰 없음 등
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            // 그 외 오류
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 수정 중 오류가 발생했습니다.");
        }
    }
}
