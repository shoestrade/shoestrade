package com.study.shoestrade.controller;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.dto.brand.BrandDto;
import com.study.shoestrade.service.brand.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final ResponseService responseService;

    /**
     * 브랜드 검색
     *
     * @param brandName 브랜드 이름
     * @param pageable  페이지 정보
     * @return 검색된 브랜드 리스트
     */
    @GetMapping()
    public Result findBrandByName(@RequestParam(value = "name") @Nullable String brandName, Pageable pageable) {
        return responseService.getSingleResult(brandService.findByBrandName(brandName, pageable));
    }

    /**
     * 브랜드 등록
     *
     * @param brandDto 등록할 브랜드 정보
     * @return 등록완료 결과
     */
    @PostMapping
    public Result saveBrand(@RequestBody BrandDto brandDto) {
        return responseService.getSingleResult(brandService.saveBrand(brandDto));
    }

    /**
     * 브랜드 삭제
     *
     * @param id 삭제할 브랜드 id
     * @return 삭제 결과
     */
    @DeleteMapping("/{id}")
    public Result deleteBrand(@PathVariable Long id) {
        brandService.deleteByBrandId(id);
        return responseService.getSuccessResult();
    }

    /**
     * 브랜드 수정
     *
     * @param id       수정할 브랜드 id
     * @param brandDto 수정할 브랜드 정보
     * @return 수정 결과
     */
    @PostMapping("/{id}")
    public Result updateBrand(@PathVariable Long id, @RequestBody BrandDto brandDto) {
        brandService.updateBrand(id, brandDto);
        return responseService.getSuccessResult();
    }
}
