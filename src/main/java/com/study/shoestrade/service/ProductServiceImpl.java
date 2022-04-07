package com.study.shoestrade.service;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductImage;
import com.study.shoestrade.domain.product.ProductSize;
import com.study.shoestrade.dto.*;
import com.study.shoestrade.exception.BrandEmptyResultDataAccessException;
import com.study.shoestrade.exception.ProductDuplicationException;
import com.study.shoestrade.exception.ProductEmptyResultDataAccessException;
import com.study.shoestrade.exception.ProductImageDuplicationException;
import com.study.shoestrade.repository.BrandRepository;
import com.study.shoestrade.repository.ProductImageRepository;
import com.study.shoestrade.repository.ProductRepository;
import com.study.shoestrade.repository.ProductSizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
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
    private final ProductImageRepository productImageRepository;

    /**
     * 상품 등록
     *
     * @param productSaveDto 등록할 상품 정보
     * @return 등록한 상품 id
     */
    @Override
    @Transactional
    public ProductSaveDto saveProduct(ProductSaveDto productSaveDto) {
        log.info("info = {}", "ProductService - saveProduct 실행");

        DuplicateProduct(productSaveDto.getName());

        Brand brand = brandRepository.findById(productSaveDto.getBrandId()).orElseThrow(() ->
                new BrandEmptyResultDataAccessException(1)
        );

        Product product = productSaveDto.toEntity(brand);
        Product saveProduct = productRepository.save(product);

        List<ProductSize> list = new ArrayList<>();

        for (int i = 0; i <= 80; i += 5) {
            list.add(ProductSize.builder()
                    .size(220 + i)
                    .product(saveProduct)
                    .build());
        }

        productSizeRepository.saveAll(list);
        return ProductSaveDto.create(saveProduct);
    }

    /**
     * 상품 삭제
     *
     * @param productId 삭제할 상품 id
     */
    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        log.info("info = {}", "ProductService - deleteProduct 실행");
        try {
            productRepository.deleteById(productId);
        } catch (EmptyResultDataAccessException e) {
            throw new ProductEmptyResultDataAccessException(1);
        }
    }

    /**
     * 상품 전체 검색
     *
     * @return 검색 결과
     */
    @Override
    public List<ProductLoadDto> findProductAll() {
        log.info("info = {}", "ProductService - findProductAll 실행");

        return productRepository.findAll()
                .stream()
                .map(ProductLoadDto::create)
                .collect(Collectors.toList());
    }


    /**
     * 상품 이름으로 검색
     *
     * @return 검색 결과
     */
    @Override
    public List<ProductLoadDto> findProductByName(String name) {
        log.info("info = {}", "ProductService - findProductByName 실행");

        return productRepository.findByNameContains(name)
                .stream()
                .map(ProductLoadDto::create)
                .collect(Collectors.toList());
    }


    /**
     * 선택된 브랜드 내에 있는 상품 이름으로 검색
     *
     * @param productSearchDto 검색어, 브랜드 이름 리스트
     * @return 검색 결과
     */
    @Override
    public List<ProductLoadDto> findProductByNameInBrand(ProductSearchDto productSearchDto) {
        log.info("info = {}", "ProductService - productSearchDto 실행");

        return productRepository.findByNameContainsAndBrand_IdIn(productSearchDto.getName(),
                        productSearchDto.getBrandIdList()
                ).stream()
                .map(ProductLoadDto::create)
                .collect(Collectors.toList());
    }

    /**
     * 상품 정보 변경
     *
     * @param productDto 변경할 정보
     */
    @Override
    @Transactional
    public void updateProduct(ProductSaveDto productDto) {
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
     * 상품 이미지 등록
     *
     * @param productImageAddDto 등록할 상품 id, 이미지 정보
     */
    @Override
    @Transactional
    public void addProductImage(ProductImageAddDto productImageAddDto) {
        Product product = productRepository.findById(productImageAddDto.getId()).orElseThrow(() ->
                new ProductEmptyResultDataAccessException(productImageAddDto.getId().toString(), 1)
        );

        DuplicateProductImage(productImageAddDto.getImageNameList(),
                product.getId()
        );

        product.getImageList().addAll(
                productImageAddDto.getImageNameList()
                        .stream()
                        .map(name -> ProductImageDto.builder().name(name).build().toEntity(product))
                        .collect(Collectors.toList())
        );
    }

    /**
     * 상품 이름 중복 여부
     *
     * @param name 중복검사 할 상품 이름
     */
    private void DuplicateProduct(String name) {
        log.info("info = {}", "ProductService - DuplicateProduct 실행");

        productRepository.findByName(name).ifPresent(
                p -> {
                    throw new ProductDuplicationException(name);
                }
        );
    }

    /**
     * 상품 이미지 이름 중복 여부
     *
     * @param names     중복검사 할 이미지 이름
     * @param productId 상품 id
     */
    private void DuplicateProductImage(List<String> names, Long productId) {
        log.info("info = {}", "ProductService - DuplicateProductImage 실행");

        List<ProductImage> findNames = productImageRepository.findByProductIdAndNameIn(productId, names);

        if (!findNames.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            findNames.forEach(productImage -> {
                sb.append(productImage.getName()).append(" ");
            });
            throw new ProductImageDuplicationException(sb.toString());
        }
    }

}
