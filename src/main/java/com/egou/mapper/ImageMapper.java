package com.egou.mapper;

import com.egou.domain.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品图片Mapper接口
 * 商品图片数据的数据库交互操作
 */
@Mapper
public interface ImageMapper {

    /**
     * 根据商品ID查询图片
     * @param commodityid 商品ID
     * @return 图片对象
     */
    Image findByCommodityId(@Param("commodityid") Integer commodityid);

    /**
     * 新增图片
     * @param image 图片对象
     * @return 影响行数
     */
    int insert(Image image);

    /**
     * 根据商品ID删除图片
     * @param commodityid 商品ID
     * @return 影响行数
     */
    int deleteByCommodityId(@Param("commodityid") Integer commodityid);

    /**
     * 根据商品ID更新图片
     * @param image 图片对象
     * @return 影响行数
     */
    int updateByCommodityId(Image image);
}
