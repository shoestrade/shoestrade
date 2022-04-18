package com.study.shoestrade.domain.product;

import com.study.shoestrade.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;

    private String korName;

    private String engName;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<Product> productList = new LinkedList<>();

    public void changeBrandName(String korName, String engName) {
        this.korName = korName;
        this.engName = engName;
    }

}
