package com.yiling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yiling.controller.exception.BusinessException;
import com.yiling.controller.exception.Code;
import com.yiling.controller.exception.SystemException;
import com.yiling.dao.SupplementCardDao;
import com.yiling.dao.TimeDao;
import com.yiling.dao.UserDao;
import com.yiling.domain.SupplementCard;
import com.yiling.domain.Time;
import com.yiling.domain.User;
import com.yiling.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/2/26 19:28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private SupplementCardDao supplementCardDao;

    @Override
    public User loginByUsername(String username, String pwd) {
        try {
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(null != username,User::getUsername,username);
            lqw.eq(null != pwd,User::getPassword,pwd);
            return userDao.selectOne(lqw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean register(User user) {
        System.out.println(user);
        try {
            int insert = userDao.insert(user);
            if(insert == 1)
                return true;
            else
                throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean clockIn(User user_c) {
        Time time = null;
        User user = null;
        LambdaUpdateWrapper<Time> tluw = new LambdaUpdateWrapper<>();
        LambdaUpdateWrapper<User> luw = new LambdaUpdateWrapper<>();
        try {
            if(user_c.getId() != null) {
                time = timeDao.selectById(user_c.getId());
                user = userDao.selectById(user_c.getId());
            }else if(user_c.getUsername() != null) {
                time = timeDao.selectByUsername(user_c);
                user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername,user_c.getUsername()));
            }else if(user_c.getName() != null){
                time = timeDao.selectOne(new LambdaQueryWrapper<Time>().eq(Time::getName,user_c.getName()));
                user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getName,user_c.getName()));
            }else{
                throw new BusinessException(Code.GET_ERR,"参数错误，上卡失败");
            }
                if (user == null || user.getClocked() == 1) {
                    if(user == null){
                        throw new BusinessException(Code.GET_ERR,"无法查询到您的信息，上卡失败");
                    }else if(user.getClocked() == 1) {
                        throw new BusinessException(Code.GET_ERR,"你还没有下卡呢，请先下卡吧");
                    }
                }
        } catch (BusinessException e) {
            throw e;
        }
        if(time != null){
            tluw.eq(Time::getId,time.getId());
        }
        luw.eq(null != user,User::getId,user.getId());
        Date date = new Date();
        try {
            if(time == null){
                time = new Time();
                User user1 = new User();
                time.setName(user.getName());
                time.setId(user.getId());
                time.setClockInTime(date.getTime());
                time.setUsername(user.getUsername());
                time.setTargetTime(user.getTargetTime());
                int insert = timeDao.insert(time);
                user1.setClocked(1);
                userDao.update(user1,luw);
                if(insert != 1){
                    throw new SystemException(Code.SYSTEM_UNKNOW_ERROR,"系统错误，请联系管理员");
                }
                return insert == 1;
            }else{
                User user1 = new User();
                user1.setClocked(1);
                time.setClockInTime(date.getTime());
                int update = timeDao.update(time, tluw);
                userDao.update(user1,luw);
                if(update != 1){
                    throw new SystemException(Code.SYSTEM_UNKNOW_ERROR,"系统错误，请联系管理员");
                }
                return update == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean clockOut(User user_c) {
        Time time = null;
        User user = null;
        LambdaUpdateWrapper<Time> tluw = new LambdaUpdateWrapper<>();
        LambdaUpdateWrapper<User> luw = new LambdaUpdateWrapper<>();
        try {
            if(user_c.getId() != null) {
                time = timeDao.selectById(user_c.getId());
                user = userDao.selectById(user_c.getId());
            }else if(user_c.getUsername() != null) {
                time = timeDao.selectByUsername(user_c);
                user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername,user_c.getUsername()));
            }else if(user_c.getName() != null){
                time = timeDao.selectOne(new LambdaQueryWrapper<Time>().eq(Time::getName,user_c.getName()));
                user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getName,user_c.getName()));
            }else{
                throw new BusinessException(Code.GET_ERR,"参数错误，下卡失败");
            }

            if (user == null || user.getClocked() == 0) {
                if(user == null){
                    throw new BusinessException(Code.GET_ERR,"无法查询到您的信息，下卡失败");
                }else {
                    throw new BusinessException(Code.GET_ERR,"你还没有上卡呢，请先上卡吧");
                }
            }
        } catch (BusinessException e) {
            throw  e;
        }
        Date date = new Date();
        luw.eq(null != user,User::getId,user.getId());
        tluw.eq(null != time,Time::getId,time.getId());
        try {
            long time1 = date.getTime();
            User user1 = new User();
            user1.setClocked(0);
            time.setClockOutTime(time1);
            time.setName(null);
            time.setClockTimeWeek(time.getClockTimeWeek()+(time1 - time.getClockInTime()));
            time.setClockTimeTotal(time.getClockTimeTotal()+(time1 - time.getClockInTime()));
            int update = timeDao.update(time, tluw);
            System.out.println(update);
            userDao.update(user1,luw);
            if(update != 1){
                throw new SystemException(Code.SYSTEM_UNKNOW_ERROR,"系统错误，请联系管理员");
            }
            return update == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User getUser(User user) {
        if(user.getUsername() == null){
            throw new BusinessException(Code.BUSINESS_ERROR,null,"参数错误，请传递正确的参数");
        }
        LambdaQueryWrapper<User> ulqw = new LambdaQueryWrapper<>();
        ulqw.eq(User::getUsername,user.getUsername());
        User user1 = userDao.selectOne(ulqw);
        if(user1 != null){
            return user1;
        }
        throw new BusinessException(Code.BUSINESS_ERROR,null,"您要查找的用户不在数据库中");
    }

    @Override
    public boolean updateUser(User user) {
        if(user.getUsername() == null){
            throw new BusinessException(Code.BUSINESS_ERROR,null,"参数错误，请传递正确的参数");
        }
        LambdaUpdateWrapper<User> uluw = new LambdaUpdateWrapper<>();
        uluw.eq(User::getUsername,user.getUsername());
        int update = userDao.update(user, uluw);
        if(user.getTargetTime() != null){
            LambdaUpdateWrapper<Time> tluw = new LambdaUpdateWrapper<>();
            tluw.eq(Time::getUsername,user.getUsername());
            Time time = new Time();
            time.setUsername(user.getUsername());
            time.setTargetTime(user.getTargetTime());
            int update1 = timeDao.update(time, tluw);
            if(update1 == 1 && update == 1){
                return true;
            }
        }
        if(update == 1){
            return true;
        }
        throw new SystemException(Code.SYSTEM_UNKNOW_ERROR,"更新用户信息失败，请联系管理员");
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userDao.selectList(null);
        return users;
    }

    @Override
    public boolean deleteByUsername(User user) {
        if(user.getUsername() == null){
            throw new BusinessException(Code.DELETE_ERR,"空参错误");
        }
        try {
            LambdaQueryWrapper<User> ulqw = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<Time> tlqw = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<SupplementCard> sclqw = new LambdaQueryWrapper<>();
            ulqw.eq(user.getUsername() != null,User::getUsername,user.getUsername());
            tlqw.eq(user.getUsername() != null,Time::getUsername,user.getUsername());
            sclqw.eq(user.getUsername() != null,SupplementCard::getUsername,user.getUsername());
            supplementCardDao.delete(sclqw);
            timeDao.delete(tlqw);
            userDao.delete(ulqw);
        } catch (Exception e) {
            throw new SystemException(Code.DELETE_ERR,"删除失败");
        }
        return true;

    }
}
