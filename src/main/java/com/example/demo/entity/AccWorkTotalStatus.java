package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.entity.enums.WorkRestId;
import com.example.demo.entity.enums.WorkStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 税务人员坐席状态总表
 * </p>
 *
 * @author heal
 * @since 2023-07-05
 */
@Getter
@Setter
@TableName("t_acc_work_total_status")
@Accessors(chain = true)
public class AccWorkTotalStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 税务人员ID
     */
    @TableId
    private String accId;

    /**
     * 状态,0=暂无状态,1=签入,-1=签出,2=示忙,-2=示闲,3=工作,-3=休息
     */
    private WorkStatus status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 版本号
     */
    private Integer accVersion;

    /**
     * 当前小休状态的id，无小休时可不传    
     */
    private WorkRestId restId;

    /**
     * 0：表示 未占线   1：占线
     */
    private Integer isBusy;

    /**
     * video：视频语音；call：电话
     */
    private String mediaType;
}
