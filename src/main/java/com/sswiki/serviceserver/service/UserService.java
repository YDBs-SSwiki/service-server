package com.sswiki.serviceserver.service;

import com.sswiki.serviceserver.dto.FavoriteItemDTO;
import com.sswiki.serviceserver.dto.GetUserFavoritesResponseDTO;
import com.sswiki.serviceserver.dto.GetUserReviewsResponseDTO;
import com.sswiki.serviceserver.entity.Bread;
import com.sswiki.serviceserver.entity.Favorites;
import com.sswiki.serviceserver.entity.Review;
import com.sswiki.serviceserver.entity.User;
import com.sswiki.serviceserver.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final S3Service s3Service;
    private final ReviewRepository reviewRepository;
    private final ReviewLikesRepository reviewLikesRepository;
    private final BreadRepository breadRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    public UserService(
            S3Service s3Service,
            ReviewRepository reviewRepository,
            ReviewLikesRepository reviewLikesRepository,
            BreadRepository breadRepository,
            UserRepository userRepository, FavoriteRepository favoriteRepository
    ) {
        this.s3Service = s3Service;
        this.reviewRepository = reviewRepository;
        this.reviewLikesRepository = reviewLikesRepository;
        this.breadRepository = breadRepository;
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
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

    public List<GetUserFavoritesResponseDTO> getUserFavorites(Integer userID) {
        // 1) userID로 User 엔티티 조회 (유효성 체크)
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다. userID=" + userID));

        // 2) Favorites 테이블에서 userID에 해당하는 찜 목록 전부 가져오기
        List<Favorites> favoritesList = favoriteRepository.findByUserUserId(userID);

        // 3) Favorites -> FavoriteItemDTO 매핑
        List<FavoriteItemDTO> favoriteItems = favoritesList.stream()
                .map(fav -> {
                    // fav.bread 로부터 breadId, name 등을 가져와 DTO로 만듦
                    FavoriteItemDTO item = new FavoriteItemDTO();
                    item.setBreadId(fav.getBread().getBreadId());
                    item.setName(fav.getBread().getName());
                    return item;
                })
                .toList();

        // 4) GetUserFavoritesResponseDTO 구성
        GetUserFavoritesResponseDTO responseDTO = new GetUserFavoritesResponseDTO();
        responseDTO.setUserId(userID);
        responseDTO.setFavorites(favoriteItems);

        return List.of(responseDTO);
    }
}
