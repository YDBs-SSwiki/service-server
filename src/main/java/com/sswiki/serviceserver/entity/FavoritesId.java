package com.sswiki.serviceserver.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class FavoritesId implements Serializable {
    // Getter & Setter
    private Integer userId;
    private Integer breadId;

    // 기본 생성자
    public FavoritesId() {}

    // 생성자
    public FavoritesId(Integer userId, Integer breadId) {
        this.userId = userId;
        this.breadId = breadId;
    }

    // equals & hashCode (필수)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoritesId that = (FavoritesId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(breadId, that.breadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, breadId);
    }
}
