package com.study.shoestrade.controller;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.ListResult;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.dto.ProductLoadDto;
import com.study.shoestrade.dto.ProductSaveDto;
import com.study.shoestrade.dto.ProductImageAddDto;
import com.study.shoestrade.dto.ProductSearchDto;
import com.study.shoestrade.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/product")
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
        log.info("info = {}", "ProductController - deleteBrand 실행");
        return responseService.getSingleResult(productService.saveProduct(productDto));
    }

    /**
     * 상품 삭제
     *
     * @param productId 삭제할 상품 id
     * @return 등록 완료된 상품 정보
     */
    @DeleteMapping("/{productId}")
    public Result deleteProduct(@PathVariable Long productId) {
        log.info("info = {}", "ProductController - deleteProduct 실행");
        productService.deleteProduct(productId);
        return responseService.getSuccessResult();
    }

    /**
     * 상품 전체 검색
     *
     * @return 검색된 결과
     */
    @GetMapping
    public ListResult<ProductLoadDto> findProductAll() {
        log.info("info = {}", "ProductController - findProductAll 실행");
        return responseService.getListResult(productService.findProductAll());
    }

    /**
     * 상품 이름으로 검색
     *
     * @param name 검색어
     * @return 검색된 결과
     */
    @GetMapping("/{name}")
    public ListResult<ProductLoadDto> findProductByName(@PathVariable String name) {
        log.info("info = {}", "ProductController - findProductByName 실행");
        return responseService.getListResult(productService.findProductByName(name));
    }

    /**
     * 선택된 브랜드 내에 상품 이름으로 검색
     *
     * @param productSearchDto 검색어, 선택된 브랜드 이름 리스트
     * @return 검색된 결과
     */
    @GetMapping("/search")
    public ListResult<ProductLoadDto> findProductByNameAndBrandList(@RequestBody ProductSearchDto productSearchDto) {
        log.info("info = {}", "ProductController - findProductByNameAndBrandList 실행");
        return responseService.getListResult(productService.findProductByNameInBrand(productSearchDto));
    }

    /**
     * 상품 정보 수정
     *
     * @param productSaveDto 변경할 상품 정보
     * @return 변경 성공 여부
     */
    @PostMapping("/{id}")
    public Result updateProduct(@PathVariable String id, @RequestBody ProductSaveDto productSaveDto) {
        log.info("info = {}", "ProductController - updateProduct 실행");
        productService.updateProduct(productSaveDto);
        return responseService.getSuccessResult();
    }


    /**
     * 상품 이미지 등록
     *
     * @param productImageAddDto 등록할 상품 id, 이미지 정보
     * @return 등록 성공 여부
     */
    @PostMapping("/image")
    public Result addImageProduct(@RequestBody ProductImageAddDto productImageAddDto) {
        log.info("info = {}", "ProductController - addImageProduct");
        productService.addProductImage(productImageAddDto);
        return responseService.getSuccessResult();
    }
}
