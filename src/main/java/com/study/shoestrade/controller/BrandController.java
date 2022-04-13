package com.study.shoestrade.controller;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.ListResult;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.dto.brand.BrandDto;
import com.study.shoestrade.service.brand.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final ResponseService responseService;

    /**
     * 브랜드 이름으로 검색
     *
     * @param brandName 브랜드 이름
     * @return 검색된 브랜드 리스트
     */
    @GetMapping("/{brandName}")
    public ListResult<BrandDto> findBrandByName(@PathVariable String brandName) {
        log.info("info = {}", "GetBrandController - findByName 실행");
        return responseService.getListResult(brandService.findByBrandName(brandName));
    }

    /**
     * 브랜드 전체 검색
     *
     * @return 브랜드 전체 리스트
     */
    @GetMapping
    public ListResult<BrandDto> findBrandAll() {
        log.info("info = {}", "GetBrandController - findBrandAll 실행");
        return responseService.getListResult(brandService.findBrandAll());
    }

    /**
     * 브랜드 등록
     * @param name 검색어
     * @return 등록완료 결과
     */
    @PostMapping("/{name}")
    public Result saveBrand(@PathVariable String name) {
        log.info("info = {}", "GetBrandController - saveBrand 실행");
        return responseService.getSingleResult(brandService.saveBrand(name));
    }

    /**
     * 브랜드 삭제
     * @param id 삭제할 브랜드 id
     * @return 삭제 결과
     */
    @DeleteMapping("/{id}")
    public Result deleteBrand(@PathVariable Long id) {
        log.info("info = {}", "GetBrandController - deleteBrand 실행");
        brandService.deleteByBrandId(id);
        return responseService.getSuccessResult();
    }

    /**
     * 브랜드 수정
     * @param brandDto 수정할 브랜드 정보
     * @return 수정 결과
     */
    @PostMapping("/update")
    public Result updateBrand(@RequestBody BrandDto brandDto) {
        log.info("info = {}", "GetBrandController - updateBrand 실행");
        brandService.updateBrand(brandDto);
        return responseService.getSuccessResult();
    }
}
