package com.study.shoestrade.service;

import com.study.shoestrade.domain.interest.InterestProduct;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductSize;
import com.study.shoestrade.dto.interest.request.InterestProductRequestDto;
import com.study.shoestrade.dto.interest.response.InterestProductResponseDto;
import com.study.shoestrade.repository.interest.InterestProductRepository;
import com.study.shoestrade.repository.jdbc.JdbcRepository;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.product.ProductRepository;
import com.study.shoestrade.repository.product.ProductSizeRepository;
import com.study.shoestrade.service.interest.InterestService;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
public class InterestServiceTest {

    @InjectMocks
    InterestService interestService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    ProductSizeRepository productSizeRepository;
    @Mock
    InterestProductRepository interestProductRepository;
    @Mock
    JdbcRepository jdbcRepository;

    Member member;
    Brand brand;
    Product product;
    List<ProductSize> productSizes;

    @BeforeEach
    public void init(){
        member = Member.builder()
                .id(1L)
                .email("email")
                .password("PW")
                .interestProductList(new ArrayList<>())
                .build();

        brand = Brand.builder()
                .id(2L)
                .korName("나이키")
                .engName("NIKE")
                .productList(new ArrayList<>())
                .build();

        product = Product.builder()
                .id(3L)
                .korName("나이키 덩크")
                .engName("NIKE Dunk")
                .code("code")
                .color("black")
                .releasePrice(123000)
                .interest(0)
                .brand(brand)
                .build();

        ProductSize productSize1 = ProductSize.builder()
                .id(100L)
                .size(255)
                .product(product)
                .interestProductList(new ArrayList<>())
                .build();

        ProductSize productSize2 = ProductSize.builder()
                .id(101L)
                .size(260)
                .product(product)
                .interestProductList(new ArrayList<>())
                .build();

        ProductSize productSize3 = ProductSize.builder()
                .id(102L)
                .size(265)
                .product(product)
                .interestProductList(new ArrayList<>())
                .build();

        ProductSize productSize4 = ProductSize.builder()
                .id(103L)
                .size(270)
                .product(product)
                .interestProductList(new ArrayList<>())
                .build();

        productSizes = new ArrayList<>();
        productSizes.add(productSize1);
        productSizes.add(productSize2);
        productSizes.add(productSize3);
        productSizes.add(productSize4);
    }

    @Test
    @DisplayName("관심 상품에 등록되어 있지 않는 상품은 관심 상품으로 등록할 수 있다.")
    public void 관심_상품_추가_성공() {
        // given
        InterestProductRequestDto requestDto = InterestProductRequestDto.builder()
                .interests(List.of(100L, 101L, 102L, 103L))
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(productRepository.findById(any())).willReturn(Optional.of(product));
        given(interestProductRepository.findPreInterests(any(), any())).willReturn(new ArrayList<>());
        given(productSizeRepository.findProductSizes(3L, requestDto.getInterests())).willReturn(productSizes);

        // when
        interestService.addWishList("email", product.getId(), requestDto);

        // then
        assertThat(product.getInterest()).isEqualTo(4);
    }

    @Test
    @DisplayName("관심 상품이 등록되어 있지 않은 상품에 대해서는 모두 체크가 false이다.")
    public void 관심_상품_목록_보기1() {
        // given

        // mocking
        given(productSizeRepository.findByProduct_Id(3L)).willReturn(productSizes);
        given(interestProductRepository.findPreInterests("email", 3L)).willReturn(new ArrayList<>());

        // when
        InterestProductResponseDto responseDto = interestService.getProductWishList("email", 3L);

        // then
        responseDto.getInterestProductSizes()
                        .forEach(i -> assertThat(i.isChecked()).isFalse());
    }

    @Test
    @DisplayName("관심 상품으로 등록되어 있는 상품은 체크가 true이다.")
    public void 관심_상품_목록_보기2() {
        // given
        InterestProduct interest1 = InterestProduct.builder()
                .id(1000L)
                .member(member)
                .productSize(productSizes.get(0))
                .build();

        InterestProduct interest2 = InterestProduct.builder()
                .id(1001L)
                .member(member)
                .productSize(productSizes.get(1))
                .build();

        List<InterestProduct> interests = new ArrayList<>();
        interests.add(interest1);
        interests.add(interest2);

        // mocking
        given(productSizeRepository.findByProduct_Id(3L)).willReturn(productSizes);
        given(interestProductRepository.findPreInterests("email", 3L)).willReturn(interests);

        // when
        InterestProductResponseDto responseDto = interestService.getProductWishList("email", 3L);

        // then
        assertThat(responseDto.getInterestProductSizes().get(0).isChecked()).isTrue();
        assertThat(responseDto.getInterestProductSizes().get(1).isChecked()).isTrue();
        assertThat(responseDto.getInterestProductSizes().get(2).isChecked()).isFalse();
        assertThat(responseDto.getInterestProductSizes().get(3).isChecked()).isFalse();
    }

    @Test
    public void 관심_상품_삭제() {
        // given
        InterestProduct interestProduct = InterestProduct.builder()
                .id(100L)
                .member(member)
                .productSize(productSizes.get(0))
                .build();

        product.addInterest(1);

        // mocking
        given(interestProductRepository.findOneInterest("email", 3L, 100L)).willReturn(Optional.of(interestProduct));
        given(productRepository.findById(3L)).willReturn(Optional.of(product));

        // when
        interestService.deleteInterestProduct("email", 3L, 100L);
        
        // then
        assertThat(product.getInterest()).isEqualTo(0);
    }
}
