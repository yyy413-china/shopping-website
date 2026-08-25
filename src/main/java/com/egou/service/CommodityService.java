package com.egou.service;

import com.egou.domain.Commodity;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 商品业务接口
 */
public interface CommodityService {

    /**
     * 分页查询全部在售商品
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页信息
     */
    PageInfo<Commodity> findAll(int pageNum, int pageSize);

    /**
     * 按分类分页查询在售商品
     * @param categoryid 分类ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页信息
     */
    PageInfo<Commodity> findByCategoryId(Integer categoryid, int pageNum, int pageSize);

    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品对象
     */
    Commodity findById(Integer id);

    /**
     * 根据商家ID查询商品
     * @param supplierid 商家ID
     * @return 商品列表
     */
    List<Commodity> findBySupplierId(Integer supplierid);

    /**
     * 新增商品（同时新增图片，双表联动）
     * @param commodity 商品对象
     * @param imagePath 图片路径
     * @param imageName 图片名称
     * @return 是否成功
     */
    boolean addCommodity(Commodity commodity, String imagePath, String imageName);

    /**
     * 修改商品
     * @param commodity 商品对象
     * @return 是否成功
     */
    boolean updateCommodity(Commodity commodity);

    /**
     * 删除商品（同时删除关联图片）
     * @param id 商品ID
     * @return 是否成功
     */
    boolean deleteCommodity(Integer id);

    /**
     * 修改商品状态（停售/在售）
     * @param id 商品ID
     * @param cstatus 状态
     * @return 是否成功
     */
    boolean updateStatus(Integer id, Integer cstatus);

    /**
     * 扣减商品库存
     * @param id 商品ID
     * @param num 扣减数量
     * @return 是否成功
     */
    boolean reduceStock(Integer id, Integer num);

    /**
     * 根据关键词搜索商品（模糊匹配商品名称）
     * @param keyword 搜索关键词
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页信息
     */
    PageInfo<Commodity> searchByKeyword(String keyword, int pageNum, int pageSize);
}
