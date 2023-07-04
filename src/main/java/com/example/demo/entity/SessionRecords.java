package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.entity.enums.SessionHandleState;
import com.example.demo.entity.enums.SessionStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author heal
 * @since 2023-07-01
 */
@Getter
@Setter
@TableName("t_session_records")
@Accessors(chain = true)
public class SessionRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会话唯一ID
     */
    @TableId
    private String sessionId;

    /**
     * 运营中心ID
     */
    private String ocId;

    /**
     * 主叫号码
     */
    private String caller;

    /**
     * 被叫号码
     */
    private String called;

    /**
     * 对接坐席
     */
    private Long account;

    /**
     * 转移坐席，多个坐席以逗号(,)分隔
     */
    private String transferAccount;

    /**
     * 0正常  1已接听  2挂断  3异常
     */
    private SessionStatus status;

    /**
     * 证件号码
     */
    private String zjhm;

    /**
     * 纳税人识别号（企业识别号）
     */
    private String nsrsbh;

    /**
     * 纳税人名称（企业名称）
     */
    private String nsrmc;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 处理结果	0已完成	1未完成	
     */
    private SessionHandleState handleState;

    /**
     * 备注
     */
    private String remark;

    /**
     * 录音记录
     */
    private String voiceUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
