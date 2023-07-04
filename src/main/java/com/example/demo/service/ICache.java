package com.example.demo.service;

/**
 * @Author: heal
 * @Date: 2023/7/4 10:17
 */
public interface ICache {
    /**
     * 添加附属于运营中心的SessionID
     * @param ocId 运营中心ID
     * @param sessionId 会话ID
     * @return 返回是否添加成功
     */
    boolean rightPushSessionId(String ocId,String sessionId);

    /**
     * 添加附属于运营中心的AccountID
     * @param ocId 运营中心ID
     * @param accountId 坐席ID
     * @return 返回是否添加成功
     */
    boolean rightPushAccountId(String ocId,String accountId);
}
