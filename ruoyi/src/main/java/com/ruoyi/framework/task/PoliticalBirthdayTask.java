package com.ruoyi.framework.task;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyMemberPoliticalBirthday;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberPoliticalBirthdayMapper;
import com.ruoyi.project.party.service.IDjPartyMemberPoliticalBirthdayService;
import com.ruoyi.project.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component("politicalBirthdayTask")
public class PoliticalBirthdayTask {

    @Autowired
    private ISysConfigService configService;
    @Autowired
    private DjPartyMemberMapper memberMapper;
    @Autowired
    private DjPartyMemberPoliticalBirthdayMapper djPartyMemberPoliticalBirthdayMapper;

    public void createPoliticalBirthday() {
        Date today = new Date();
        List<DjPartyMember> memberList = memberMapper.selectPoliticalBirthdayPartyMemberList();
        memberList.stream().forEach(partyMember -> {
            DjPartyMemberPoliticalBirthday politicalBirthday = new DjPartyMemberPoliticalBirthday();
            politicalBirthday.setBirthdayUuid(UUID.randomUUID().toString());
            politicalBirthday.setMemberId(partyMember.getMemberId());
            politicalBirthday.setPartyOrgId(partyMember.getPartyOrgId());
            politicalBirthday.setPoliticalBirthday(today);
            politicalBirthday.setSendTime(today);
            politicalBirthday.setPoliticalAge((long) DateUtils.getYearReduce(partyMember.getJoinData(),today));
            politicalBirthday.setTheme("党员"+partyMember.getMemberName()+"政治生日");
            String content = configService.selectConfigByKey("politicalBirthday.content");
            politicalBirthday.setContent(content);
            politicalBirthday.setCreateBy("1");
            politicalBirthday.setCreateTime(today);
            djPartyMemberPoliticalBirthdayMapper.insertDjPartyMemberPoliticalBirthday(politicalBirthday);
        });


    }
}
