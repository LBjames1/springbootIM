package com.github.lauz.mapper;

import com.github.lauz.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @ Description   :  登录mapper
 * @ Author        :  lauz
 * @ CreateDate    :  2020/3/13 16:04
 */

@Mapper
public interface LoginMapper {
    @Select("select * from user where username=#{username}")
    User getUserByName(String username);

    @Select("select * from user where uid=#{uid}")
    User getUserById(int uid);
}
