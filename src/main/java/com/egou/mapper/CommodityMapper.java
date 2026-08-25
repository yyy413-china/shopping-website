package com.egou.mapper;

import com.egou.domain.Commodity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品Mapper接口
 * 商品数据的数据库交互操作
 */
@Mapper
public interface CommodityMapper {

    /**
     * 查询全部在售商品（联表查询分类名称和图片路径）
     * @return 商品列表
     */
    List<Commodity> findAll();

    /**
     * 根据分类ID查询在售商品
     * @param categoryid 分类ID
     * @return 商品列表
     */
    List<Commodity> findByCategoryId(@Param("categoryid") Integer categoryid);

    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品对象
     */
    Commodity findById(@Param("id") Integer id);

    /**
     * 根据商家ID查询商品
     * @param supplierid 商家ID
     * @return 商品列表
     */
    List<Commodity> findBySupplierId(@Param("supplierid") Integer supplierid);

    /**
     * 新增商品
     * @param commodity 商品对象
     * @return 影响行数
     */
    int insert(Commodity commodity);

    /**
     * 修改商品
     * @param commodity 商品对象
     * @return 影响行数
     */
    int update(Commodity commodity);

    /**
     * 删除商品
     * @param id 商品ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 修改商品状态（停售/在售）
     * @param id 商品ID
     * @param cstatus 商品状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Integer id, @Param("cstatus") Integer cstatus);

    /**
     * 扣减商品库存
     * @param id 商品ID
     * @param num 扣减数量
     * @return 影响行数
     */
    int reduceStock(@Param("id") Integer id, @Param("num") Integer num);

    /**
     * 根据关键词搜索商品（模糊匹配商品名称）
     * @param keyword 搜索关键词
     * @return 商品列表
     */
    List<Commodity> searchByKeyword(@Param("keyword") String keyword);
}
