package com.study.shoestrade.service;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductSize;
import com.study.shoestrade.dto.ProductSaveDto;
import com.study.shoestrade.repository.BrandRepository;
import com.study.shoestrade.repository.ProductRepository;
import com.study.shoestrade.repository.ProductSizeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {


    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductSizeRepository productSizeRepository;

    @Test
    @DisplayName("상품_등록_테스트")
    void 상품_등록() {
        ProductSaveDto productSaveDto = ProductSaveDto.builder()
                .id(1L)
                .name("상품명1")
                .code("상품코드1")
                .releasePrice(100000)
                .build();

        Brand brand = Brand.builder()
                .id(1L)
                .name("브랜드1").build();

        List<ProductSize> list = new ArrayList<>();

        for (int i = 0; i <= 80; i += 5) {
            list.add(ProductSize.builder()
                    .size(220 + i)
                    .product(productSaveDto.toEntity(brand))
                    .build());
        }

        Product product = productSaveDto.toEntity(brand);

        given(productRepository.save(any())).willReturn(product);
        given(brandRepository.findById(any())).willReturn(Optional.ofNullable(brand));
        given(productSizeRepository.saveAll(any())).willReturn(list);

        assertThatCode(() -> {
            productService.saveProduct(productSaveDto);
        }).doesNotThrowAnyException();

    }
}