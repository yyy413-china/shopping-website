package com.egou.service.impl;

import com.egou.domain.Supplier;
import com.egou.mapper.SupplierMapper;
import com.egou.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商家业务实现类
 */
@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public Supplier findByUserId(Integer userid) {
        if (userid == null) {
            return null;
        }
        return supplierMapper.findByUserId(userid);
    }
}
