package com.yiling.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

/**
 * @Author MisakiMikoto
 * @Date 2023/3/2 15:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tbl_supplementcard")
public class SupplementCard {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    @TableField(value = "supplementCardTimeLength")
    private Long supplementCardTimeLength;
    private String reason;
    @TableField(value = "supplementCardTime")
    private Long supplementCardTime;
    @TableField(value = "solvedTime")
    private Long solvedTime;
    @TableField(value = "username")
    private String username;
    private Integer solved;
    private Integer accepted;
}
