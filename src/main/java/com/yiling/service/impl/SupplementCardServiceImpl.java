package com.yiling.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.controller.exception.BusinessException;
import com.yiling.controller.exception.Code;
import com.yiling.controller.exception.SystemException;
import com.yiling.dao.SupplementCardDao;
import com.yiling.dao.TimeDao;
import com.yiling.dao.UserDao;
import com.yiling.domain.Time;
import com.yiling.domain.SupplementCard;
import com.yiling.domain.User;
import com.yiling.service.SupplementCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/3/2 15:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SupplementCardServiceImpl implements SupplementCardService {
    @Autowired
    SupplementCardDao supplementCardDao;

    @Autowired
    TimeDao timeDao;

    @Autowired
    UserDao userDao;

    @Override
    public boolean acceptMsg(SupplementCard supplementCard) {
        int insert;
        Date date = new Date();
        LambdaQueryWrapper<SupplementCard> sclqw = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> ulqw = new LambdaQueryWrapper<User>();
        LambdaUpdateWrapper<User> uluw = new LambdaUpdateWrapper<>();
        if(supplementCard == null || supplementCard.getUsername() == null
                || supplementCard.getSupplementCardTimeLength() == null){
            throw new BusinessException(Code.BUSINESS_ERROR,null,"消息接收错误，请重新发送补卡请求");
        }
        uluw.eq(User::getUsername,supplementCard.getUsername());
        ulqw.eq(User::getUsername,supplementCard.getUsername());
        sclqw.eq(SupplementCard::getUsername,supplementCard.getUsername());
        sclqw.eq(SupplementCard::getSolved,0);
        try {
            User user = userDao.selectOne(ulqw);
            List<SupplementCard> supplementCards = supplementCardDao.selectList(sclqw);
            if(supplementCards.size() >= 1){
                throw new BusinessException(Code.BUSINESS_ERROR,null,"您还有至少一个补卡请求未被处理，请等待管理员处理");
            }
            supplementCard.setSupplementCardTime(date.getTime());
            supplementCard.setAccepted(1);
            supplementCard.setSolved(0);
            supplementCard.setSolvedTime(0L);
            supplementCard.setName(user.getName());
            supplementCard.setSupplementCardTimeLength(supplementCard.getSupplementCardTimeLength() * 60 * 1000);
            insert = supplementCardDao.insert(supplementCard);
            if(insert == 1){
                user.setIsAction(false);
                userDao.update(user,uluw);
                return true;
            }else{
                return false;
            }
        } catch (BusinessException e) {
            throw e;
        }
    }

    @Override
    public List<SupplementCard> getAllSupplementCardMsg() {
        LambdaQueryWrapper<SupplementCard> sclqw = new LambdaQueryWrapper<>();
        sclqw.eq(SupplementCard::getAccepted,1);
        return supplementCardDao.selectList(sclqw);
    }

    @Override
    public List<SupplementCard> getAllNoSolvedSupplementCardMsg() {
        LambdaQueryWrapper<SupplementCard> sclqw = new LambdaQueryWrapper<>();
        sclqw.eq(SupplementCard::getAccepted,1);
        sclqw.eq(SupplementCard::getSolved,0);
        return supplementCardDao.selectList(sclqw);
    }

    @Override
    public List<SupplementCard> getSupplementCardMsgByCondition(SupplementCard supplementCard) {
        LambdaQueryWrapper<SupplementCard> sclqw = new LambdaQueryWrapper<>();
        sclqw.eq(supplementCard.getUsername() != null,SupplementCard::getUsername,supplementCard.getUsername());
        sclqw.eq(supplementCard.getSolved() != null,SupplementCard::getSolved,supplementCard.getSolved());
        sclqw.eq(supplementCard.getName() != null,SupplementCard::getName,supplementCard.getName());
        sclqw.eq(supplementCard.getId() != null,SupplementCard::getId,supplementCard.getId());
        List<SupplementCard> supplementCards = supplementCardDao.selectList(sclqw);
        return supplementCards;
    }

    @Override
    public boolean modifyTime(SupplementCard supplementCard) {
        Date date = new Date();
        int update;
        int update1;
        LambdaQueryWrapper<Time> tlqw = new LambdaQueryWrapper<>();
        LambdaUpdateWrapper<SupplementCard> scluw = new LambdaUpdateWrapper<>();
        LambdaUpdateWrapper<Time> tluw = new LambdaUpdateWrapper<>();
        LambdaQueryWrapper<SupplementCard> sclqw = new LambdaQueryWrapper<>();
        LambdaUpdateWrapper<User> uluw = new LambdaUpdateWrapper<>();
        try {
            if(supplementCard == null
//                    || supplementCard.getName() == null
//                    || supplementCard.getSupplementCardTimeLength() == null
            ){
                throw new BusinessException(Code.BUSINESS_ERROR,null,"消息接收错误，请重新发送补卡请求");
            }
            sclqw.eq(SupplementCard::getId,supplementCard.getId());
            supplementCard = supplementCardDao.selectOne(sclqw);
            if(supplementCard.getSolved() == 1){
                throw new BusinessException(Code.SYSTEM_UNKNOW_ERROR,"该补卡请求已经被处理了，请勿重复处理");
            }
            String name = supplementCard.getName();
            tlqw.eq(name != null,Time::getName,name);
            Time time = timeDao.selectOne(tlqw);
            time.setClockTimeWeek(time.getClockTimeWeek() + supplementCard.getSupplementCardTimeLength());
            time.setClockTimeTotal(time.getClockTimeTotal() + supplementCard.getSupplementCardTimeLength());
            supplementCard.setSolvedTime(date.getTime());
            supplementCard.setSolved(1);
            SupplementCard supplementCard1 = supplementCardDao.selectOne(sclqw);
            tluw.eq(Time::getName,name);
            scluw.eq(SupplementCard::getId,supplementCard1.getId());
            update = timeDao.update(time, tluw);
            update1 = supplementCardDao.update(supplementCard, scluw);
            if(update == 1 && update1 == 1){
                User user = new User();
                user.setIsAction(true);
                uluw.eq(User::getUsername,supplementCard.getUsername());
                int update2 = userDao.update(user, uluw);
                if(update2 == 1){
                    return true;
                }else{
                    return false;
                }
            }else{
                throw new SystemException(Code.SYSTEM_UNKNOW_ERROR,"数据库更新错误，请联系管理员");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
