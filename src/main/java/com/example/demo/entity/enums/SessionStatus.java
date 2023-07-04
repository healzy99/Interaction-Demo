package com.example.demo.entity.enums;

import lombok.Getter;

/**
 * @Author: heal
 * @Date: 2023/7/5 11:50
 */
@Getter
public enum SessionStatus {
    /**
     * 正常
     */
    NORMAL,
    /**
     * 已接听
     */
    ANSWERED,
    /**
     * 挂断
     */
    HANG_UP,
    /**
     * 异常
     */
    ABNORMAL,
}
