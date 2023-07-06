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
        if (b) return "Success";
        return "Error";
    }

    @GetMapping("/batchSingIn")
    public void batchSingIn(){
        // 模拟随机坐席签入
        accWorkTotalStatusService.batchSingIn();
    }

    @PostMapping("/singIn")
    public void singIn(@RequestParam Long accountId){
        // 坐席签出
        accWorkTotalStatusService.singIn(accountId);
    }

    @PostMapping("/singUp")
    public void singUp(@RequestParam Long accountId){
        // 签出
        accWorkTotalStatusService.singUp(accountId);
    }
}
