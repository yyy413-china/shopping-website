package com.egou.mapper;

import com.egou.domain.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品评价Mapper接口
 */
@Mapper
public interface ReviewMapper {

    /**
     * 添加评价
     * @param review 评价对象
     * @return 影响行数
     */
    int insert(Review review);

    /**
     * 查询某商品的全部评价
     * @param commodityid 商品ID
     * @return 评价列表
     */
    List<Review> findByCommodityId(@Param("commodityid") Integer commodityid);

    /**
     * 查询某用户的全部评价
     * @param userid 用户ID
     * @return 评价列表
     */
    List<Review> findByUserId(@Param("userid") Integer userid);

    /**
     * 查询某用户是否已评价某商品
     * @param userid 用户ID
     * @param commodityid 商品ID
     * @return 评价记录
     */
    Review findByUserAndCommodity(@Param("userid") Integer userid, @Param("commodityid") Integer commodityid);

    /**
     * 统计某商品的平均评分
     * @param commodityid 商品ID
     * @return 平均评分
     */
    double avgRatingByCommodityId(@Param("commodityid") Integer commodityid);

    /**
     * 统计某商品的评价数量
     * @param commodityid 商品ID
     * @return 评价数量
     */
    int countByCommodityId(@Param("commodityid") Integer commodityid);

    /**
     * 查询商家所有商品的评价（联表查询）
     * @param supplierid 商家ID
     * @return 评价列表
     */
    List<Review> findBySupplierId(@Param("supplierid") Integer supplierid);
}
