package com.study.shoestrade.controller;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.ListResult;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.dto.ProductDto;
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
    public Result saveProduct(@RequestBody ProductDto productDto) {
        log.info("info = {}", "ProductController - deleteBrand 실행");
        return responseService.getSingleResult(productService.saveProduct(productDto));
    }

    /**
     * 상품 전체 검색
     *
     * @return 검색된 결과
     */
    @GetMapping
    public ListResult<ProductDto> findProductAll() {
        log.info("info = {}", "ProductController - findProductAll 실행");
        return responseService.getListResult(productService.findProductAll());
    }

    /**
     * 상품 이름으로 검색
     *
     * @return 검색된 결과
     */
    @GetMapping("/{name}")
    public ListResult<ProductDto> findProductByName(@PathVariable String name) {
        log.info("info = {}", "ProductController - findProductByName 실행");
        return responseService.getListResult(productService.findProductByName(name));
    }

    @PostMapping("/update")
    public Result updateProduct(@RequestBody ProductDto productDto) {
        log.info("info = {}", "ProductController - updateProduct 실행");
        productService.updateProduct(productDto);
        return responseService.getSuccessResult();
    }
}
