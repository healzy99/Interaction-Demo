package com.example.demo.service;

import com.example.demo.entity.AccWorkTotalStatus;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 税务人员坐席状态总表 服务类
 * </p>
 *
 * @author heal
 * @since 2023-07-05
 */
public interface IAccWorkTotalStatusService extends IService<AccWorkTotalStatus> {
    void batchSingIn();
    void singIn(Long accountId);
    void singUp(Long accountId);
}
