package com.study.shoestrade.controller;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.dto.interest.request.InterestProductRequestDto;
import com.study.shoestrade.dto.product.request.ProductSaveDto;
import com.study.shoestrade.dto.product.ProductImageAddDto;
import com.study.shoestrade.dto.product.request.ProductSearchDto;
import com.study.shoestrade.dto.product.response.ProductDetailDto;
import com.study.shoestrade.dto.product.response.ProductLoadDto;
import com.study.shoestrade.dto.trade.response.TradeDoneDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import com.study.shoestrade.dto.trade.response.TradeTransactionDto;
import com.study.shoestrade.service.product.ProductService;
import com.study.shoestrade.service.trade.TradeService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ResponseService responseService;
    private final ProductService productService;
    private final TradeService tradeService;

    @ApiOperation(value = "상품 등록", notes = "상품을 등록합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "상품 등록 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productDto", value = "등록할 상품 정보", dataTypeClass = ProductSaveDto.class)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result saveProduct(@RequestBody ProductSaveDto productDto) {
        productService.saveProduct(productDto);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "상품 삭제", notes = "상품을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상품 삭제 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "삭제할 상품 id", dataTypeClass = Long.class)
    })
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public Result deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "상품 검색", notes = "상품을 검색합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상품 검색 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productSearchDto", value = "검색할 상품 정보 (상품이름, 브랜드 id 리스트)", dataTypeClass = ProductSearchDto.class)
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<Page<ProductLoadDto>> findProductByNameAndBrandList(@RequestBody ProductSearchDto productSearchDto, Pageable pageable) {
        return responseService.getSingleResult(productService.findProductByNameInBrand(productSearchDto, pageable));
    }

    @ApiOperation(value = "상품 정보 수정", notes = "상품 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상품 정보 수정 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productDto", value = "수정할 상품 정보", dataTypeClass = ProductSaveDto.class),
            @ApiImplicitParam(name = "productId", value = "수정할 상품 id", dataTypeClass = Long.class)
    })
    @PostMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public Result updateProduct(@PathVariable Long productId, @RequestBody ProductSaveDto productDto) {
        productService.updateProduct(productId, productDto);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "상품 이미지 등록", notes = "상품의 이미지를 등록합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "상품 이미지 등록 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "이미지 등록할 상품 id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "productImageAddDto", value = "등록할 이미지 이름", dataTypeClass = ProductImageAddDto.class)
    })
    @PostMapping("/{productId}/images")
    @ResponseStatus(HttpStatus.CREATED)                         // 여기 수정 productImageAddDto -> list로
    public Result addImageProduct(@PathVariable Long productId, @RequestBody ProductImageAddDto productImageAddDto) {
        productService.addProductImage(productImageAddDto);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "상품 이미지 삭제", notes = "상품의 이미지를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상품 이미지 삭제 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "이미지 삭제할 상품 id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "productImageId", value = "삭제할 이미지 id", dataTypeClass = Long.class)
    })
    @DeleteMapping("/{productId}/images/{productImageId}")
    @ResponseStatus(HttpStatus.OK)
    public Result deleteProductImage(@PathVariable Long productId, @PathVariable Long productImageId) {
        productService.deleteProductImage(productImageId);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "상품 상세 검색", notes = "상품을 상세 검색합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상품 상세 검색 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "상세 검색 상품 id", dataTypeClass = Long.class),
    })
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<ProductDetailDto> findProductDetail(@PathVariable Long productId){
        return responseService.getSingleResult(productService.findProductDetailById(productId));
    }

    @ApiOperation(value = "상품의 체결 거래 내역 조회", notes = "상품의 체결 거래 내역을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상품의 체결 거래 내역 조회 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "상품 id", dataTypeClass = Long.class),
    })
    @GetMapping("/{productId}/trades/done")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<Page<TradeDoneDto>> findDoneTrade(@PathVariable("productId") Long productId, Pageable pageable) {
        return responseService.getSingleResult(tradeService.findDoneTrade(productId, pageable));
    }

    @ApiOperation(value = "상품의 입찰 내역 조회", notes = "상품의 입찰(판매, 구매) 내역을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상품의 입찰 내역 조회 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "상품 id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "tradeState", value = "입찰 타입(sell, purchase)", dataTypeClass = String.class),
    })
    @GetMapping("/{productId}/trades/{tradeState}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<Page<TradeTransactionDto>> findTransactionTrade(@PathVariable("productId") Long productId, @PathVariable("tradeState") String tradeState, Pageable pageable) {
        return responseService.getSingleResult(tradeService.findTransactionTrade(productId, tradeState, pageable));
    }

    @ApiOperation(value = "상품의 즉시 거래가 조회", notes = "상품의 즉시 거래가를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상품의 즉시 거래가 조회 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "상품 id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "tradeState", value = "입찰 타입(sell, purchase)", dataTypeClass = String.class),
    })
    @GetMapping("/{productId}/trades/{tradeState}/instant")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<List<TradeLoadDto>> findInstantTrade(@PathVariable("productId") Long productId, @PathVariable("tradeState") String tradeState) {
        return responseService.getSingleResult(tradeService.findInstantTrade(productId, tradeState));
    }
}
