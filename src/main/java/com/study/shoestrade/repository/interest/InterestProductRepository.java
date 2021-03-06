package com.study.shoestrade.repository.interest;

import com.study.shoestrade.domain.interest.InterestProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface InterestProductRepository extends JpaRepository<InterestProduct, Long>, InterestProductRepositoryCustom {

    @Query("select i from InterestProduct i join i.member m join fetch i.productSize p join fetch p.product pp where m.email = :email and i.id = :interestId")
    Optional<InterestProduct> findOneInterest(@Param("email") String email, @Param("interestId") Long interestId);
}
