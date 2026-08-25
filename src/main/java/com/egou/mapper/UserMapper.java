package com.egou.mapper;

import com.egou.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 * 用户数据的数据库交互操作
 */
@Mapper
public interface UserMapper {

    /**
     * 根据账号查询用户
     * @param account 账号
     * @return 用户对象
     */
    User findByAccount(@Param("account") String account);

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    User findById(@Param("id") Integer id);

    /**
     * 新增用户（注册）
     * @param user 用户对象
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户在线状态
     * @param id 用户ID
     * @param onlineyes 在线状态：0-离线，1-在线
     * @return 影响行数
     */
    int updateOnlineStatus(@Param("id") Integer id, @Param("onlineyes") Integer onlineyes);

    /**
     * 更新用户最后登录时间
     * @param id 用户ID
     * @return 影响行数
     */
    int updateLoginTime(@Param("id") Integer id);

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 影响行数
     */
    int update(User user);
}
