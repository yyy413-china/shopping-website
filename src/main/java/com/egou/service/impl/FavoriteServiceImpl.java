package com.egou.service.impl;

import com.egou.domain.Favorite;
import com.egou.mapper.FavoriteMapper;
import com.egou.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收藏业务实现类
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public boolean toggleFavorite(Integer userid, Integer commodityid) {
        if (userid == null || commodityid == null) {
            return false;
        }
        // 检查是否已收藏
        Favorite existing = favoriteMapper.findByUserAndCommodity(userid, commodityid);
        if (existing != null) {
            // 已收藏，取消收藏
            favoriteMapper.deleteByUserAndCommodity(userid, commodityid);
            return false;
        } else {
            // 未收藏，添加收藏
            favoriteMapper.insert(userid, commodityid);
            return true;
        }
    }

    @Override
    public boolean isFavorite(Integer userid, Integer commodityid) {
        if (userid == null || commodityid == null) {
            return false;
        }
        return favoriteMapper.findByUserAndCommodity(userid, commodityid) != null;
    }

    @Override
    public List<Favorite> findByUserId(Integer userid) {
        if (userid == null) {
            return List.of();
        }
        return favoriteMapper.findByUserId(userid);
    }

    @Override
    public int countByUserId(Integer userid) {
        if (userid == null) {
            return 0;
        }
        return favoriteMapper.countByUserId(userid);
    }
}
