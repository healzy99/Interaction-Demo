package com.example.demo.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Author: heal
 * @Date: 2023/7/5 16:25
 */
@Getter
public enum WorkRestId {

    /**
     * 默认下线
     */
    EMPTY(0),
    /**
     * 签入
     */
    SING_IN(1),
    /**
     * 签出
     */
    SING_OUT(-1),
    /**
     * 示忙
     */
    BUSY(2),
    /**
     * 示闲
     */
    FREE(-2),
    /**
     * 工作
     */
    BREAK_STOP(3),
    /**
     * 休息
     */
    BREAK(-3);

    @EnumValue
    private final int code;

    WorkRestId(int code) {
        this.code = code;
    }
}
