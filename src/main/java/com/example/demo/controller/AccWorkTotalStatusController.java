package com.example.demo.controller;

import com.example.demo.entity.AccWorkTotalStatus;
import com.example.demo.service.IAccWorkTotalStatusService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 税务人员坐席状态总表 前端控制器
 * </p>
 *
 * @author heal
 * @since 2023-07-05
 */
@RestController
@RequestMapping("/acc-work-total-status")
public class AccWorkTotalStatusController {

    @Resource
    private IAccWorkTotalStatusService accWorkTotalStatusService;

    @PostMapping("/updateWorkStatus")
    public String updateAccountWorkStatus(@RequestParam AccWorkTotalStatus workTotalStatus) {
        boolean b = accWorkTotalStatusService.saveOrUpdate(workTotalStatus);
        if (b) {
            return "Success";
        }
        return "Error";
    }

    @GetMapping("/batchSingIn")
    public String batchSingIn() {
        // 模拟随机坐席签入
        accWorkTotalStatusService.batchSingIn();
        return "Success";
    }

    @GetMapping("/batchSingUp")
    public String batchSingUn() {
        // 模拟随机坐席签入
        accWorkTotalStatusService.batchSingUp();
        return "Success";
    }

    @PostMapping("/singIn")
    public String singIn(@RequestParam Long accountId) {
        // 坐席签出
        boolean b = accWorkTotalStatusService.singIn(accountId);
        if (b) {
            return "Success";
        }
        return "Error";
    }

    @PostMapping("/singUp")
    public String singUp(@RequestParam Long accountId) {
        // 签出
        boolean b = accWorkTotalStatusService.singUp(accountId);
        if (b) {
            return "Success";
        }
        return "Error";
    }
}
