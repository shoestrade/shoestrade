package com.study.shoestrade.repository.interest;

import com.study.shoestrade.domain.interest.InterestProduct;
import com.study.shoestrade.dto.interest.response.MyInterest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InterestProductRepositoryCustom {

    List<InterestProduct> findPreInterests(String email, Long productId);

    Page<MyInterest> findMyInterests(String email, Pageable pageable);

    List<MyInterest> findMemberInterests(Long id);
}
