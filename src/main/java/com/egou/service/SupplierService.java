package com.egou.service;

import com.egou.domain.Supplier;

/**
 * 商家业务接口
 */
public interface SupplierService {

    /**
     * 根据用户ID查询商家信息
     * @param userid 用户ID
     * @return 商家对象
     */
    Supplier findByUserId(Integer userid);
}
