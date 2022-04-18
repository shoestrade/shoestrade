package com.study.shoestrade.service;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.dto.brand.BrandDto;
import com.study.shoestrade.repository.brand.BrandRepository;
import com.study.shoestrade.service.brand.BrandService;
import com.study.shoestrade.service.brand.BrandServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

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
    @DisplayName("브랜드_등록_테스트")
    void 브랜드_등록() {
        // given
        given(brandRepository.save(any())).willReturn(brand1);

        // when
        BrandDto findBrandDto = brandService.saveBrand(brand1.getName());

        // then
        assertThat(findBrandDto).isEqualTo(BrandDto.create(brand1));
    }


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
    @DisplayName("브랜드_삭제_테스트")
    void 브랜드_삭제() {
        // given
        willDoNothing().given(brandRepository).deleteById(brand1.getId());

        // when
        // then
        assertThatCode(() -> brandService.deleteByBrandId(brand1.getId())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("브랜드_이름으로_검색_테스트")
    void 브랜드_이름_검색() {
        // given
        List<Brand> list = new ArrayList<>();
        list.add(brand1);
        Page<Brand> page = new PageImpl<>(list);
        PageRequest pageRequest = PageRequest.of(0, 3);

        given(brandRepository.findByNameContains(any(), any())).willReturn(page);
        // when
        Page<BrandDto> findPage = brandService.findByBrandName("나이키", pageRequest);

        // then
        assertThat(findPage).isEqualTo(page.map(BrandDto::create));
    }
}