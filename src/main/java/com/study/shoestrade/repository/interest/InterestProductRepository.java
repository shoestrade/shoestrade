package com.study.shoestrade.repository.interest;

import com.study.shoestrade.domain.interest.InterestProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InterestProductRepository extends JpaRepository<InterestProduct, Long>, InterestProductRepositoryCustom {
}
