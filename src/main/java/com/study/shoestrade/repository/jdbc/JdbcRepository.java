package com.study.shoestrade.repository.jdbc;

import com.study.shoestrade.domain.interest.InterestProduct;
import com.study.shoestrade.domain.product.ProductImage;
import com.study.shoestrade.domain.product.ProductSize;

import java.util.List;

public interface JdbcRepository {
   /**
    * 상품 사이즈 저장
    * @param items 저장할 사이즈
    */
   void saveAllSize(List<ProductSize> items);

   /**
    * 상품 이미지 저장
    * @param images 저장할 이미지
    */
   void saveAllImage(List<ProductImage> images);

   /**
    * 관심 상품 저장
    * @param interests : 저장할 관심 상품
    */
   void saveAllInterest(List<InterestProduct> interests);
}
