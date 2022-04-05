package com.study.shoestrade.service;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.dto.ProductDto;
import com.study.shoestrade.repository.BrandRepository;
import com.study.shoestrade.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {


    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Test
    @DisplayName("상품_등록_테스트")
    void 상품_등록() {
        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .name("상품명1")
                .code("상품코드1")
                .releasePrice(100000)
                .build();

        Brand brand = Brand.builder()
                .id(1L)
                .name("브랜드1").build();

        Product product = productDto.toEntity(brand);

        given(productRepository.save(any())).willReturn(product);
        given(brandRepository.findById(any())).willReturn(Optional.ofNullable(brand));

        assertThatCode(() -> {
            productService.saveProduct(productDto);
        }).doesNotThrowAnyException();

    }
}