package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.SessionRecords;
import com.example.demo.entity.form.CallingForm;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author heal
 * @since 2023-07-01
 */
public interface ISessionRecordsService extends IService<SessionRecords> {
    void callingBus(CallingForm form);
    boolean invalidSessionList(String sessionId);

    List<String> getAllPhone();

}
