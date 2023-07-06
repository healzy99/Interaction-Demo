package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户账号表
 * </p>
 *
 * @author heal
 * @since 2023-07-06
 */
@Getter
@Setter
@TableName("t_account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 税务人员ID
     */
    private Long accId;

    /**
     * 单位id
     */
    private Long muId;

    /**
     * 账号名
     */
    private String accCode;

    /**
     * 账号密码
     */
    private String accPwd;

    /**
     * 账号命名
     */
    private String accName;

    /**
     * 用户状态
     */
    private Long state;

    /**
     * 用户相片资源	空为没上传
     */
    private String photoRes;

    /**
     * 工号
     */
    private String empNumber;

    /**
     * 外部系统账号ID
     */
    private Long foreignUid;

    /**
     * 用户手机号码
     */
    private String mobile;

    /**
     * 工作项ID
     */
    private Long jobId;

    /**
     * 荣誉
     */
    private String honor;

    /**
     * 运营中心ID
     */
    private String ocId;

    /**
     * 座机号码
     */
    private String swrysfDm;
}
