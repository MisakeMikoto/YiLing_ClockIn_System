package com.yiling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.controller.exception.BusinessException;
import com.yiling.controller.exception.Code;
import com.yiling.dao.TimeDao;
import com.yiling.domain.Time;
import com.yiling.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/3/1 21:13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TimeServiceImpl implements TimeService {
    @Autowired
    TimeDao timeDao;
    @Override
    public List<Time> getAllTimes() {
        List<Time> times = timeDao.selectList(null);
        return times;
    }

    @Override
    public Time getTime(Time time) {
        if(time.getUsername() == null){
            throw new BusinessException(Code.GET_ERR,"参数错误");
        }
        LambdaQueryWrapper<Time> tlqw = new LambdaQueryWrapper<>();
        tlqw.eq(Time::getUsername,time.getUsername());
        Time time1 = timeDao.selectOne(tlqw);
        return time1;
    }
}
