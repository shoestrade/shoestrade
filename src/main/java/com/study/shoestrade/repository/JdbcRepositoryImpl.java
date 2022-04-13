package com.study.shoestrade.repository;

import com.study.shoestrade.domain.product.ProductImage;
import com.study.shoestrade.domain.product.ProductSize;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcRepositoryImpl implements JdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    private int batchSize = 50;

    /**
     * 상품 사이즈 저장
     * @param items 저장할 사이즈
     */
    @Override
    public void saveAllSize(List<ProductSize> items) {
        int batchCount = 0;
        List<ProductSize> subItems = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            subItems.add(items.get(i));
            if ((i + 1) % batchSize == 0) {
                batchCount = batchInsertSize(batchSize, batchCount, subItems);
            }
        }
        if (!subItems.isEmpty()) {
            batchCount = batchInsertSize(batchSize, batchCount, subItems);
        }
    }

    /**
     * 상품 이미지 저장
     * @param images 저장할 이미지
     */
    @Override
    public void saveAllImage(List<ProductImage> images) {
        int batchCount = 0;
        List<ProductImage> subImages = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            subImages.add(images.get(i));
            if ((i + 1) % batchSize == 0) {
                batchCount = batchInsertImage(batchSize, batchCount, subImages);
            }
        }
        if (!subImages.isEmpty()) {
            batchCount = batchInsertImage(batchSize, batchCount, subImages);
        }
    }

    private int batchInsertImage(int batchSize, int batchCount, List<ProductImage> subImages) {
        jdbcTemplate.batchUpdate("insert into product_image (`name`, `product_id`) values (?,?)"
                , new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, subImages.get(i).getName());
                        ps.setLong(2, subImages.get(i).getProduct().getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return subImages.size();
                    }
                });
        subImages.clear();
        batchCount++;
        return batchCount;
    }

    private int batchInsertSize(int batchSize, int batchCount, List<ProductSize> subItems) {
        jdbcTemplate.batchUpdate("insert into product_size (`size`, `product_id`) values (?,?)"
                , new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, subItems.get(i).getSize());
                        ps.setLong(2, subItems.get(i).getProduct().getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return subItems.size();
                    }
                });
        subItems.clear();
        batchCount++;
        return batchCount;
    }

}
