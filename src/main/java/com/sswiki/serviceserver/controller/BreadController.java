package com.sswiki.serviceserver.controller;

import com.sswiki.serviceserver.dto.GetAllBreadsResponseDTO;
import com.sswiki.serviceserver.service.BreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bread")
public class BreadController {

    @Autowired
    private BreadService breadService;

    @GetMapping
    public GetAllBreadsResponseDTO getAllBreads() {
        return breadService.getAllBreads();
    }
}
