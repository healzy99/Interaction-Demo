package com.example.demo.service;

import com.example.demo.entity.SessionRecords;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author heal
 * @since 2023-07-01
 */
public interface ISessionRecordsService extends IService<SessionRecords> {
    boolean invalidSessionList(String sessionId);

}
