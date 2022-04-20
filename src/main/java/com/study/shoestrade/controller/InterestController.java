package com.study.shoestrade.controller;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.dto.interest.request.InterestProductRequestDto;
import com.study.shoestrade.dto.interest.response.InterestProductResponseDto;
import com.study.shoestrade.dto.interest.response.MyInterest;
import com.study.shoestrade.service.interest.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;
    private final ResponseService responseService;

    /**
     * 관심 상품 등록 및 수정
     * @param email : accessToken email
     * @param productId : product id
     * @param requestDto : 관심 상품으로 등록할 productSize id들
     * @return
     */
    @PostMapping("/product/{productId}/wish")
    public Result addWishList(@LoginMember String email, @PathVariable("productId") Long productId, @RequestBody InterestProductRequestDto requestDto){
        interestService.addWishList(email, productId, requestDto);
        return responseService.getSuccessResult();
    }

    /**
     * 상품 페이지에서 관심 상품 보기
     * @param email : accessToken email
     * @param productId : product id
     * @return
     */
    @GetMapping("/product/{productId}/wish")
    public SingleResult<InterestProductResponseDto> getProductWishList(@LoginMember String email, @PathVariable("productId") Long productId){
        InterestProductResponseDto responseDto = interestService.getProductWishList(email, productId);
        return responseService.getSingleResult(responseDto);
    }

    @GetMapping("/my/wish")
    public SingleResult<Page<MyInterest>> getMyWishList(@LoginMember String email, @PageableDefault(size = 10) Pageable pageable){
        Page<MyInterest> responseDto = interestService.getMyWishList(email, pageable);
        return responseService.getSingleResult(responseDto);
    }


}
