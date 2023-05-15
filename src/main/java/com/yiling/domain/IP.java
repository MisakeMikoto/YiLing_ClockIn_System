package com.yiling.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MisakiMikoto
 * @Date 2023/2/28 19:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tbl_ip")
public class IP {
    @TableId(type = IdType.AUTO)
    private String id;
    private String ip;
    private String name;
}
