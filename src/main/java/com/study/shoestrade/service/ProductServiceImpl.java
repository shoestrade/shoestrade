package com.study.shoestrade.service;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.dto.ProductDto;
import com.study.shoestrade.exception.BrandEmptyResultDataAccessException;
import com.study.shoestrade.exception.ProductDuplicationException;
import com.study.shoestrade.exception.ProductEmptyResultDataAccessException;
import com.study.shoestrade.repository.BrandRepository;
import com.study.shoestrade.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

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
                new BrandEmptyResultDataAccessException(0)
        );

        Product product = productDto.toEntity(brand);

        return ProductDto.create(productRepository.save(product));
    }

    /**
     * 상품 전체 검색
     *
     * @return 검색 결과
     */
    @Override
    public List<ProductDto> findProductAll() {
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
        return productRepository.findByNameContains(name)
                .stream()
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
        Product product = productRepository.findById(productDto.getId()).orElseThrow(() ->
                new ProductEmptyResultDataAccessException(0)
        );

        if (!product.getName().equals(productDto.getName())) {
            DuplicateProduct(productDto.getName());
        }

        Brand brand = brandRepository.findById(productDto.getBrandId()).orElseThrow(() ->
                new BrandEmptyResultDataAccessException(0)
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
        productRepository.findByName(name).ifPresent(
                p -> {
                    throw new ProductDuplicationException();
                }
        );
    }
}
