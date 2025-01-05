package com.sswiki.serviceserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BreadToStores {
    @EmbeddedId
    private BreadToStoresId id = new BreadToStoresId();

    @ManyToOne
    @MapsId("breadId")
    @JoinColumn(name = "bread_id", nullable = false)
    private Bread bread;

    @ManyToOne
    @MapsId("storeId")
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}

