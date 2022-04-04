package com.study.shoestrade.service;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.dto.BrandDto;
import com.study.shoestrade.exception.BrandDuplicationException;
import com.study.shoestrade.repository.BrandRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {

    @InjectMocks
    private BrandServiceImpl brandService;

    @Mock
    private BrandRepository brandRepository;

    Brand brand1 = Brand.builder()
            .id(1L)
            .name("나이키")
            .build();

    Brand brand2 = Brand.builder()
            .id(2L)
            .name("아디다스")
            .build();

    Brand brand3 = Brand.builder()
            .id(3L)
            .name("뉴발란스")
            .build();

    @Test
    @DisplayName("브랜드_수정_테스트")
    void 브랜드_수정() {
        // given
        given(brandRepository.findById(brand1.getId())).willReturn(Optional.ofNullable(brand1));


        BrandDto updateBrand = BrandDto.builder()
                .id(1L)
                .name("언더아머")
                .build();

        // when
        brandService.updateBrand(updateBrand);

        // then
        Optional<Brand> findBrand = brandRepository.findById(brand1.getId());
        assertThat(findBrand.orElse(brand1).getName()).isEqualTo("언더아머");
    }

    @Test
    @DisplayName("브랜드_전체검색_테스트")
    void 브랜드_전체검색() {
        // given
        List<Brand> list = new ArrayList<>();
        list.add(brand1);
        list.add(brand2);
        list.add(brand3);

        given(brandRepository.findAll()).willReturn(list);

        // when

        List<BrandDto> findList = brandService.findBrandAll();

        // then
        assertThat(findList).isEqualTo(list.stream().map(BrandDto::create).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("브랜드_삭제_테스트")
    void 브랜드_삭제() {
        // given
        willDoNothing().given(brandRepository).deleteById(brand1.getId());

        // when
        // then
        assertThatCode(() -> {
            brandService.deleteByBrandId(brand1.getId());
        }).doesNotThrowAnyException();
    }

}