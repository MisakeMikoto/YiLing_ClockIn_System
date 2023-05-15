package com.yiling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiling.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author MisakiMikoto
 * @Date 2023/2/26 19:24
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

}
