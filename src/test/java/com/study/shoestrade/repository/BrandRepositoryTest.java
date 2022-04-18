package com.study.shoestrade.repository;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.dto.brand.BrandDto;
import com.study.shoestrade.repository.brand.BrandRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    Brand brand1 = Brand.builder()
            .korName("나이키")
            .engName("a")
            .build();

    Brand brand2 = Brand.builder()
            .korName("아디다스")
            .engName("b")
            .build();

    Brand brand3 = Brand.builder()
            .korName("뉴발란스")
            .engName("c")
            .build();


    @Test
    @DisplayName("브랜드_저장_테스트")
    public void 브랜드_저장() {
        // given

        // when
        brandRepository.save(brand1);

        // then
        assertThat(brandRepository.findById(brand1.getId()).orElse(null)).isEqualTo(brand1);
    }


    @Test
    @DisplayName("브랜드_이름_수정_테스트")
    public void 브랜드_이름_수정() {
        // given
        brandRepository.save(brand1);
        Brand findBrand = brandRepository.findById(brand1.getId()).orElse(null);

        // when
        findBrand.changeBrandName("언더아머", "a");

        brandRepository.flush();

        findBrand = brandRepository.findById(brand1.getId()).orElse(null);
        // then
        assertThat(findBrand.getKorName()).isEqualTo("언더아머");
    }

    @Test
    @DisplayName("브랜드_이름으로_단일_검색_테스트")
    public void 브랜드_이름_단일_검색() {
        // given
        brandRepository.save(brand1);

        // when
        Brand findByNameBrand = brandRepository.findByKorName(brand1.getKorName()).orElse(null);
        // then
        assertThat(findByNameBrand).isEqualTo(brand1);
    }

    @Test
    @DisplayName("브랜드_삭제_테스트")
    public void 브랜드_삭제_테스트() {
        // given
        brandRepository.save(brand1);
        brandRepository.save(brand2);

        // when
        brandRepository.delete(brand1);

        // then
        assertThat(brandRepository.findById(brand1.getId()).orElse(null)).isNull();
    }
}