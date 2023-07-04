package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author heal
 * @since 2023-07-03
 */
@Getter
@Setter
@TableName("t_operations_center")
public class OperationsCenter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 运营中心主键id
     */
    @TableId
    private String ocId;

    /**
     * 运营中心名称
     */
    private String ocName;

    /**
     * 父节点id
     */
    private String parentId;

    /**
     * 备注
     */
    private Long rank;

    /**
     * 管制单位ID
     */
    private String muId;

    /**
     * 是否为运营中心
     */
    private Long isOc;

    /**
     * 运营中心所在层级
     */
    private Long ocLevel;

    /**
     * 排序id
     */
    private Long displayOrder;
}
