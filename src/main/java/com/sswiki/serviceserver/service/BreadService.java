package com.sswiki.serviceserver.service;

import com.sswiki.serviceserver.entity.Bread;
import com.sswiki.serviceserver.dto.GetAllBreadsResponseDTO;
import com.sswiki.serviceserver.repository.BreadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BreadService {
    private final BreadRepository breadRepository;

    public BreadService(BreadRepository breadRepository) {
        this.breadRepository = breadRepository;
    }

    public GetAllBreadsResponseDTO getAllBreads() {
        List<GetAllBreadsResponseDTO.BreadDTO> breadDTOList = breadRepository.findAll()
                .stream()
                .map(bread -> new GetAllBreadsResponseDTO.BreadDTO(
                        bread.getBreadId(),
                        bread.getName(),
                        bread.getPrice()
                ))
                .collect(Collectors.toList());

        return new GetAllBreadsResponseDTO(breadDTOList);
    }
}
