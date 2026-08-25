package com.egou.service.impl;

import com.egou.domain.Commodity;
import com.egou.domain.Image;
import com.egou.mapper.CommodityMapper;
import com.egou.mapper.ImageMapper;
import com.egou.service.CommodityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品业务实现类
 * 处理商品增删改查、图片联动、库存扣减等业务逻辑
 */
@Service
public class CommodityServiceImpl implements CommodityService {

    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private ImageMapper imageMapper;

    /**
     * 分页查询全部在售商品
     */
    @Override
    public PageInfo<Commodity> findAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Commodity> list = commodityMapper.findAll();
        return new PageInfo<>(list);
    }

    /**
     * 按分类分页查询在售商品
     */
    @Override
    public PageInfo<Commodity> findByCategoryId(Integer categoryid, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Commodity> list = commodityMapper.findByCategoryId(categoryid);
        return new PageInfo<>(list);
    }

    /**
     * 根据ID查询商品
     */
    @Override
    public Commodity findById(Integer id) {
        if (id == null) {
            return null;
        }
        return commodityMapper.findById(id);
    }

    /**
     * 根据商家ID查询商品
     */
    @Override
    public List<Commodity> findBySupplierId(Integer supplierid) {
        if (supplierid == null) {
            return null;
        }
        return commodityMapper.findBySupplierId(supplierid);
    }

    /**
     * 新增商品（同时新增图片，双表联动）
     * 使用Spring声明式事务保证数据一致性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCommodity(Commodity commodity, String imagePath, String imageName) {
        if (commodity == null) {
            return false;
        }
        // 设置默认状态为在售
        if (commodity.getCstatus() == null) {
            commodity.setCstatus(1);
        }
        // 新增商品
        int result = commodityMapper.insert(commodity);
        if (result > 0 && imagePath != null) {
            // 新增商品图片（一对一联动）
            Image image = new Image();
            image.setCommodityid(commodity.getId());
            image.setIname(imageName);
            image.setIpath(imagePath);
            imageMapper.insert(image);
        }
        return result > 0;
    }

    /**
     * 修改商品
     */
    @Override
    public boolean updateCommodity(Commodity commodity) {
        if (commodity == null || commodity.getId() == null) {
            return false;
        }
        return commodityMapper.update(commodity) > 0;
    }

    /**
     * 删除商品（同时删除关联图片）
     * 使用Spring声明式事务保证数据一致性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCommodity(Integer id) {
        if (id == null) {
            return false;
        }
        // 先删除关联图片
        imageMapper.deleteByCommodityId(id);
        // 再删除商品
        return commodityMapper.deleteById(id) > 0;
    }

    /**
     * 修改商品状态（停售/在售）
     */
    @Override
    public boolean updateStatus(Integer id, Integer cstatus) {
        if (id == null || cstatus == null) {
            return false;
        }
        return commodityMapper.updateStatus(id, cstatus) > 0;
    }

    /**
     * 扣减商品库存
     */
    @Override
    public boolean reduceStock(Integer id, Integer num) {
        if (id == null || num == null || num <= 0) {
            return false;
        }
        return commodityMapper.reduceStock(id, num) > 0;
    }

    /**
     * 根据关键词搜索商品（模糊匹配商品名称）
     */
    @Override
    public PageInfo<Commodity> searchByKeyword(String keyword, int pageNum, int pageSize) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll(pageNum, pageSize);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Commodity> list = commodityMapper.searchByKeyword(keyword.trim());
        return new PageInfo<>(list);
    }
}
