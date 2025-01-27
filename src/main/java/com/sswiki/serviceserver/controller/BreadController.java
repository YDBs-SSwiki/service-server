package com.sswiki.serviceserver.controller;

import com.sswiki.serviceserver.dto.*;
import com.sswiki.serviceserver.entity.Bread;
import com.sswiki.serviceserver.service.BreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/bread")
public class BreadController {

    @Autowired
    private BreadService breadService;

    // 기존 /bread (GET): 모든 빵 데이터를 가져오기
    @GetMapping
    public GetAllBreadsResponseDTO getAllBreads() {
        return breadService.getAllBreads();
    }

    // 새로운 /bread/addBread (POST): 이미지를 포함한 빵 데이터 추가
    @PostMapping("/addBread")
    public Bread addBread(
            @RequestParam("name") String name,
            @RequestParam("detail") String detail,
            @RequestParam("price") Integer price,
            @RequestParam("count") Integer count,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("storeIds") List<Integer> storeIds

    ) throws Exception {
        // 빵 데이터와 이미지 파일을 처리하여 저장
        InputStream imageInputStream = imageFile.getInputStream();
        String contentType = imageFile.getContentType();

        return breadService.saveBreadWithImageAndStores(
                name, detail, price, count,
                imageInputStream, contentType,
                storeIds
        );
    }

    // 해당 breadId에 해당하는 빵 데이터 가져오기
    @GetMapping ("/{breadId}")
    public BreadDetailResponseDTO getBread(@PathVariable Integer breadId) {
        return breadService.getBread(breadId);
    }

    // 해당 breadId에 해당하는 빵 데이터 요약 정보 가져오기
    @GetMapping("/{breadId}/summary")
    public BreadSummaryResponseDTO getBreadSummary(@PathVariable Integer breadId) {
        return breadService.getBreadSummary(breadId);
    }

    // 해당 breadId에 해당하는 빵 리뷰 가져오기
    @GetMapping("/{breadId}/reviews")
    public GetBreadReviewsResponseDTO getBreadReviews(@PathVariable Integer breadId) {
        return breadService.getBreadReviews(breadId);
    }

    // keyword로 빵 검색하기
    @GetMapping("/search")
    public SearchBreadsResponseDTO searchBreads(@RequestParam("keyword") String keyword) {
        return breadService.searchBreads(keyword);
    }

    @PostMapping("/{breadId}/update")
    public UpdateBreadResponseDTO updateBread(@PathVariable Integer breadId, @RequestBody UpdateBreadRequestDTO requestDTO) {
        return breadService.updateBread(breadId, requestDTO);
    }
}
