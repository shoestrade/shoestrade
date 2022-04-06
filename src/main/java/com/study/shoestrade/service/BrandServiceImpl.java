package com.study.shoestrade.service;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.dto.BrandDto;
import com.study.shoestrade.exception.BrandDuplicationException;
import com.study.shoestrade.exception.BrandEmptyResultDataAccessException;
import com.study.shoestrade.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        log.info("info log={}", "BrandService - saveBrand 실행");
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
        log.info("info log={}", "BrandService - updateBrand 실행");
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
        log.info("info log={}", "BrandService - deleteByBrandId 실행");
        try {
            brandRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new BrandEmptyResultDataAccessException(id.toString(), 1);
        }
    }

    /**
     * 브랜드 전체 검색
     *
     * @return 브랜드 전체 리스트
     */
    @Override
    public List<BrandDto> findBrandAll() {
        log.info("info log={}", "BrandService - findBrandAll 실행");

        return brandRepository.findAll()
                .stream()
                .map(BrandDto::create)
                .collect(Collectors.toList());
    }

    /**
     * 브랜드 이름으로 검색
     *
     * @param name 검색할 브랜드 이름
     * @return 검색된 브랜드 리스트
     */
    @Override
    public List<BrandDto> findByBrandName(String name) {
        log.info("info log={}", "BrandService - findByBrandNameList 실행");

        return brandRepository.findByNameContains(name)
                .stream()
                .map(BrandDto::create)
                .collect(Collectors.toList());
    }


    /**
     * 이름 중복 여부
     *
     * @param name 중복검사 할 브랜드 이름
     */
    private void duplicateBrandName(String name) {
        log.info("info = {}", "BrandService - duplicateBrandName 실행");

        brandRepository.findByName(name).ifPresent(
                b -> {
                    throw new BrandDuplicationException(name);
                }
        );
    }
}
