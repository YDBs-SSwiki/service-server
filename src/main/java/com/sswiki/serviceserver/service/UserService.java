package com.sswiki.serviceserver.service;

import com.sswiki.serviceserver.dto.*;
import com.sswiki.serviceserver.entity.*;
import com.sswiki.serviceserver.repository.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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

    public UpdateUserResponseDTO updateUser(Integer userID, UpdateUserRequestDTO requestDTO) {
        // 1) userID로 User 엔티티 조회 (유효성 체크)
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다. userID=" + userID));

        if (Duration.between(user.getLastModifiedAt(), LocalDateTime.now()).compareTo(Duration.ofDays(30)) < 0) {
            throw new RuntimeException("마지막 변경일이 30일 이내입니다.");
        }

        // 2) User 엔티티의 userName 필드를 requestDTO의 userName으로 변경
        user.setUsername(requestDTO.getUsername());

        // 3) User 엔티티 저장
        userRepository.save(user);

        // 4) ResponseDTO 생성 후 반환
        return new UpdateUserResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getLastModifiedAt()
        );
    }

    public GetUserInfoResponseDTO getUserInfo(Integer userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다. userID=" + userID));

        return new GetUserInfoResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getRole(),
                user.getEmailAddress(),
                user.getLastModifiedAt(),
                user.getCreatedAt()
        );
    }

    public GetUserLikesResponseDTO getUserLikes(Integer userID) {
        // 1) userID로 User 엔티티 조회 (유효성 체크)
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다. userID=" + userID));

        // 2) ReviewLikes 테이블에서 userID에 해당하는 좋아요 목록 전부 가져오기
        List<ReviewLikes> likes = reviewLikesRepository.findByUserUserId(userID);

        // 3) Review -> LikesItemDTO 매핑
        List<LikesItemDTO> likesItems = likes.stream()
                .map(review -> {
                    LikesItemDTO item = new LikesItemDTO();
                    item.setReviewId(review.getReview().getReviewId());
                    return item;
                })
                .toList();

        // 4) GetUserLikesResponseDTO 구성
        GetUserLikesResponseDTO responseDTO = new GetUserLikesResponseDTO();
        responseDTO.setUserId(userID);
        responseDTO.setLikes(likesItems);

        return responseDTO;
    }
}
