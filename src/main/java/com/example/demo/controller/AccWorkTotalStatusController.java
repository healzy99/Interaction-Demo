package com.example.demo.controller;

import com.example.demo.entity.AccWorkTotalStatus;
import com.example.demo.service.IAccWorkTotalStatusService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
