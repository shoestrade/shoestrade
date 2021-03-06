package com.study.shoestrade.service.product;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductImage;
import com.study.shoestrade.domain.product.ProductSize;
import com.study.shoestrade.dto.product.request.ProductSaveDto;
import com.study.shoestrade.dto.product.ProductImageAddDto;
import com.study.shoestrade.dto.product.ProductImageDto;
import com.study.shoestrade.dto.product.request.ProductSearchDto;
import com.study.shoestrade.dto.product.response.ProductDetailDto;
import com.study.shoestrade.dto.product.response.ProductLoadDto;
import com.study.shoestrade.exception.brand.BrandEmptyResultDataAccessException;
import com.study.shoestrade.exception.product.ProductDuplicationException;
import com.study.shoestrade.exception.product.ProductEmptyResultDataAccessException;
import com.study.shoestrade.exception.product.ProductImageDuplicationException;
import com.study.shoestrade.exception.product.ProductImageEmptyResultDataAccessException;
import com.study.shoestrade.repository.brand.BrandRepository;
import com.study.shoestrade.repository.jdbc.JdbcRepository;
import com.study.shoestrade.repository.product.ProductImageRepository;
import com.study.shoestrade.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ProductImageRepository productImageRepository;
    private final JdbcRepository jdbcRepository;

    /**
     * ?????? ??????
     *
     * @param productSaveDto ????????? ?????? ??????
     */
    @Override
    @Transactional
    public void saveProduct(ProductSaveDto productSaveDto) {
        DuplicateProductKorName(productSaveDto.getKorName());
        DuplicateProductEngName(productSaveDto.getEngName());

        Brand brand = brandRepository.findById(productSaveDto.getBrandId()).orElseThrow(() ->
                new BrandEmptyResultDataAccessException(productSaveDto.getBrandId().toString(), 1)
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

        jdbcRepository.saveAllSize(list);

        jdbcRepository.saveAllImage(productSaveDto.getImageList()
                .stream()
                .map(i -> ProductImage.builder()
                        .name(i)
                        .product(product)
                        .build())
                .collect(Collectors.toList()));
    }

    /**
     * ?????? ??????
     *
     * @param productId ????????? ?????? id
     */
    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        try {
            productRepository.deleteById(productId);
        } catch (EmptyResultDataAccessException e) {
            throw new ProductEmptyResultDataAccessException(productId.toString(), 1);
        }
    }

    /**
     * ????????? ????????? ?????? ?????? ?????? ???????????? ??????
     *
     * @param productSearchDto ?????????, ????????? ?????? ?????????
     * @param pageable         ????????? ??????
     * @return ?????? ??????
     */
    @Override
    public Page<ProductLoadDto> findProductByNameInBrand(ProductSearchDto productSearchDto, Pageable pageable) {
        return productRepository.findProduct(productSearchDto.getName(),
                productSearchDto.getBrandIdList(), pageable);
    }

    /**
     * ?????? ?????? ??????
     *
     * @param id             ????????? ?????? id
     * @param productSaveDto ????????? ??????
     */
    @Override
    @Transactional
    public void updateProduct(Long id, ProductSaveDto productSaveDto) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ProductEmptyResultDataAccessException(1)
        );

        if (!product.getKorName().equals(productSaveDto.getKorName())) {
            DuplicateProductKorName(productSaveDto.getKorName());
        }

        if (!product.getEngName().equals(productSaveDto.getEngName())) {
            DuplicateProductEngName(productSaveDto.getEngName());
        }

        Brand brand = brandRepository.findById(productSaveDto.getBrandId()).orElseThrow(() ->
                new BrandEmptyResultDataAccessException(productSaveDto.getBrandId().toString(), 1)
        );

        product.changeProduct(productSaveDto);
        product.changeProductBrand(brand);
    }


    @Override
    public List<ProductImageDto> findProductImageByProductId(Long productId) {
        productRepository.findById(productId).orElseThrow(() ->
                new ProductEmptyResultDataAccessException(productId.toString(), 1)
        );

        return productImageRepository.findByProductId(productId)
                .stream()
                .map(ProductImageDto::create)
                .collect(Collectors.toList());
    }

    /**
     * ?????? ????????? ??????
     *
     * @param productId          ????????? ?????? id
     * @param productImageAddDto ????????? ??????
     */
    @Override
    @Transactional
    public void addProductImage(Long productId, ProductImageAddDto productImageAddDto) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ProductEmptyResultDataAccessException(productId.toString(), 1)
        );

        duplicateProductImage(productImageAddDto.getImageNameList(), product.getId());

        jdbcRepository.saveAllImage(
                productImageAddDto.getImageNameList()
                        .stream()
                        .map(name -> ProductImageDto.builder().name(name).build().toEntity(product))
                        .collect(Collectors.toList())
        );
    }

    /**
     * ?????? ????????? ??????
     *
     * @param productImageId ????????? ????????? id
     */
    @Override
    @Transactional
    public void deleteProductImage(Long productImageId) {
        try {
            productImageRepository.deleteById(productImageId);
        } catch (EmptyResultDataAccessException e) {
            throw new ProductImageEmptyResultDataAccessException(productImageId.toString(), 1);
        }
    }


    /**
     * ?????? ?????? ??????
     *
     * @param productId ????????? ?????? id
     * @return ?????? ??????
     */
    @Override
    public ProductDetailDto findProductDetailById(Long productId) {
        return productRepository.findProductDetail(productId)
                .orElseThrow(() -> new ProductEmptyResultDataAccessException(productId.toString(), 1));
    }

    /**
     * ?????? ?????? ?????? ?????? ??????
     *
     * @param korName ???????????? ??? ?????? ?????? ??????
     */
    private void DuplicateProductKorName(String korName) {
        productRepository.findByKorName(korName).ifPresent(p -> {
            throw new ProductDuplicationException(korName);
        });
    }

    /**
     * ?????? ?????? ?????? ?????? ??????
     *
     * @param engName ???????????? ??? ?????? ?????? ??????
     */
    private void DuplicateProductEngName(String engName) {
        productRepository.findByEngName(engName).ifPresent(p -> {
            throw new ProductDuplicationException(engName);
        });
    }

    /**
     * ?????? ????????? ?????? ?????? ??????
     *
     * @param names     ???????????? ??? ????????? ??????
     * @param productId ?????? id
     */
    private void duplicateProductImage(List<String> names, Long productId) {
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
