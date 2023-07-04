package com.example.demo.entity.form;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: heal
 * @Date: 2023/7/3 16:49
 */
@Data
@Accessors(chain = true)
public class CallingForm {
    /**
     * 呼入号码
     */
    private String phone;
    /**
     * 会话ID
     */
    private String sessionId;
    /**
     * 运营中心ID
     */
    private String ocId;
    /**
     * 是否转移 默认不转移动
     */
    private boolean isTransfer = false;
}
