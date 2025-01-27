package com.sswiki.serviceserver.service;

import com.sswiki.serviceserver.dto.*;
import com.sswiki.serviceserver.entity.*;
import com.sswiki.serviceserver.repository.BreadRepository;
import com.sswiki.serviceserver.repository.BreadToStoresRepository;
import com.sswiki.serviceserver.repository.ReviewRepository;
import com.sswiki.serviceserver.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BreadService {
    private static final Logger logger = LoggerFactory.getLogger(BreadService.class);
    private final BreadRepository breadRepository;
    private final S3Service s3Service;
    private final ReviewRepository reviewRepository;

    // 매장 및 중간 테이블 레포지토리 필요
    private final StoreRepository storeRepository;
    private final BreadToStoresRepository breadToStoresRepository;

    public BreadService(
            BreadRepository breadRepository,
            S3Service s3Service, ReviewRepository reviewRepository,
            StoreRepository storeRepository,
            BreadToStoresRepository breadToStoresRepository
    ) {
        this.breadRepository = breadRepository;
        this.s3Service = s3Service;
        this.reviewRepository = reviewRepository;
        this.storeRepository = storeRepository;
        this.breadToStoresRepository = breadToStoresRepository;
    }

    public GetAllBreadsResponseDTO getAllBreads() {
        logger.info("Get all breads");
        List<Bread> breads = breadRepository.findAll();

        // 각각의 Bread에 대해 store 정보까지 매핑
        List<GetAllBreadsResponseDTO.BreadDTO> breadDTOList = breads
                .stream()
                .map(bread -> {
                    GetAllBreadsResponseDTO.BreadDTO dto = new GetAllBreadsResponseDTO.BreadDTO(
                            bread.getBreadId(),
                            bread.getName(),
                            bread.getImageUrl(),
                            bread.getPrice()
                    );

                    // ★ 매장 정보 변환
                    List<GetAllBreadsResponseDTO.StoreDTO> storeDTOs = bread.getBreadToStores()
                            .stream()
                            .map(bts -> {
                                var store = bts.getStore();
                                return new GetAllBreadsResponseDTO.StoreDTO(
                                        store.getStoreId(),
                                        store.getStoreName(),
                                        store.getAddress(),
                                        store.getPhoneNumber()
                                );
                            })
                            .collect(Collectors.toList());

                    dto.setStores(storeDTOs);
                    return dto;
                })
                .collect(Collectors.toList());

        return new GetAllBreadsResponseDTO(breadDTOList);
    }

    /**
     * (기존) 단일 기능: 빵 + 이미지 등록
     */
    public Bread saveBreadWithImage(String name, String detail, Integer price, Integer count,
                                    InputStream imageInputStream, String contentType) throws Exception {
        // S3 업로드
        String fileName = name + ".jpg";
        String imageUrl = s3Service.uploadFile(fileName, imageInputStream, contentType);

        // Bread 엔티티 생성
        Bread bread = new Bread();
        bread.setName(name);
        bread.setDetail(detail);
        bread.setPrice(price);
        bread.setCount(count);
        bread.setImageUrl(imageUrl);
        bread.setCreatedAt(LocalDateTime.now());
        bread.setUpdatedAt(LocalDateTime.now());

        return breadRepository.save(bread);
    }

    /**
     * (신규) 빵 + 이미지 + 매장정보(storeIds) 등록
     */
    public Bread saveBreadWithImageAndStores(
            String name,
            String detail,
            Integer price,
            Integer count,
            InputStream imageInputStream,
            String contentType,
            List<Integer> storeIds  // ★ 추가
    ) throws Exception {

        // 1) 우선 빵 + 이미지 저장
        Bread bread = saveBreadWithImage(name, detail, price, count, imageInputStream, contentType);

        // 2) storeIds에 대해 Store 찾고 BreadToStores 매핑
        for (Integer storeId : storeIds) {
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

            BreadToStores breadToStores = new BreadToStores();
            breadToStores.setBread(bread);
            breadToStores.setStore(store);

            breadToStoresRepository.save(breadToStores);
        }

        return bread;
    }

    public BreadDetailResponseDTO getBread(Integer breadId) {
        Bread bread = breadRepository.findById(breadId)
                .orElseThrow(() -> new RuntimeException("Bread not found with id: " + breadId));

        // Bread에 대한 Store 정보 매핑
        List<BreadDetailResponseDTO.StoreDTO> storeDTOs = bread.getBreadToStores()
                .stream()
                .map(bts -> {
                    var store = bts.getStore();
                    return new BreadDetailResponseDTO.StoreDTO(
                            store.getStoreId(),
                            store.getStoreName(),
                            store.getAddress(),
                            store.getPhoneNumber()
                    );
                })
                .collect(Collectors.toList());

        return new BreadDetailResponseDTO(
                bread.getBreadId(),
                bread.getName(),
                bread.getDetail(),
                bread.getImageUrl(),
                bread.getPrice(),
                bread.getCount(),
                bread.getCreatedAt(),
                bread.getUpdatedAt(),
                storeDTOs
        );
    }

    public BreadSummaryResponseDTO getBreadSummary(Integer breadId) {
        Bread bread = breadRepository.findById(breadId)
                .orElseThrow(() -> new RuntimeException("Bread not found with id: " + breadId));

        return new BreadSummaryResponseDTO(
                bread.getBreadId(),
                bread.getName(),
                bread.getImageUrl(),
                bread.getDetail()
        );
    }

    public GetBreadReviewsResponseDTO getBreadReviews(Integer breadId) {
        List<Review> reviews = reviewRepository.findByBread_BreadId(breadId);

        List<GetBreadReviewsResponseDTO.ReviewDTO> reviewDTOList = reviews.stream()
                .map(review -> new GetBreadReviewsResponseDTO.ReviewDTO(
                        review.getReviewId(),
                        review.getUser().getUserId(),
                        review.getRating(),
                        review.getContent(),
                        review.getLikes(),
                        review.getCreatedAt()
                ))
                .toList();

        return new GetBreadReviewsResponseDTO(
                breadId,
                reviewDTOList
        );
    }

    public SearchBreadsResponseDTO searchBreads(String keyword) {
        List<Bread> foundBreads = breadRepository.findByNameContainingIgnoreCase(keyword);

        // 3) DTO 로 변환
        List<SearchBreadsResponseDTO.SearchResultDTO> resultDTOs = foundBreads.stream()
                .map(bread -> new SearchBreadsResponseDTO.SearchResultDTO(
                        bread.getBreadId(),
                        bread.getName()
                ))
                .collect(Collectors.toList());

        return new SearchBreadsResponseDTO(resultDTOs);
    }

    public UpdateBreadResponseDTO updateBread(Integer breadId, UpdateBreadRequestDTO requestDTO) {
        // 1. 기존 빵 엔티티 조회
        Bread bread = breadRepository.findById(breadId)
                .orElseThrow(() -> new RuntimeException("Bread not found with id: " + breadId));

        // 2. 빵 엔티티의 필드를 요청 DTO에 맞게 수정
        bread.setName(requestDTO.getName());
        bread.setDetail(requestDTO.getDetail());
        bread.setPrice(requestDTO.getPrice());
        bread.setCount(requestDTO.getCount());

        // 3. 스토어 연관관계(breadToStores) 수정
        //    (1) 기존 리스트 전체 제거 후 새로 세팅하는 가장 간단한 방법
        bread.getBreadToStores().clear();

        //    (2) 요청으로 들어온 storeIds를 이용해 다시 매핑
        if (requestDTO.getStoreIds() != null) {
            for (Integer storeId : requestDTO.getStoreIds()) {
                Store store = storeRepository.findById(storeId)
                        .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

                // BreadToStores 생성
                BreadToStores breadToStores = new BreadToStores();
                breadToStores.setBread(bread);
                breadToStores.setStore(store);
                breadToStores.setId(new BreadToStoresId(bread.getBreadId(), store.getStoreId()));

                // 양방향(혹은 단방향) 매핑 처리
                bread.getBreadToStores().add(breadToStores);
            }
        }

        // 4. 수정된 빵 엔티티 저장
        Bread updatedBread = breadRepository.save(bread);

        // 5. 응답 DTO 생성
        UpdateBreadResponseDTO response = new UpdateBreadResponseDTO();
        response.setBreadId(updatedBread.getBreadId());
        response.setName(updatedBread.getName());
        response.setDetail(updatedBread.getDetail());
        response.setPrice(updatedBread.getPrice());
        response.setCount(updatedBread.getCount());

        // storeIds는 매핑된 BreadToStores에서 추출
        List<Integer> storeIds = updatedBread.getBreadToStores().stream()
                .map(bts -> bts.getStore().getStoreId())
                .collect(Collectors.toList());
        response.setStoreIds(storeIds);

        // imageUrl, createdAt, updatedAt 등은 필요에 맞게 설정
        response.setImageUrl(updatedBread.getImageUrl());
        response.setCreatedAt(
                (updatedBread.getCreatedAt() != null) ? updatedBread.getCreatedAt().toString() : null
        );
        response.setUpdatedAt(
                (updatedBread.getUpdatedAt() != null) ? updatedBread.getUpdatedAt().toString() : null
        );

        return response;
    }
}
