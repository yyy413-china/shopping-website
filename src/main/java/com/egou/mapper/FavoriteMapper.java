package com.egou.mapper;

import com.egou.domain.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收藏Mapper接口
 * 收藏数据的数据库交互操作
 */
@Mapper
public interface FavoriteMapper {

    /**
     * 添加收藏
     * @param userid 用户ID
     * @param commodityid 商品ID
     * @return 影响行数
     */
    int insert(@Param("userid") Integer userid, @Param("commodityid") Integer commodityid);

    /**
     * 取消收藏
     * @param userid 用户ID
     * @param commodityid 商品ID
     * @return 影响行数
     */
    int deleteByUserAndCommodity(@Param("userid") Integer userid, @Param("commodityid") Integer commodityid);

    /**
     * 查询用户是否已收藏某商品
     * @param userid 用户ID
     * @param commodityid 商品ID
     * @return 收藏记录，未收藏返回null
     */
    Favorite findByUserAndCommodity(@Param("userid") Integer userid, @Param("commodityid") Integer commodityid);

    /**
     * 查询用户的全部收藏（联表查询商品信息）
     * @param userid 用户ID
     * @return 收藏列表
     */
    List<Favorite> findByUserId(@Param("userid") Integer userid);

    /**
     * 统计用户收藏数量
     * @param userid 用户ID
     * @return 收藏数量
     */
    int countByUserId(@Param("userid") Integer userid);
}
