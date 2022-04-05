package com.study.shoestrade.service;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductSize;
import com.study.shoestrade.dto.BrandDto;
import com.study.shoestrade.dto.ProductDto;
import com.study.shoestrade.dto.ProductSearchDto;
import com.study.shoestrade.exception.BrandEmptyResultDataAccessException;
import com.study.shoestrade.exception.ProductDuplicationException;
import com.study.shoestrade.exception.ProductEmptyResultDataAccessException;
import com.study.shoestrade.repository.BrandRepository;
import com.study.shoestrade.repository.ProductRepository;
import com.study.shoestrade.repository.ProductSizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ProductSizeRepository productSizeRepository;

    /**
     * 상품 등록
     *
     * @param productDto 등록할 상품 정보
     * @return 등록한 상품 id
     */
    @Override
    @Transactional
    public ProductDto saveProduct(ProductDto productDto) {
        log.info("info = {}", "ProductService - saveProduct 실행");

        DuplicateProduct(productDto.getName());

        Brand brand = brandRepository.findById(productDto.getBrandId()).orElseThrow(() ->
                new BrandEmptyResultDataAccessException(1)
        );

        Product product = productDto.toEntity(brand);
        Product saveProduct = productRepository.save(product);

        List<ProductSize> list = new ArrayList<>();

        for (int i = 0; i <= 80; i += 5) {
            list.add(ProductSize.builder()
                    .size(220 + i)
                    .product(saveProduct)
                    .build());
        }

        productSizeRepository.saveAll(list);
        return ProductDto.create(saveProduct);
    }

    /**
     * 상품 전체 검색
     *
     * @return 검색 결과
     */
    @Override
    public List<ProductDto> findProductAll() {
        log.info("info = {}", "ProductService - findProductAll 실행");

        return productRepository.findAll()
                .stream()
                .map(ProductDto::create)
                .collect(Collectors.toList());
    }


    /**
     * 상품 이름으로 검색
     *
     * @return 검색 결과
     */
    @Override
    public List<ProductDto> findProductByName(String name) {
        log.info("info = {}", "ProductService - findProductByName 실행");

        return productRepository.findByNameContains(name)
                .stream()
                .map(ProductDto::create)
                .collect(Collectors.toList());
    }


    /**
     * 선택된 브랜드 내에 있는 상품 이름으로 검색
     *
     * @param productSearchDto 검색어, 브랜드 이름 리스트
     * @return 검색 결과
     */
    @Override
    public List<ProductDto> findProductByNameInBrand(ProductSearchDto productSearchDto) {
        log.info("info = {}", "ProductService - productSearchDto 실행");

        return productRepository.findByNameContainsAndBrandNameIn(productSearchDto.getName(),
                        productSearchDto.getBrandList()
                                .stream()
                                .map(BrandDto::getName)
                                .collect(Collectors.toList())
                ).stream()
                .map(ProductDto::create)
                .collect(Collectors.toList());
    }

    /**
     * 상품 정보 변경
     *
     * @param productDto 변경할 정보
     */
    @Override
    @Transactional
    public void updateProduct(ProductDto productDto) {
        log.info("info = {}", "ProductService - updateProduct 실행");

        Product product = productRepository.findById(productDto.getId()).orElseThrow(() ->
                new ProductEmptyResultDataAccessException(1)
        );

        if (!product.getName().equals(productDto.getName())) {
            DuplicateProduct(productDto.getName());
        }

        Brand brand = brandRepository.findById(productDto.getBrandId()).orElseThrow(() ->
                new BrandEmptyResultDataAccessException(1)
        );

        product.changeProduct(productDto);
        product.changeProductBrand(brand);
    }


    /**
     * 이름 중복 여부
     *
     * @param name 중복검사 할 상품 이름
     */
    private void DuplicateProduct(String name) {
        log.info("info = {}", "ProductService - DuplicateProduct 실행");

        productRepository.findByName(name).ifPresent(
                p -> {
                    throw new ProductDuplicationException();
                }
        );
    }
}
