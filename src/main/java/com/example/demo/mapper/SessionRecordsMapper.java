package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.OperationsCenter;
import com.example.demo.entity.SessionRecords;
import com.example.demo.entity.dto.AnswerDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author heal
 * @since 2023-07-01
 */
public interface SessionRecordsMapper extends BaseMapper<SessionRecords> {
    List<OperationsCenter> queryOCByPhone(@Param("phone")String phone);
    List<OperationsCenter> queryParentOCByOCId(@Param("id")String ocId);
    AnswerDTO queryAnswerDTOByAccountId(@Param("id")Long accountId);
}
