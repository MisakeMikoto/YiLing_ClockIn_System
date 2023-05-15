package com.yiling.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

/**
 * @Author MisakiMikoto
 * @Date 2023/2/26 19:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tbl_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String name;
    private String password;
    private Integer clocked;
    @TableField(value = "isAction")
    private Boolean isAction;
    @TableField(value = "targetTime")
    private Long targetTime;
    @TableField(value = "profilePhotoPath")
    private String profilePhotoPath;
    @TableField(exist = false)
    @JsonProperty(value = "iPAddress")
    private String iPAddress;
}
