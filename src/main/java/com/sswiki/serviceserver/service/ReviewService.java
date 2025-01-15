package com.sswiki.serviceserver.service;

import com.sswiki.serviceserver.dto.CreateReviewResponseDTO;
import com.sswiki.serviceserver.dto.UpdateReviewLikeResponseDTO;
import com.sswiki.serviceserver.entity.Bread;
import com.sswiki.serviceserver.entity.Review;
import com.sswiki.serviceserver.entity.ReviewLikes;
import com.sswiki.serviceserver.entity.User;
import com.sswiki.serviceserver.repository.BreadRepository;
import com.sswiki.serviceserver.repository.ReviewLikesRepository;
import com.sswiki.serviceserver.repository.ReviewRepository;
import com.sswiki.serviceserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    private final S3Service s3Service;
    private final ReviewRepository reviewRepository;
    private final ReviewLikesRepository reviewLikesRepository;
    private final BreadRepository breadRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(
            S3Service s3Service,
            ReviewRepository reviewRepository,
            ReviewLikesRepository reviewLikesRepository,
            BreadRepository breadRepository,
            UserRepository userRepository
    ) {
        this.s3Service = s3Service;
        this.reviewRepository = reviewRepository;
        this.reviewLikesRepository = reviewLikesRepository;
        this.breadRepository = breadRepository;
        this.userRepository = userRepository;
    }

    /**
     * 리뷰 생성 메서드
     *
     * @param breadId   빵의 ID
     * @param userId    사용자의 ID
     * @param rating    평점
     * @param title     리뷰 제목
     * @param content   리뷰 내용
     * @param imageFile 리뷰 이미지 파일
     * @return 생성된 리뷰의 응답 DTO
     * @throws Exception 이미지 업로드 실패 시 예외 발생
     */
    @Transactional
    public CreateReviewResponseDTO createReview(Integer breadId, Integer userId, Integer rating, String title, String content, MultipartFile imageFile) throws Exception {
        // 빵 엔티티 조회
        Optional<Bread> optionalBread = breadRepository.findById(breadId);
        if (optionalBread.isEmpty()) {
            throw new Exception("Bread not found with id: " + breadId);
        }
        Bread bread = optionalBread.get();

        // 사용자 엔티티 조회
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found with id: " + userId);
        }
        User user = optionalUser.get();

        // 이미지 업로드 처리
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = "review_" + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            imageUrl = s3Service.uploadFile(fileName, imageFile.getInputStream(), imageFile.getContentType());
        }

        logger.info("Review image uploaded: {}", imageUrl);

        // 리뷰 엔티티 생성
        Review review = new Review();
        review.setBread(bread);
        review.setUser(user);
        review.setRating(rating);
        review.setTitle(title);
        review.setContent(content);
        review.setImageUrl(imageUrl);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        // 리뷰 저장
        Review savedReview = reviewRepository.save(review);

        // 응답 DTO 생성
        return new CreateReviewResponseDTO(
                savedReview.getReviewId(),
                savedReview.getBread().getBreadId(),
                savedReview.getUser().getUserId(),
                savedReview.getRating(),
                savedReview.getTitle(),
                savedReview.getContent(),
                savedReview.getImageUrl(),
                savedReview.getCreatedAt().toLocalDate()
        );
    }

    @Transactional
    public UpdateReviewLikeResponseDTO toggleLike(Long reviewId, Long userId, boolean like) {
        // 1) 필요한 엔티티를 미리 조회 (유효성 체크 겸)
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));
        Review review = reviewRepository.findById(Math.toIntExact(reviewId))
                .orElseThrow(() -> new RuntimeException("해당 리뷰가 없습니다."));

        // 2) 좋아요 여부에 따라 Insert 혹은 Delete
        boolean alreadyLiked = reviewLikesRepository.existsByReviewReviewIdAndUserUserId(reviewId, userId);
        if (like) {
            if (!alreadyLiked) {
                ReviewLikes newLike = new ReviewLikes(user, review);
                reviewLikesRepository.save(newLike);
                // 리뷰 likes 컬럼 증가
                reviewRepository.increaseLikes(reviewId);
            }
        } else {
            if (alreadyLiked) {
                reviewLikesRepository.deleteByReviewReviewIdAndUserUserId(reviewId, userId);
                reviewRepository.decreaseLikes(reviewId);
            }
        }

        // 3) 최종 Like 개수
        int totalLikes = reviewRepository.findById(Math.toIntExact(reviewId))
                .map(Review::getLikes)
                .orElse(0);

        // 4) 응답 DTO
        return new UpdateReviewLikeResponseDTO(reviewId, userId, like, totalLikes);
    }

}
