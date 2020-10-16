package com.ruoyi.project.activity.domain;

import com.ruoyi.framework.web.domain.BaseEntity;
import lombok.Data;

@Data
public class DjActivityParams extends BaseEntity {

    private String activityTheme;

    private String partyOrgId;

    private String state ;

    private String delFlag;

}
