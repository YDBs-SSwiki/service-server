package com.sswiki.serviceserver.service;

import com.sswiki.serviceserver.dto.GetUserReviewsResponseDTO;
import com.sswiki.serviceserver.entity.Review;
import com.sswiki.serviceserver.repository.BreadRepository;
import com.sswiki.serviceserver.repository.ReviewLikesRepository;
import com.sswiki.serviceserver.repository.ReviewRepository;
import com.sswiki.serviceserver.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final S3Service s3Service;
    private final ReviewRepository reviewRepository;
    private final ReviewLikesRepository reviewLikesRepository;
    private final BreadRepository breadRepository;
    private final UserRepository userRepository;

    public UserService(
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

    // GetUserReviewsResponseDTO 반환 메서드
    public GetUserReviewsResponseDTO getUserReviews(Integer userID) {
        // 1. 해당 유저의 리뷰 목록을 조회
        List<Review> reviews = reviewRepository.findByUser_UserId(userID);

        // 2. DTO 변환 작업
        List<GetUserReviewsResponseDTO.ReviewDTO> reviewDTOList = reviews.stream()
                .map(review -> new GetUserReviewsResponseDTO.ReviewDTO(
                        review.getReviewId(),
                        review.getBreadId(),
                        review.getRating(),
                        review.getContent(),
                        review.getCreatedAt()
                ))
                .toList();

        // 3. 최종 ResponseDTO 생성 후 반환
        return new GetUserReviewsResponseDTO(
                userID,
                reviewDTOList
        );
    }
}
