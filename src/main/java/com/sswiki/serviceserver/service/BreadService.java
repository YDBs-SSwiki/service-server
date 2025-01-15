package com.sswiki.serviceserver.service;

import com.sswiki.serviceserver.dto.*;
import com.sswiki.serviceserver.entity.Bread;
import com.sswiki.serviceserver.entity.BreadToStores;
import com.sswiki.serviceserver.entity.Review;
import com.sswiki.serviceserver.entity.Store;
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

}
