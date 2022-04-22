package com.study.shoestrade.repository.interest;

import com.study.shoestrade.domain.interest.InterestProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface InterestProductRepository extends JpaRepository<InterestProduct, Long>, InterestProductRepositoryCustom {

    @Query("select i from InterestProduct i join i.member m join i.productSize p where m.email = :email and p.product.id = :productId and i.id = :interestId")
    Optional<InterestProduct> findOneInterest(@Param("email") String email, @Param("productId") Long productId, @Param("interestId") Long interestId);
}
