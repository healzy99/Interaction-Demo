package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.AccWorkTotalStatus;
import com.example.demo.entity.Account;
import com.example.demo.entity.enums.WorkRestId;
import com.example.demo.entity.enums.WorkStatus;
import com.example.demo.mapper.AccWorkTotalStatusMapper;
import com.example.demo.service.IAccWorkTotalStatusService;
import com.example.demo.service.IAccountService;
import com.example.demo.service.ICache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 税务人员坐席状态总表 服务实现类
 * </p>
 *
 * @author heal
 * @since 2023-07-05
 */
@Service
@Slf4j
@Transactional
public class AccWorkTotalStatusServiceImpl extends ServiceImpl<AccWorkTotalStatusMapper, AccWorkTotalStatus> implements IAccWorkTotalStatusService {
    @Resource
    private ICache cache;

    @Resource
    private IAccountService accountService;

    @Override
    public void batchSingIn() {
        // 查询所有坐席
        List<Account> accounts = accountService.list();
        // 根据坐席批量更改状态
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(RandomUtils.nextInt(0, accounts.size()));
//            AccWorkTotalStatus one = getOne(new QueryWrapper<AccWorkTotalStatus>().eq("ACC_ID", account.getAccId()));
//            if (one == null){
//                one = new AccWorkTotalStatus().setAccId(account.getAccId().toString()).setAccVersion(1).setId(UUID.randomUUID().toString()).setIsBusy(0).setCreateTime(LocalDateTime.now());
//            }
//            one.setStatus(WorkStatus.SING_IN).setRestId(WorkRestId.SING_IN).setUpdateTime(LocalDateTime.now());
//            boolean b = saveOrUpdate(one);
//            if (!b) log.error("坐席签入失败---Account: {}", one);
//            else {
            cache.putIfAbsentUnusedAccount(account.getOcId(), account.getAccId());
            log.info("坐席签入成功----OCID: {},Account Id: {}", account.getOcId(), account.getAccId());
//            }
            accounts.remove(account);
        }
    }

    @Override
    public void batchSingUp() {
        // 查询所有坐席
        List<Account> accounts = accountService.list();
        // 根据坐席批量更改状态
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(RandomUtils.nextInt(0, accounts.size()));
            cache.deleteUnusedAccount("Unused:" + account.getOcId(), account.getAccId());
            log.info("坐席签出成功----OCID: {},Account Id: {}", account.getOcId(), account.getAccId());
            accounts.remove(account);
        }
    }

    @Override
    public boolean singIn(Long accountId) {
        Account account = accountService.getOne(new QueryWrapper<Account>().eq("ACC_ID", accountId));
        AccWorkTotalStatus accWorkTotalStatus = new AccWorkTotalStatus()
                .setAccId(accountId.toString())
                .setAccVersion(1)
                .setId(UUID.randomUUID().toString())
                .setIsBusy(0)
                .setCreateTime(LocalDateTime.now())
                .setStatus(WorkStatus.SING_IN)
                .setRestId(WorkRestId.SING_IN)
                .setUpdateTime(LocalDateTime.now());
        boolean b = saveOrUpdate(accWorkTotalStatus);
        if (!b) {
            log.error("坐席签入失败---Account: {}", accountId);
            return false;
        } else {
            cache.putIfAbsentUnusedAccount(account.getOcId(), account.getAccId());
            log.info("坐席签入成功----OCID: {},Account Id: {},Status: {},RestId: {}", account.getOcId(), account.getAccId(), accWorkTotalStatus.getStatus(), accWorkTotalStatus.getRestId());
            return true;
        }
    }

    @Override
    public boolean singUp(Long accountId) {
        Account account = accountService.getOne(new QueryWrapper<Account>().eq("ACC_ID", accountId));
        AccWorkTotalStatus accWorkTotalStatus = new AccWorkTotalStatus()
                .setAccId(accountId.toString())
                .setAccVersion(1)
                .setId(UUID.randomUUID().toString())
                .setIsBusy(0)
                .setCreateTime(LocalDateTime.now())
                .setStatus(WorkStatus.EMPTY)
                .setRestId(WorkRestId.EMPTY)
                .setUpdateTime(LocalDateTime.now());
        boolean b = saveOrUpdate(accWorkTotalStatus);
        if (!b) {
            log.error("坐席签出失败---Account: {}", accountId);
            return false;
        } else {
            cache.deleteUnusedAccount(account.getOcId(), accountId);
            log.info("坐席签出成功----OCID: {},Account Id: {},Status: {},RestId: {}", account.getOcId(), account.getAccId(), accWorkTotalStatus.getStatus(), accWorkTotalStatus.getRestId());
            return true;
        }
    }
}
