package com.egou.service;

import com.egou.domain.Review;

import java.util.List;

/**
 * 商品评价业务接口
 */
public interface ReviewService {

    /**
     * 添加评价
     * @param review 评价对象
     * @return 是否成功
     */
    boolean addReview(Review review);

    /**
     * 查询某商品的全部评价
     * @param commodityid 商品ID
     * @return 评价列表
     */
    List<Review> findByCommodityId(Integer commodityid);

    /**
     * 查询某用户的全部评价
     * @param userid 用户ID
     * @return 评价列表
     */
    List<Review> findByUserId(Integer userid);

    /**
     * 查询某用户是否已评价某商品
     * @param userid 用户ID
     * @param commodityid 商品ID
     * @return 是否已评价
     */
    boolean hasReviewed(Integer userid, Integer commodityid);

    /**
     * 统计某商品的平均评分
     * @param commodityid 商品ID
     * @return 平均评分
     */
    double avgRating(Integer commodityid);

    /**
     * 统计某商品的评价数量
     * @param commodityid 商品ID
     * @return 评价数量
     */
    int countByCommodityId(Integer commodityid);

    /**
     * 查询商家所有商品的评价
     * @param supplierid 商家ID
     * @return 评价列表
     */
    List<Review> findBySupplierId(Integer supplierid);
}
