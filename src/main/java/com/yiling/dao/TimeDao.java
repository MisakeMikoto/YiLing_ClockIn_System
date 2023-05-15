package com.yiling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiling.domain.Time;
import com.yiling.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author MisakiMikoto
 * @Date 2023/2/27 21:41
 */
@Mapper
public interface TimeDao extends BaseMapper<Time> {

    @Select("select * from tbl_time where id = (select id from tbl_user where username = #{username} )")
    Time selectByUsername(User user);
}
