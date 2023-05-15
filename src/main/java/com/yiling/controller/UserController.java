package com.yiling.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.yiling.controller.exception.BusinessException;
import com.yiling.controller.exception.Code;
import com.yiling.controller.exception.Result;
import com.yiling.controller.utils.IPUtils;
import com.yiling.dao.TimeDao;
import com.yiling.domain.IP;
import com.yiling.domain.SupplementCard;
import com.yiling.domain.Time;
import com.yiling.domain.User;
import com.yiling.service.impl.IPServiceImpl;
import com.yiling.service.impl.SupplementCardServiceImpl;
import com.yiling.service.impl.TimeServiceImpl;
import com.yiling.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * @Author MisakiMikoto
 * @Date 2023/2/26 19:38
 */
@RestController
@RequestMapping("user")
@ResponseBody
@CrossOrigin
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    IPServiceImpl ipService;

    @Autowired
    TimeServiceImpl timeService;

    @Autowired
    SupplementCardServiceImpl SCservice;

    @Value("${prop.upload-folder}")
    private String UPLOAD_FOLDER;

    @PostMapping("/login")
    public Result loginByUsername(@RequestBody User user,HttpServletRequest request){
        System.out.println(user);
        String remoteAddr = user.getIPAddress();
        if(remoteAddr == null){
            remoteAddr = IPUtils.getIP(request);
        }
        System.out.println(remoteAddr);
        remoteAddr = remoteAddr.substring(0, remoteAddr.lastIndexOf(".") + 1);
        try {
            List<IP> ips = ipService.getIps();
            for (IP ip : ips) {
                String ip_t = ip.getIp().substring(0,ip.getIp().lastIndexOf(".") + 1);
                if(remoteAddr.equals(ip_t)){
                    User user_s = userService.loginByUsername(user.getUsername(), user.getPassword());
                    if(user_s == null){
                        throw new BusinessException(Code.GET_ERR,"账号或者密码错误,请重新输入");
                    }
                    return new Result(Code.GET_OK,user_s,"success");
                }
            }
        } catch (BusinessException e) {
            return new Result(e.getCode(),false,e.getMessage());
        }
        return new Result(Code.GET_ERR,false,"你还未连接团队WIFI,请先连接团队WIFI");
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        boolean register = userService.register(user);
        if(!register){
            throw new BusinessException(Code.INSERT_ERR,"注册失败了哦，请联系管理员");
        }
        return new Result(Code.INSERT_OK,true,"注册成功，请返回登录页面");
    }

    @PostMapping("/in")
    public Result clock_in(@RequestBody User user,HttpServletRequest request){
        try {
            String remoteAddr = user.getIPAddress();
            if(remoteAddr == null){
                remoteAddr = IPUtils.getIP(request);
            }
            remoteAddr = remoteAddr.substring(0, remoteAddr.lastIndexOf(".") + 1);
            List<IP> ips = ipService.getIps();
            for (IP ip : ips) {
                String ip_t = ip.getIp().substring(0,ip.getIp().lastIndexOf(".") + 1);
                if(remoteAddr.equals(ip_t)){
                    boolean b = userService.clockIn(user);
                    return new Result(Code.UPDATE_OK,b,b ?"上卡成功":"上卡失败");
                }
            }
        } catch (BusinessException e) {
            return new Result(e.getCode(),false,e.getMessage());
        }
        return new Result(Code.UPDATE_ERR,false,"IP不匹配,请连接团队WIFI");
    }

    @PostMapping("/out")
    public Result clock_out(@RequestBody User user, HttpServletRequest request){
        try {
            String remoteAddr = user.getIPAddress();
            if(remoteAddr == null){
                remoteAddr = IPUtils.getIP(request);
            }
            remoteAddr = remoteAddr.substring(0, remoteAddr.lastIndexOf(".") + 1);
            List<IP> ips = ipService.getIps();
            for (IP ip : ips) {
                String ip_t = ip.getIp().substring(0,ip.getIp().lastIndexOf(".") + 1);
                if(remoteAddr.equals(ip_t)){
                    boolean b = userService.clockOut(user);
                    return new Result(Code.INSERT_OK,b,b ? "下卡成功":"下卡失败");
                }
            }
        } catch (BusinessException e) {
            return new Result(e.getCode(),false,e.getMessage());
        }
        return new Result(Code.UPDATE_ERR,false,"下卡失败,请连接团队WIFI");
    }

    @PostMapping("/getTimes")
    public Result getAllTme(){
        List<Time> allTimes = timeService.getAllTimes();
        if(allTimes == null){
            return new Result(Code.GET_ERR,false,"系统错误，获取时间统计失败");
        }else {
            return new Result(Code.GET_OK,allTimes,"获取时间统计成功");
        }
    }

    @PostMapping("/sendSCMsg")
    public Result receiveSupplementCardMsg(@RequestBody SupplementCard supplementCard){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isAction",false);
        boolean b = false;
        try {
            b = SCservice.acceptMsg(supplementCard);
        } catch (BusinessException e) {
            return new Result(Code.INSERT_ERR,jsonObject,"补卡信息发送失败，请重新发送");
        }
        if(!b){
            jsonObject.put("isAction",false);
            return new Result(Code.INSERT_ERR,jsonObject,"补卡信息发送失败，请重新发送");
        }
        jsonObject.put("isAction",true);
        return new Result(Code.INSERT_OK,jsonObject,"补卡信息发送成功，请等待管理员处理");
    }

    @PostMapping("/getAllSC")
    public Result getAllSCMsg(){
        List<SupplementCard> allSupplementCardMsg = SCservice.getAllSupplementCardMsg();
        if(allSupplementCardMsg == null){
            return new Result(Code.GET_ERR,false,"信息获取失败");
        }
        return new Result(Code.GET_OK,allSupplementCardMsg,"信息获取成功");
    }

    @PostMapping("getAllNoSolvedSC")
    public Result getAllNoSovleSCMsg(){
        List<SupplementCard> allSupplementCardMsg = SCservice.getAllNoSolvedSupplementCardMsg();
        if(allSupplementCardMsg == null){
            return new Result(Code.GET_ERR,false,"信息获取失败");
        }
        return new Result(Code.GET_OK,allSupplementCardMsg,"信息获取成功");
    }

    @PostMapping("/solve")
    public Result solveSCMsg(@RequestBody SupplementCard supplementCard) {
        if (supplementCard == null) {
            return new Result(Code.GET_ERR, false, "信息发送失败，请重新发送");
        }
        boolean b = SCservice.modifyTime(supplementCard);
        if (!b) {
            return new Result(Code.UPDATE_ERR, b, "系统暂时无法处理，请稍后再试");
        }
        return new Result(Code.UPDATE_OK, b, "补卡成功");
    }

    @PostMapping("/getUser")
    public Result getUser(@RequestBody User user){
        User user1 = userService.getUser(user);
        return new  Result(Code.GET_OK,user1,"获取指定用户信息成功");
    }

    @PostMapping("/getAllUser")
    public Result getAllUser(){
        List<User> allUsers = userService.getAllUsers();
        return new Result(Code.GET_OK,allUsers,"获取用户信息成功");
    }

    @PostMapping("/changeUser")
    public Result updateUser(@RequestBody User user){
        boolean b = userService.updateUser(user);
        if(b){
            return new Result(Code.UPDATE_OK,b,"更新成功");
        }
        return new Result(Code.UPDATE_ERR,b,"更新失败");
    }

    @PostMapping("/getTime")
    public Result getTime(@RequestBody Time time){
        Time time1 = timeService.getTime(time);
        if(time1 == null){
            return new Result(Code.GET_ERR,null,"获取失败");
        }
        return new Result(Code.GET_OK,time1,"获取成功");
    }

    @PostMapping("/getSCMsgByCondition")
    public Result getSCMsgByCondition(@RequestBody SupplementCard supplementCard){
        List<SupplementCard> supplementCardMsgByCondition = SCservice.getSupplementCardMsgByCondition(supplementCard);
        if(supplementCardMsgByCondition == null){
            return new Result(Code.GET_ERR,null,"获取失败");
        }
        return new Result(Code.GET_OK,supplementCardMsgByCondition,"获取成功");
    }


    @PostMapping("/upload")
    public Result upload(@RequestParam(name = "file", required = false) MultipartFile file, User user , HttpServletRequest request) {
        if (file == null) {
            return new Result(Code.BUSINESS_ERROR,false,"请选择要上传的图片");
        }
        if (file.getSize() > 1024 * 1024 * 10) {
            return new Result(Code.BUSINESS_ERROR,false,"图片过大");
        }
        //获取文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
            return new Result(Code.BUSINESS_ERROR,false,"请上传jpg,jpeg,gif,png格式的图片");
        }
        String savePath = UPLOAD_FOLDER;
        File savePathFile = new File(savePath);
        if (!savePathFile.exists()) {
            //若不存在该目录，则创建目录
            savePathFile.mkdir();
        }
        //通过UUID生成唯一文件名
        String filename = UUID.randomUUID().toString().replaceAll("-","") + "." + suffix;
        try {
            //将文件保存指定目录
            file.transferTo(new File(savePath + user.getUsername() + "." + suffix));
            user.setProfilePhotoPath("/static/img/" + user.getUsername() + "." + suffix);
            userService.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Code.BUSINESS_ERROR,false,"图片保存错误");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url","/static/img/"+user.getUsername()+"."+suffix);
        jsonObject.put("username",user.getUsername());
        //返回文件名称
        return new Result(Code.SAVE_OK,jsonObject,"保存成功");
//        return ResultUtil.success(ResultEnum.SUCCESS, filename, request);
    }

    @PostMapping("/delete")
    public Result deleteByUsername(@RequestBody User user){
        try {
            boolean b = userService.deleteByUsername(user);
            return new Result(Code.DELETE_OK,true,"删除成功");
        } catch (Exception e) {
            return new Result(Code.DELETE_ERR,false,e.getMessage());
        }
    }
}
