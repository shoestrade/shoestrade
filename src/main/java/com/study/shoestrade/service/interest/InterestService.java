package com.study.shoestrade.service.interest;

import com.study.shoestrade.domain.interest.InterestProduct;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductSize;
import com.study.shoestrade.dto.interest.request.InterestProductRequestDto;
import com.study.shoestrade.dto.interest.response.InterestProductResponseDto;
import com.study.shoestrade.dto.interest.response.InterestProductResponseSizeDto;
import com.study.shoestrade.dto.interest.response.MyInterest;
import com.study.shoestrade.exception.interest.InterestNotFoundException;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.product.ProductEmptyResultDataAccessException;
import com.study.shoestrade.repository.interest.InterestProductRepository;
import com.study.shoestrade.repository.jdbc.JdbcRepository;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.product.ProductRepository;
import com.study.shoestrade.repository.product.ProductSizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class InterestService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final InterestProductRepository interestProductRepository;
    private final JdbcRepository jdbcRepository;

    // 관심 상품 추가 및 수정
    public void addWishList(String email, Long productId, InterestProductRequestDto requestDto){
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductEmptyResultDataAccessException(productId.toString(), 1));

        // 기존에 등록되어 있는 관심 상품
        List<InterestProduct> preInterests = interestProductRepository.findPreInterests(email, productId);
        // 기존 관심 상품 toMap
        Map<ProductSize, Long> preProducts = preInterests.stream()
                .collect(Collectors.toMap(InterestProduct::getProductSize, InterestProduct::getId));

        // 입력 받은 ProductSize의 Id로 ProductSize들 찾기
        List<ProductSize> productSizes = productSizeRepository.findProductSizes(productId, requestDto.getInterests());
        // ProductSize -> toSet
        Set<ProductSize> newProducts = new HashSet<>(productSizes);

        // 삭제할 기존 관심 상품
        List<InterestProduct> deleteInterests = preProducts.entrySet().stream()
                .filter(entry -> !newProducts.contains(entry.getKey()))
                .map(entry ->
                        InterestProduct.builder()
                                .id(entry.getValue())
                                .member(findMember)
                                .productSize(entry.getKey())
                                .build())
                .collect(Collectors.toList());

        // 추가할 새로운 관심 상품
        List<InterestProduct> insertInterests = newProducts.stream()
                .filter(p -> !preProducts.containsKey(p))
                .map(productSize ->
                        InterestProduct.builder()
                                .member(findMember)
                                .productSize(productSize)
                                .build())
                .collect(Collectors.toList());

        // 삭제된 관심 상품 삭제
        interestProductRepository.deleteAllInBatch(deleteInterests);
        // 새로운 관심 상품 리스트 추가 (벌크)
        jdbcRepository.saveAllInterest(insertInterests);

        product.subInterest(deleteInterests.size());
        product.addInterest(insertInterests.size());
    }

    // 상품페이지에서 관심 상품 목록 보기
    public InterestProductResponseDto getProductWishList(String email, Long productId){
        List<ProductSize> productSizes = productSizeRepository.findByProduct_Id(productId);

        Set<Long> collect = interestProductRepository.findPreInterests(email, productId).stream()
                .map(InterestProduct::getProductSize)
                .map(ProductSize::getId)
                .collect(Collectors.toSet());

        return InterestProductResponseDto.builder()
                .interestProductSizes(
                        productSizes.stream()
                                .map(p -> InterestProductResponseSizeDto.builder()
                                        .productSizeId(p.getId())
                                        .checked(collect.contains(p.getId()))
                                        .build())
                                .collect(Collectors.toList()))
                .build();
    }

    // 마이페이지에서 관심 상품 목록 보기
    public Page<MyInterest> getMyWishList(String email, Pageable pageable){
        return interestProductRepository.findMyInterests(email, pageable);
    }

    // 마이페이지에서 관심 상품 삭제
    public void deleteInterestProduct(String email, Long interestId){
        InterestProduct interestProduct = interestProductRepository.findOneInterest(email, interestId)
                .orElseThrow(InterestNotFoundException::new);

        Product product = interestProduct.getProductSize().getProduct();

        product.subInterest(1);
        interestProductRepository.delete(interestProduct);
    }
}
