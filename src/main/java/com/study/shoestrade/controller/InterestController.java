package com.study.shoestrade.controller;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.dto.brand.BrandDto;
import com.study.shoestrade.dto.interest.request.InterestProductRequestDto;
import com.study.shoestrade.dto.interest.response.InterestProductResponseDto;
import com.study.shoestrade.dto.interest.response.MyInterest;
import com.study.shoestrade.service.interest.InterestService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InterestController {

    private final InterestService interestService;
    private final ResponseService responseService;

    @ApiOperation(value = "관심 상품 등록 및 수정", notes = "관심 상품을 등록 및 수정합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "관심 상품 등록 및 수정 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "productId", value = "관심 상품으로 등록할 상품의 id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "requestDto", value = "관심 상품으로 등록할 상품 사이즈들의 id", dataTypeClass = InterestProductRequestDto.class)
    })
    @PostMapping("/products/{productId}/interests")
    @ResponseStatus(HttpStatus.CREATED)
    public Result addWishList(@LoginMember String email, @PathVariable("productId") Long productId, @RequestBody InterestProductRequestDto requestDto){
        interestService.addWishList(email, productId, requestDto);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "상품 페이지에서 관심 상품 보기", notes = "상품 페이지에서 관심 상품 클릭 시 모달창에서 사용자가 관심상품 사이즈 리스트(관심 상품 등록 여부 확인).")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상품 페이지에서 관심 상품 보기 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "productId", value = "상품의 id", dataTypeClass = Long.class),
    })
    @GetMapping("/products/{productId}/interests")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<InterestProductResponseDto> getProductWishList(@LoginMember String email, @PathVariable("productId") Long productId){
        InterestProductResponseDto responseDto = interestService.getProductWishList(email, productId);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "마이페이지에서 관심 상품 보기", notes = "마이페이지에서 등록된 관심 상품 목록 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상품 페이지에서 관심 상품 보기 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header")
    })
    @GetMapping("/member/interests")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<Page<MyInterest>> getMyWishList(@LoginMember String email, @PageableDefault(size = 10) Pageable pageable){
        Page<MyInterest> responseDto = interestService.getMyWishList(email, pageable);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "마이페이지에서 관심 상품 삭제", notes = "마이페이지에서 등록된 관심 상품 삭제합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "마이페이지에서 관심 상품 삭제 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "interestId", value = "관심 상품의 id", dataTypeClass = Long.class),
    })
    @DeleteMapping("/member/interests/{interestId}")
    @ResponseStatus(HttpStatus.OK)
    public Result deleteInterestProduct(@LoginMember String email, @PathVariable("interestId") Long interestId){
        interestService.deleteInterestProduct(email, interestId);
        return responseService.getSuccessResult();
    }

}
