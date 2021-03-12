package com.ruoyi.framework.task;

import com.alibaba.fastjson.JSON;
import com.ruoyi.project.party.domain.DjPartyChange;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyOrg;
import com.ruoyi.project.party.mapper.DjPartyChangeMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.mapper.DjPartyOrgMapper;
import com.ruoyi.project.sys.domain.DjSysMessage;
import com.ruoyi.project.sys.domain.DjSysTodo;
import com.ruoyi.project.sys.service.IDjSysMessageService;
import com.ruoyi.project.sys.service.IDjSysTodoService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.system.service.ISysDictDataService;
import com.ruoyi.project.system.service.ISysRoleService;
import com.ruoyi.project.system.service.ISysUserService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author:万登枫
 * @date: 2021/3/6
 */
@Component("partyChangeTask")
public class PartyChangeTask {

    @Autowired
    private DjPartyOrgMapper partyOrgMapper ;
    @Autowired
    private DjPartyChangeMapper partyChangeMapper ;
    @Autowired
    private DjPartyMemberMapper partyMemberMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private IDjSysTodoService djSysTodoService;
    @Autowired
    private IDjSysMessageService sysMessageService;
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private ISysUserService userService;

    public void createPartyChangeTodo(String daysString) {
        String[] days =  daysString.split("/");
        List<DjPartyOrg> djPartyOrgList = partyOrgMapper.selectDjPartyOrgList(null);
        djPartyOrgList.stream().forEach(djPartyOrg -> {
            DjPartyChange djPartyChange = new DjPartyChange();
            djPartyChange.setPartyOrgId(djPartyOrg.getPartyOrgId());
            List<DjPartyChange> djPartyChangeList = partyChangeMapper.selectDjPartyChangeList(djPartyChange);
            if(djPartyChangeList!=null && djPartyChangeList.size()>0){
                DjPartyChange partyChange = djPartyChangeList.get(0);
                if("1".equals(djPartyOrg.getOrgType())){  //党委 5年一换
                    for(String day : days){
                        if(remind(partyChange.getChangeTime(),5, Long.parseLong(day))){

                            String partyOrgFullName ="";
                            String[] djPartyOrgIds =djPartyOrg.getAncestors().split(",");
                            for(String djPartyOrgId:djPartyOrgIds){
                                if(!"0".equals(djPartyOrgId)){
                                    partyOrgFullName+=partyOrgMapper.selectDjPartyOrgById(Long.parseLong(djPartyOrgId)).getPartyOrgName()+"/";
                                }
                            }
                            djPartyOrg.setPartyOrgFullName(partyOrgFullName+djPartyOrg.getPartyOrgName());

                            createTodo(djPartyOrg, Long.parseLong(day),partyChange.getChangeId());
                        }
                    }

                }else if("2".equals(djPartyOrg.getOrgType()) || "3".equals(djPartyOrg.getOrgType())){    //党支 3年一换
                    for(String day : days){
                        if(remind(partyChange.getChangeTime(),3, Long.parseLong(day))){

                            String partyOrgFullName ="";
                            String[] djPartyOrgIds =djPartyOrg.getAncestors().split(",");
                            for(String djPartyOrgId:djPartyOrgIds){
                                if(!"0".equals(djPartyOrgId)){
                                    partyOrgFullName+=partyOrgMapper.selectDjPartyOrgById(Long.parseLong(djPartyOrgId)).getPartyOrgName()+"/";
                                }
                            }
                            djPartyOrg.setPartyOrgFullName(partyOrgFullName+djPartyOrg.getPartyOrgName());

                            createTodo(djPartyOrg,Long.parseLong(day),partyChange.getChangeId());
                        }
                    }
                }
            }
        });
    }

    private void createTodo(DjPartyOrg djPartyOrg,long day,Long lastPartyChangeId){


        SysUser user = null;
        if("1".equals(djPartyOrg.getOrgType())){
            List<SysUser> userList = userService.selectUserByRoleId(13L);
            if( !CollectionUtils.isEmpty(userList)){
                user = userList.get(0);
            }
        }else{
            DjPartyMember djPartyMember = new DjPartyMember();
            djPartyMember.setPartyOrgId(djPartyOrg.getPartyOrgId());
            djPartyMember.setPartyPositionType("1");
            List<DjPartyMember> partyMemberList = partyMemberMapper.selectDjPartyMemberList(djPartyMember);
            if(partyMemberList ==null || partyMemberList.size() ==0){ //没有书记 就给副书记
                djPartyMember.setPartyPositionType("2");
                partyMemberList = partyMemberMapper.selectDjPartyMemberList(djPartyMember);
                if(partyMemberList ==null || partyMemberList.size() ==0){
                    return ;
                }else{
                    djPartyMember =   partyMemberList.get(0);
                }
            }else{
                djPartyMember =   partyMemberList.get(0);
            }

            user = userMapper.selectUserByPartyMemberId(djPartyMember.getMemberId());
        }
        if(user == null ){
            return ;
        }
        DjSysTodo sysTodo = new DjSysTodo();
        sysTodo.setUuid(djPartyOrg.getPartyOrgUuid());
        sysTodo.setType("9"); //换届提醒
        sysTodo.setTitle(djPartyOrg.getPartyOrgFullName()+" 党组织换届,还有 "+day+" 天,请知悉!");
        sysTodo.setUrlName("PartyChangeRemind");
        sysTodo.setUrlPath("todo/partyChangeRemind");
        sysTodo.setUserId(user.getUserId());
        sysTodo.setStatus("2");
        Map<String, String> map = new HashMap<String, String>();
        map.put("partyOrgId", String.valueOf(djPartyOrg.getPartyOrgId()));
        map.put("lastPartyChangeId", String.valueOf(lastPartyChangeId));
        sysTodo.setUrlParams(JSON.toJSONString(map));
        sysTodo.setCreateBy("1");
        djSysTodoService.insertDjSysTodo(sysTodo);

        DjSysMessage sysMessage = new DjSysMessage();
        sysMessage.setMessageUuid(sysTodo.getUuid());
        sysMessage.setTitle(dictDataService.selectDictLabel("sys_todo_type",sysTodo.getType()));
        sysMessage.setContent("您收到一条"+sysMessage.getTitle()+"，内容如下："+sysTodo.getTitle());
        sysMessage.setType(2);
        sysMessage.setPlatform(0);
        sysMessage.setGroupName("");
        sysMessage.setStatus("0");
        sysMessage.setUserIds(sysTodo.getUserId().toString());
        sysMessage.setCreateBy("1");
        sysMessageService.insertDjSysMessage(sysMessage);
    }

    @SneakyThrows
    private boolean remind(Date date, long year , long day)  {
        long time = date.getTime();
        year = year * 365 * 24 * 60 * 60 * 1000;
        time = time + year;
        day = day * 24 * 60 * 60 * 1000;
        time = time - day;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowdayTime = dateFormat.format(new Date());
        Date nowDate = dateFormat.parse(nowdayTime);

        return  nowDate.compareTo(new Date(time)) == 0;
    }

}

