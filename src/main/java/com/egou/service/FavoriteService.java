package com.egou.service;

import com.egou.domain.Favorite;

import java.util.List;

/**
 * 收藏业务接口
 */
public interface FavoriteService {

    /**
     * 收藏/取消收藏商品
     * @param userid 用户ID
     * @param commodityid 商品ID
     * @return true-已收藏，false-已取消收藏
     */
    boolean toggleFavorite(Integer userid, Integer commodityid);

    /**
     * 查询用户是否已收藏某商品
     * @param userid 用户ID
     * @param commodityid 商品ID
     * @return 是否已收藏
     */
    boolean isFavorite(Integer userid, Integer commodityid);

    /**
     * 查询用户的全部收藏
     * @param userid 用户ID
     * @return 收藏列表
     */
    List<Favorite> findByUserId(Integer userid);

    /**
     * 统计用户收藏数量
     * @param userid 用户ID
     * @return 收藏数量
     */
    int countByUserId(Integer userid);
}
