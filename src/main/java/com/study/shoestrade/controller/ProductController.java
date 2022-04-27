package com.study.shoestrade.controller;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.dto.product.request.ProductSaveDto;
import com.study.shoestrade.dto.product.ProductImageAddDto;
import com.study.shoestrade.dto.product.request.ProductSearchDto;
import com.study.shoestrade.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ResponseService responseService;
    private final ProductService productService;

    /**
     * 상품 등록
     *
     * @param productDto 상품 등록 정보
     * @return 등록 완료된 상품 정보
     */
    @PostMapping
    public Result saveProduct(@RequestBody ProductSaveDto productDto) {
        productService.saveProduct(productDto);
        return responseService.getSuccessResult();
    }

    /**
     * 상품 삭제
     *
     * @param productId 삭제할 상품 id
     * @return 등록 완료된 상품 정보
     */
    @DeleteMapping("/{productId}")
    public Result deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return responseService.getSuccessResult();
    }

    /**
     * 상품 검색
     *
     * @param productSearchDto 검색어, 선택된 브랜드 이름 리스트
     * @param pageable 페이지 정보
     * @return 검색된 결과
     */
    @GetMapping
    public Result findProductByNameAndBrandList(@RequestBody ProductSearchDto productSearchDto, Pageable pageable) {
        return responseService.getSingleResult(productService.findProductByNameInBrand(productSearchDto, pageable));
    }

    /**
     * 상품 정보 수정
     *
     * @param productId 수정할 상품 id
     * @param productDto 변경할 상품 정보
     * @return 변경 성공 여부
     */
    @PostMapping("/{productId}")
    public Result updateProduct(@PathVariable Long productId, @RequestBody ProductSaveDto productDto) {
        productService.updateProduct(productId, productDto);
        return responseService.getSuccessResult();
    }


    /**
     * 상품 이미지 등록
     *
     * @param productImageAddDto 등록할 상품 id, 이미지 정보
     * @return 등록 성공 여부
     */
    @PostMapping("/images")
    public Result addImageProduct(@RequestBody ProductImageAddDto productImageAddDto) {
        productService.addProductImage(productImageAddDto);
        return responseService.getSuccessResult();
    }

    /**
     * 상품 이미지 삭제
     *
     * @param productImageId 삭제할 상품 id
     * @return 등록 완료된 상품 정보
     */
    @DeleteMapping("/images/{productImageId}")
    public Result deleteProductImage(@PathVariable Long productImageId) {
        productService.deleteProductImage(productImageId);
        return responseService.getSuccessResult();
    }

    /**
     * 상품 상세 검색
     *
     * @param productId 검색할 상품 id
     * @return 검색 결과
     */
    @GetMapping("/{productId}")
    public Result findProductDetail(@PathVariable Long productId){
        return responseService.getSingleResult(productService.findProductDetailById(productId));
    }
}
