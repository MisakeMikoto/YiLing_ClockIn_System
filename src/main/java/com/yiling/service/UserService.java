package com.yiling.service;

import com.yiling.domain.User;

import java.util.List;


/**
 * @Author MisakiMikoto
 * @Date 2023/2/26 19:24
 */

public interface UserService {
    //1.登录
    User loginByUsername(String acct, String pwd);
    //2.注册
    boolean register(User user);
    //3.上卡
    boolean clockIn(User user);
    //4.下卡
    boolean clockOut(User user);
    //5.补卡
    /*
    * 1. 前端发送补卡申请 (真实姓名 (name) 补卡时常 (long) 补卡原因(String reason))
    * 数据表结构 name varchar(255) supplementCardTimeLength bigint reason varchar(255) supplementCardTime bigint Solved int accepted int
    * 2. 后端读取JSON封装到supplementCard类中(name),存入对应的表中
    * 3. 后台读取全部补卡表，
    *
    *
    *
    * */
    User getUser(User user);

    boolean updateUser(User user);

    List<User> getAllUsers();

    boolean deleteByUsername(User user);

}
