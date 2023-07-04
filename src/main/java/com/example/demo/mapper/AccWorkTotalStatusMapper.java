package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.AccWorkTotalStatus;
import com.example.demo.entity.dto.AnswerDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 税务人员坐席状态总表 Mapper 接口
 * </p>
 *
 * @author heal
 * @since 2023-07-05
 */
public interface AccWorkTotalStatusMapper extends BaseMapper<AccWorkTotalStatus> {
    AnswerDTO getWorkStatusByAccount(@Param("id") Long accountId);
}
