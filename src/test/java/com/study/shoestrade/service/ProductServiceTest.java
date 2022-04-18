package com.study.shoestrade.service;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.dto.product.ProductImageAddDto;
import com.study.shoestrade.dto.product.ProductDto;
import com.study.shoestrade.dto.product.request.ProductSearchDto;
import com.study.shoestrade.repository.brand.BrandRepository;
import com.study.shoestrade.repository.jdbc.JdbcRepository;
import com.study.shoestrade.repository.product.ProductImageRepository;
import com.study.shoestrade.repository.product.ProductRepository;
import com.study.shoestrade.service.product.ProductService;
import com.study.shoestrade.service.product.ProductServiceImpl;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private JdbcRepository jdbcRepository;

    @Test
    @DisplayName("상품_등록_테스트")
    void 상품_등록() {
        // given
        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .name("상품명1")
                .code("상품코드1")
                .releasePrice(100000)
                .imageList(new ArrayList<>())
                .build();

        Brand brand = Brand.builder()
                .id(1L)
                .korName("브랜드1").build();

        Product product = productDto.toEntity(brand);

        given(productRepository.save(any())).willReturn(product);
        given(brandRepository.findById(any())).willReturn(Optional.ofNullable(brand));
        willDoNothing().given(jdbcRepository).saveAllImage(any());
        willDoNothing().given(jdbcRepository).saveAllSize(any());

        // when
        // then
        assertThatCode(() -> productService.saveProduct(productDto)).doesNotThrowAnyException();
    }


    @Test
    @DisplayName("상품_정보_변경_테스트")
    void 상품_정보_변경() {
        // given

        Brand brand = Brand.builder()
                .korName("브랜드1")
                .id(2L)
                .build();

        Product product = Product.builder()
                .name("변경 전 상품명")
                .id(1L)
                .brand(brand)
                .build();

        ProductDto updateDto = ProductDto.builder()
                .name("변경 후 상품명")
                .id(1L)
                .brandId(2L)
                .build();


        given(productRepository.findById(any())).willReturn(Optional.ofNullable(product));
        given(brandRepository.findById(any())).willReturn(Optional.ofNullable(brand));

        // when
        productService.updateProduct(updateDto);

        // then
        assertThat(product.getName()).isEqualTo(updateDto.getName());
    }

    @Test
    @DisplayName("상품_삭제_테스트")
    void 상품_삭제() {
        // given
        willDoNothing().given(productRepository).deleteById(any());

        // when
        // then
        assertThatCode(() -> productService.deleteProduct(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("상품_검색_테스트")
    void 상품_검색() {
        // given
        ArrayList<Product> list = new ArrayList<>();
        list.add(Product.builder()
                .name("상품명1")
                .brand(Brand.builder()
                        .id(1L)
                        .build())
                .imageList(new ArrayList<>())
                .build());
        PageImpl<Product> page = new PageImpl<>(list);
        PageRequest pageRequest = PageRequest.of(0, 3);

        given(productRepository.findProduct(any(), any(), any())).willReturn(page);

        // when
        ProductSearchDto productSearchDto = ProductSearchDto.builder()
                .name("상품명1")
                .brandIdList(new ArrayList<>(List.of(1L)))
                .build();

        Page<ProductDto> findPages = productService.findProductByNameInBrand(productSearchDto, pageRequest);

        // then
        assertThat(findPages).isEqualTo(page.map(ProductDto::create));
    }

    @Test
    @DisplayName("상품_이미지_등록_테스트")
    void 상품_이미지_등록() {
        // given
        Product product = Product.builder()
                .name("상품명1")
                .brand(Brand.builder()
                        .id(1L)
                        .build())
                .imageList(new ArrayList<>())
                .build();

        ProductImageAddDto productImageAddDto = ProductImageAddDto.builder()
                .productId(1L)
                .imageNameList(new ArrayList<>(Arrays.asList("이미지1", "이미지2")))
                .build();

        given(productImageRepository.findByProductIdAndNameIn(any(), any())).willReturn(new ArrayList<>());
        given(productRepository.findById(any())).willReturn(Optional.ofNullable(product));
        willDoNothing().given(jdbcRepository).saveAllImage(any());
        // when
        assertThatCode(() -> productService.addProductImage(productImageAddDto))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("상품_이미지_삭제_테스트")
    void 상품_이미지_삭제() {
        // given
        willDoNothing().given(productImageRepository).deleteById(any());

        // when
        // then
        assertThatCode(() -> productService.deleteProductImage(1L))
                .doesNotThrowAnyException();
    }

}