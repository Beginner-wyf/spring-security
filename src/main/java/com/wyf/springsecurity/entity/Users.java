package com.wyf.springsecurity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wangyifan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("users")
public class Users implements Serializable {

    @TableId("id")
    private Integer id;

    @TableField("username")
    private String userName;

    @TableField("password")
    private String passWord;

}
