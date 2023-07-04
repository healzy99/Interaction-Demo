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
     * 根据运营中心ID获取Session ID
     * @param ocId 运营中心ID
     * @return 返回SessionId
     */
    String leftPopSessionId(String ocId);

    /**
     * 写入无效会话集合
     * @param sessionId 会话ID
     */
    boolean addInvalidSession(String sessionId);

    /**
     * 删除某个失效会话
     * @param sessionId 会话ID
     */
    void removeInvalidSession(String sessionId);

    /**
     * 检查是否包含某个无效会话ID
     * @param sessionId 会话ID
     * @return 返回是否包含
     */
    boolean isMemberInvalidSession(String sessionId);

    /**
     * 添加对应运营中心空闲的坐席
     * @param ocId 运营中心ID
     * @param accountId 坐席ID
     */
    void putIfAbsentUnusedAccount(String ocId,Long accountId);

    /**
     * 获取对应运营中心的空闲坐席
     * @param ocId 运营中心ID
     * @return 返回空闲坐席的AccountId
     */
    Long getUnusedAccount(String ocId);
    /**
     * 删除对应运营中心的坐席
     * @param ocId 运营中心ID
     * @param accountId 坐席ID
     * @return 返回删除结果
     */
    boolean deleteUnusedAccount(String ocId, Long accountId);

    /**
     * 添加附属于运营中心的AccountID
     * @param ocId 运营中心ID
     * @param accountId 坐席ID
     * @return 返回是否添加成功
     */
    boolean rightPushAccountId(String ocId,Long accountId);
}
