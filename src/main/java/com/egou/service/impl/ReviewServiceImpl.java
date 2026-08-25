package com.egou.service.impl;

import com.egou.domain.Review;
import com.egou.mapper.ReviewMapper;
import com.egou.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品评价业务实现类
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public boolean addReview(Review review) {
        if (review == null || review.getUserid() == null || review.getCommodityid() == null) {
            return false;
        }
        // 检查是否已评价
        Review existing = reviewMapper.findByUserAndCommodity(review.getUserid(), review.getCommodityid());
        if (existing != null) {
            return false;
        }
        return reviewMapper.insert(review) > 0;
    }

    @Override
    public List<Review> findByCommodityId(Integer commodityid) {
        if (commodityid == null) {
            return List.of();
        }
        return reviewMapper.findByCommodityId(commodityid);
    }

    @Override
    public List<Review> findByUserId(Integer userid) {
        if (userid == null) {
            return List.of();
        }
        return reviewMapper.findByUserId(userid);
    }

    @Override
    public boolean hasReviewed(Integer userid, Integer commodityid) {
        if (userid == null || commodityid == null) {
            return false;
        }
        return reviewMapper.findByUserAndCommodity(userid, commodityid) != null;
    }

    @Override
    public double avgRating(Integer commodityid) {
        if (commodityid == null) {
            return 0;
        }
        return reviewMapper.avgRatingByCommodityId(commodityid);
    }

    @Override
    public int countByCommodityId(Integer commodityid) {
        if (commodityid == null) {
            return 0;
        }
        return reviewMapper.countByCommodityId(commodityid);
    }

    @Override
    public List<Review> findBySupplierId(Integer supplierid) {
        if (supplierid == null) {
            return List.of();
        }
        return reviewMapper.findBySupplierId(supplierid);
    }
}
