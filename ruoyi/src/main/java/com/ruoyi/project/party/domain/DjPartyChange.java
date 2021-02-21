package com.ruoyi.project.party.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 党组织换届对象 dj_party_change
 *
 * @author ruoyi
 * @date 2021-02-18
 */
@Data
public class DjPartyChange extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long changeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String changeUuid;

    /** 党组织名称 */
    @Excel(name = "党组织名称")
    private Long partyOrgId;

    private DjPartyOrg partyOrg;

    /** 换届时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "换届时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date changeTime;

    private String status;

}
