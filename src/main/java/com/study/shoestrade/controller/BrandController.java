package com.study.shoestrade.controller;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.dto.brand.BrandDto;
import com.study.shoestrade.service.brand.BrandService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BrandController {

    private final BrandService brandService;
    private final ResponseService responseService;

    @ApiOperation(value = "브랜드 목록 조회", notes = "브랜드 대한 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "브랜드 목록 조회 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "검색할 브랜드 이름", dataTypeClass = String.class)
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<Page<BrandDto>> findBrandByName(@RequestParam(value = "name", required = false) @Nullable String brandName, Pageable pageable) {
        return responseService.getSingleResult(brandService.findByBrandName(brandName, pageable));
    }

    @ApiOperation(value = "브랜드 등록", notes = "브랜드를 등록합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "브랜드 등록 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "brandDto", value = "저장할 브랜드 정보", dataTypeClass = BrandDto.class)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result saveBrand(@RequestBody BrandDto brandDto) {
        brandService.saveBrand(brandDto);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "브랜드 삭제", notes = "브랜드를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "브랜드 삭제 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "삭제할 브랜드 id", dataTypeClass = Long.class)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result deleteBrand(@PathVariable Long id) {
        brandService.deleteByBrandId(id);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "브랜드 수정", notes = "브랜드를 수정합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "브랜드 수정 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "삭제할 브랜드 id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "brandDto", value = "수정할 브랜드 정보", dataTypeClass = BrandDto.class)
    })
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result updateBrand(@PathVariable Long id, @RequestBody BrandDto brandDto) {
        brandService.updateBrand(id, brandDto);
        return responseService.getSuccessResult();
    }
}
