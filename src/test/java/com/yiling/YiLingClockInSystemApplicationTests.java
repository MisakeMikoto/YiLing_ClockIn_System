package com.yiling;

import com.yiling.dao.UserDao;
import com.yiling.domain.Time;
import com.yiling.domain.User;
import com.yiling.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.StampedLock;

@SpringBootTest()
class YiLingClockInSystemApplicationTests {

//    @Autowired
//    UserDao userDao;
//
//    @Autowired
//    UserService userService;
//
//    @Test
//    void contextLoads() throws ParseException {
//        Date date = new Date();
//        long time = date.getTime();
//        System.out.println(time);
//        System.out.println("---------");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String format = dateFormat.format(time);
//        System.out.println(format);
//        Date parse = dateFormat.parse(format);
//        System.out.println(parse);
//        long time1 = parse.getTime();
//        System.out.println(time1);
//
//    }
//
//    @Test
//    void TimeTest(){
//        Date date = new Date();
//        long time = date.getTime();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
//        System.out.println(simpleDateFormat.format(time));
//    }
//
//    @Test
//    void clockInTest(){
//        userService.clockIn(new User());
//    }
//
//    @Test
//    void  clockOutTest(){
//        userService.clockOut(new User());
//    }

    @Test
    void time(){
        Date date = new Date();
        Timestamp timestamp = new Timestamp(22039L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        System.out.println(simpleDateFormat.format(22036L));
    }



}
