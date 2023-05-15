package com.yiling.jobs;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.controller.exception.BusinessException;
import com.yiling.controller.exception.Code;
import com.yiling.dao.TimeDao;
import com.yiling.domain.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author MisakiMikoto
 * @Date 2023/2/28 21:31
 */
@Component
public class WeekUpdateClockTime {

    @Autowired
    TimeDao timeDao;

    @Scheduled(cron = "59 59 23 ? * 7")
    private void weekUpdate(){
        System.out.println("weedupdate");
        Time time = new Time();
        time.setClockTimeWeek(0L);
        try {
            timeDao.update(time,null);
        } catch (Exception e) {
            throw new BusinessException(Code.UPDATE_ERR,"时间更新错误");
        }

    }

//    @Scheduled(cron = "0/2 * * * * ?")
//    private void Test(){
//        Date date = new Date();
//        System.out.println(new Timestamp(date.getTime()));
//    }
}
