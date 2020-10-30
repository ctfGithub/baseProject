package com.springbootbasepackage.dao;

import com.springbootbasepackage.dto.PartnerSourceDTO;
import com.springbootbasepackage.entity.PartnerSourceDO;
import com.springbootbasepackage.query.PartnerSourceQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * <pre>
 * created by nvwa gen at 2020-09-05 10:02:59.860804
 * </per>
 *
 */
@Repository
public interface PartnerSourceDAO  {

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    Integer delete(@Param("id") Long id);

    Integer insert(PartnerSourceDO var1);

    Integer update(PartnerSourceDO var1);

    List<PartnerSourceDO> query(PartnerSourceQuery var1);

    Integer queryCount(PartnerSourceQuery var1);


    /**
     * 根据主键查询
     * @param id
     * @return
     */
    PartnerSourceDO queryById(@Param("id") Long id);

    PartnerSourceDO getByPartnerId(@Param("partnerId") String partnerId);

    List<PartnerSourceDTO> testPartnerSource();

}
