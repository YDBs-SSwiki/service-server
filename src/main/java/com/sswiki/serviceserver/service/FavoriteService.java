package com.sswiki.serviceserver.service;

import com.sswiki.serviceserver.dto.SetFavoriteRequestDTO;
import com.sswiki.serviceserver.dto.SetFavoriteResponseDTO;
import com.sswiki.serviceserver.entity.Bread;
import com.sswiki.serviceserver.entity.Favorites;
import com.sswiki.serviceserver.entity.User;
import com.sswiki.serviceserver.repository.BreadRepository;
import com.sswiki.serviceserver.repository.FavoriteRepository;
import com.sswiki.serviceserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final BreadRepository breadRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteService(
            BreadRepository breadRepository,
            UserRepository userRepository,
            FavoriteRepository favoriteRepository
    ) {
        this.breadRepository = breadRepository;
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public SetFavoriteResponseDTO addFavorite(SetFavoriteRequestDTO favoriteRequest) {
        // 1. User, Bread 엔티티 조회 (유효성 체크)
        //    - userId, breadId가 실제 DB에 존재하는지 확인
        User user = userRepository.findById(favoriteRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다. ID: " + favoriteRequest.getUserId()));

        Bread bread = breadRepository.findById(favoriteRequest.getBreadId())
                .orElseThrow(() -> new RuntimeException("해당 빵이 존재하지 않습니다. ID: " + favoriteRequest.getBreadId()));

        // 2. 중복 체크 (이미 찜 되어있는지)
        //    existsByUserAndBread, 또는 existsByUserUserIdAndBreadBreadId 처럼 구현 가능
        boolean alreadyFavorite = favoriteRepository.existsByUserAndBread(user, bread);
        if (alreadyFavorite) {
            // 이미 찜되어 있으면, 그냥 리턴하거나 예외를 던지는 등 정책에 따라 처리
            // 여기서는 "이미 찜되어 있음" 이라는 응답을 보내는 예시
            SetFavoriteResponseDTO response = new SetFavoriteResponseDTO();
            response.setUserId(user.getUserId());
            response.setBreadId(bread.getBreadId());
            response.setFavoriteSet(true); // 이미 true 상태
            response.setCreatedAt(null);   // 이미 있었으므로 createdAt은 따로 넘기지 않음 (원하면 가져올 수도 있음)
            return response;
        }

        // 3. Favorites 엔티티 생성 후 저장
        Favorites favorites = new Favorites();
        favorites.setUser(user);
        favorites.setBread(bread);
        // favorites.setCreatedAt(LocalDateTime.now()); // @PrePersist로 자동 처리됨

        favoriteRepository.save(favorites);

        // 4. 응답 DTO 구성
        SetFavoriteResponseDTO response = new SetFavoriteResponseDTO();
        response.setUserId(user.getUserId());
        response.setBreadId(bread.getBreadId());
        response.setFavoriteSet(true);
        // createdAt을 LocalDate가 아닌 LocalDateTime으로 변경하고 싶다면, DTO 타입 수정 필요
        response.setCreatedAt(java.time.LocalDate.now());
        return response;
    }
}
