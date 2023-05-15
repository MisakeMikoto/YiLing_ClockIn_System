package com.yiling.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @Author MisakiMikoto
 * @Date 2023/2/27 20:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tbl_time")
public class Time {
    private String name;
    private String username;
    private Long id;
    @TableField(value = "clockInTime")
    private Long clockInTime;
    @TableField(value = "clockOutTime")
    private Long clockOutTime;
    @TableField(value = "clockTimeWeek")
    private Long clockTimeWeek;
    @TableField(value = "clockTimeTotal")
    private Long clockTimeTotal;
    @TableField(value = "targetTime")
    private Long targetTime;
}
