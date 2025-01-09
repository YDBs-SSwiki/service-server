package com.sswiki.serviceserver.service;

import com.sswiki.serviceserver.entity.Bread;
import com.sswiki.serviceserver.dto.GetAllBreadsResponseDTO;
import com.sswiki.serviceserver.repository.BreadRepository;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BreadService {
    private final BreadRepository breadRepository;
    private final S3Service s3Service;

    public BreadService(BreadRepository breadRepository, S3Service s3Service) {
        this.breadRepository = breadRepository;
        this.s3Service = s3Service;
    }

    public GetAllBreadsResponseDTO getAllBreads() {
        List<GetAllBreadsResponseDTO.BreadDTO> breadDTOList = breadRepository.findAll()
                .stream()
                .map(bread -> new GetAllBreadsResponseDTO.BreadDTO(
                        bread.getBreadId(),
                        bread.getName(),
                        bread.getImageUrl(),
                        bread.getPrice()
                ))
                .collect(Collectors.toList());

        return new GetAllBreadsResponseDTO(breadDTOList);
    }

    // 이미지 파일 업로드 및 Bread 엔티티 저장
    public Bread saveBreadWithImage(String name, String detail, Integer price, Integer count,
                                    InputStream imageInputStream, String contentType) throws Exception {
        String fileName = name + ".jpg"; // 파일명 생성
        String imageUrl = s3Service.uploadFile(fileName, imageInputStream, contentType); // S3에 업로드

        // Bread 엔티티 생성
        Bread bread = new Bread();
        bread.setName(name);
        bread.setDetail(detail);
        bread.setPrice(price);
        bread.setCount(count);
        bread.setImageUrl(imageUrl);
        bread.setCreatedAt(LocalDateTime.now());
        bread.setUpdatedAt(LocalDateTime.now());

        // 데이터베이스에 저장
        return breadRepository.save(bread);
    }
}
