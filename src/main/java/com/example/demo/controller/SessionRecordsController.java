package com.example.demo.controller;

import com.example.demo.entity.form.CallingForm;
import com.example.demo.service.ISessionRecordsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author heal
 * @since 2023-07-01
 */
@Slf4j
@RestController
@RequestMapping("/session-records")
public class SessionRecordsController {
    @Resource
    private ISessionRecordsService sessionRecordsService;

    @PostMapping("/invalidSession")
    public String invalidSession(@RequestParam String sessionId) {
        boolean b = sessionRecordsService.invalidSessionList(sessionId);
        if (b) return "Success";
        return "Error";
    }

    @GetMapping("/calling")
    @Async("interaction-thread")
    public void calling(){
        List<String> allPhone = sessionRecordsService.getAllPhone();
        for (int i = 0; i < 10; i++) {
            String phone = allPhone.get(RandomUtils.nextInt(0, allPhone.size()));
            CallingForm form = new CallingForm().setSessionId(UUID.randomUUID().toString()).setPhone(phone);
            sessionRecordsService.callingBus(form);
        }
    }

    @PostMapping("callOne")
    public String callOne(@RequestParam String phone){
        // 广州运营中心测试
        CallingForm form = new CallingForm().setSessionId(UUID.randomUUID().toString()).setPhone(phone);
        sessionRecordsService.callingBus(form);
        return form.getSessionId();
    }
}
