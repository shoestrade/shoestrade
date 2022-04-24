package com.study.shoestrade.repository.product;

import com.study.shoestrade.domain.product.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long>, ProductSizeRepositoryCustom {

    @Query("select p from ProductSize p  where p.product.id = :product_id and p.id in :lists")
    List<ProductSize> findProductSizes(@Param("product_id") Long product_id, @Param("lists") List<Long> lists);

    List<ProductSize> findByProduct_Id(Long productId);


/*
    @Query("select new com.study.shoestrade.dto.interest.repository.InterestRepoDto(p.id, i.id) " +
            "from ProductSize p " +
            "left outer join InterestProduct i " +
            "on i.productSize = p " +
            "where i.member.email = :email and p.product.id = :productId")
    List<InterestRepoDto> findDto(@Param("email") String email, @Param("productId") Long productId);*/
}
