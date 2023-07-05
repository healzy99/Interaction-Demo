package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
