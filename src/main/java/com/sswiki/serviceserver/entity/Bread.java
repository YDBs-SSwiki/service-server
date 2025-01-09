package com.sswiki.serviceserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer breadId;

    private String name;
    private String detail;
    private Integer price;
    private Integer count;

    private String imageUrl; // 이미지 파일 경로 필드 추가

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "bread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BreadToStores> breadToStores = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
