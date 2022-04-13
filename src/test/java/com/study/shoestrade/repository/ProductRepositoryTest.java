package com.study.shoestrade.repository;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Test
    @DisplayName("상품_등록_테스트")
    public void 상품_등록() {
        // given
        Product product = Product.builder()
                .name("상품명1")
                .code("상품코드1")
                .releasePrice(100000)
                .build();

        // when
        Product findProduct = productRepository.save(product);

        // then
        assertThat(findProduct).isEqualTo(product);
    }


    @Test
    @DisplayName("상품_검색_테스트")
    public void 상품_검색() {
        // given
        Brand brand = brandRepository.save(Brand.builder().id(1L).name("브랜드1").build());
        Product product = Product.builder()
                .name("상품명1")
                .code("상품코드1")
                .brand(brand)
                .build();

        productRepository.save(product);

        List<Long> list = new ArrayList<>();
        list.add(brand.getId());

        // when
        List<Product> findList = productRepository.findProduct("상품명1", list);

        // then
        assertThat(findList)
                .extracting("name")
                .containsOnly("상품명1");
    }
}