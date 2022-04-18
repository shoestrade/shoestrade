package com.study.shoestrade.service.brand;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.dto.brand.BrandDto;
import com.study.shoestrade.exception.brand.BrandDuplicationException;
import com.study.shoestrade.exception.brand.BrandEmptyResultDataAccessException;
import com.study.shoestrade.repository.brand.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    /**
     * 브랜드 등록
     *
     * @param name 등록할 브랜드 이름
     * @return 등록된 브랜드 id
     */
    @Override
    @Transactional
    public BrandDto saveBrand(String name){
        duplicateBrandName(name);
        return BrandDto.create(brandRepository.save(Brand.builder().name(name).build()));
    }

    /**
     * 브랜드 수정
     *
     * @param brandDto 수정할 브랜드
     */
    @Override
    @Transactional
    public void updateBrand(BrandDto brandDto) {
        duplicateBrandName(brandDto.getName());

        Brand findBrand = brandRepository.findById(brandDto.getId()).orElseThrow(
                () -> new BrandEmptyResultDataAccessException(brandDto.getId().toString(), 1)
        );
        findBrand.changeBrandName(brandDto.getName());
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
     * @param name 중복검사 할 브랜드 이름
     */
    private void duplicateBrandName(String name) {
        brandRepository.findByName(name).ifPresent(
                b -> {
                    throw new BrandDuplicationException(name);
                }
        );
    }
}
