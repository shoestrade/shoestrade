package com.study.shoestrade.repository;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductSize;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.product.response.ProductLoadDto;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.product.ProductSizeNoSuchElementException;
import com.study.shoestrade.repository.brand.BrandRepository;
import com.study.shoestrade.repository.jdbc.JdbcRepository;
import com.study.shoestrade.repository.product.ProductRepository;
import com.study.shoestrade.repository.product.ProductSizeRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    @DisplayName("상품_등록_테스트")
    public void 상품_등록() {
        // given
        Product product = Product.builder()
                .korName("상품명1")
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
        Brand brand = brandRepository.save(Brand.builder()
                .korName("브랜드1")
                .build());

        Product product = productRepository.save(Product.builder()
                .korName("상품명1")
                .code("상품코드1")
                .brand(brand)
                .imageList(new ArrayList<>())
                .build());

        productSizeRepository.save(ProductSize.builder()
                .size(220)
                .product(product)
                .build());

        PageRequest pageRequest = PageRequest.of(0, 3);
        // when
        Page<ProductLoadDto> findPage = productRepository.findProduct("상품명1", List.of(brand.getId()), pageRequest);

        // then
        assertThat(findPage)
                .extracting("korName")
                .contains("상품명1");
    }
}