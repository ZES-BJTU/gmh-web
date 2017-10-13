package com.zes.squad.gmh.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zes.squad.gmh.web.entity.count.po.ConsumeCountPo;
import com.zes.squad.gmh.web.entity.po.ConsumeRecordPo;

public interface ConsumeRecordMapper {

    /**
     * 插入消费记录
     * 
     * @param po
     * @return
     */
    int insert(ConsumeRecordPo po);

    /**
     * 统计消费记录
     * 
     * @param storeId
     * @param month
     * @return
     */
    List<ConsumeCountPo> sumCardCharge(@Param("storeId") Long storeId, @Param("month") Integer month);

    /**
     * 统计消费记录
     * 
     * @param storeId
     * @param month
     * @return
     */
    List<ConsumeCountPo> sumOtherCharge(@Param("storeId") Long storeId, @Param("month") Integer month);

    /**
     * 根据消费记录id查询会员
     * 
     * @param id
     * @return
     */
    Long selectMemberById(Long id);

    /**
     * 根据id删除
     * 
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     */
    ConsumeRecordPo selectById(Long id);

    /**
     * 条件查询
     * 
     * @param storeId
     * @param mobile
     * @param memberId
     * @param startTime
     * @param endTime
     * @return
     */
    List<ConsumeRecordPo> selectByCondition(@Param("storeId") Long storeId, @Param("mobile") String mobile,
                                            @Param("memberId") Long memberId, @Param("startTime") Date startTime,
                                            @Param("endTime") Date endTime);

}
