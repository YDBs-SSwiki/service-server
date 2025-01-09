package com.sswiki.serviceserver.controller;

import com.sswiki.serviceserver.dto.GetAllBreadsResponseDTO;
import com.sswiki.serviceserver.entity.Bread;
import com.sswiki.serviceserver.service.BreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

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
            @RequestParam("image") MultipartFile imageFile
    ) throws Exception {
        // 빵 데이터와 이미지 파일을 처리하여 저장
        InputStream imageInputStream = imageFile.getInputStream();
        String contentType = imageFile.getContentType();
        return breadService.saveBreadWithImage(name, detail, price, count, imageInputStream, contentType);
    }

}
