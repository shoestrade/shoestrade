package com.study.shoestrade.service.brand;


import com.study.shoestrade.dto.brand.BrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandService {
    /**
     * 브랜드 등록
     *
     * @param brandDto 등록할 브랜드 정보
     * @return 등록된 브랜드 id
     */
    BrandDto saveBrand(BrandDto brandDto);

    /**
     * 브랜드 수정
     *
     * @param brandDto 수정할 브랜드 정보
     */
    void updateBrand(Long id, BrandDto brandDto);

    /**
     * 브랜드 삭제
     *
     * @param id 삭제할 브랜드 id
     */
    void deleteByBrandId(Long id);

    /**
     * 브랜드 검색
     *
     * @param name 검색할 브랜드 이름
     * @param pageable 페이지 정보
     * @return 검색된 브랜드 리스트
     */
    Page<BrandDto> findByBrandName(String name, Pageable pageable);
}

