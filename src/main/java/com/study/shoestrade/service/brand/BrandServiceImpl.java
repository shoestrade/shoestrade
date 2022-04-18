package com.study.shoestrade.service.brand;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.dto.brand.BrandDto;
import com.study.shoestrade.exception.brand.BrandDuplicationException;
import com.study.shoestrade.exception.brand.BrandEmptyResultDataAccessException;
import com.study.shoestrade.repository.brand.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    /**
     * 브랜드 등록
     *
     * @return 등록된 브랜드 id
     */
    @Override
    @Transactional
    public BrandDto saveBrand(BrandDto brandDto) {
        duplicateBrandEngName(brandDto);
        return BrandDto.create(brandRepository.save(
                Brand.builder().korName(brandDto.getKorName()).engName(brandDto.getEngName()).build())
        );
    }

    /**
     * 브랜드 수정
     *
     * @param brandDto 수정할 브랜드
     */
    @Override
    @Transactional
    public void updateBrand(BrandDto brandDto) {

        Brand findBrand = brandRepository.findById(brandDto.getId()).orElseThrow(
                () -> new BrandEmptyResultDataAccessException(brandDto.getId().toString(), 1)
        );

        if(!brandDto.getEngName().equals(findBrand.getEngName())){
            duplicateBrandEngName(brandDto);
        }


        if(!brandDto.getKorName().equals(findBrand.getKorName())){
            duplicateBrandKorName(brandDto);
        }

        findBrand.changeBrandName(brandDto.getKorName(), brandDto.getEngName());
    }

    /**
     * 브랜드 삭제
     *
     * @param id 삭제할 브랜드 id
     */
    @Override
    @Transactional
    public void deleteByBrandId(Long id) {
        try {
            brandRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new BrandEmptyResultDataAccessException(id.toString(), 1);
        }
    }

    /**
     * 브랜드 검색
     *
     * @param name 검색할 브랜드 이름
     * @return 검색된 브랜드 리스트
     */
    @Override
    public Page<BrandDto> findByBrandName(String name, Pageable pageable) {
        return brandRepository.findByNameContains(name, pageable)
                .map(BrandDto::create);
    }


    /**
     * 이름 중복 여부
     *
     */
    private void duplicateBrandEngName(BrandDto brandDto) {
        brandRepository.findByEngName(brandDto.getEngName()).ifPresent(
                b -> {
                    throw new BrandDuplicationException(brandDto.getEngName());
                }
        );
    }

    private void duplicateBrandKorName(BrandDto brandDto) {
        brandRepository.findByKorName(brandDto.getKorName()).ifPresent(
                b -> {
                    throw new BrandDuplicationException(brandDto.getKorName());
                }
        );
    }
}
