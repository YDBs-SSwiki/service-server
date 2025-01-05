package com.sswiki.serviceserver.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class BreadToStoresId implements Serializable {
    // Getter & Setter
    private Integer breadId;
    private Integer storeId;

    // 기본 생성자
    public BreadToStoresId() {}

    // 생성자
    public BreadToStoresId(Integer breadId, Integer storeId) {
        this.breadId = breadId;
        this.storeId = storeId;
    }

    // equals & hashCode (필수)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BreadToStoresId that = (BreadToStoresId) o;
        return Objects.equals(breadId, that.breadId) && Objects.equals(storeId, that.storeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(breadId, storeId);
    }
}
