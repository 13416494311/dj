package com.ruoyi.project.party.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.party.domain.DjOrgAssessment;
import com.ruoyi.project.party.domain.DjPartyMember;
import com.ruoyi.project.party.domain.DjPartyOrg;
import com.ruoyi.project.party.mapper.DjOrgAssessmentMapper;
import com.ruoyi.project.party.mapper.DjPartyMemberMapper;
import com.ruoyi.project.party.mapper.DjPartyOrgMapper;
import com.ruoyi.project.sys.domain.DjSysMessage;
import com.ruoyi.project.sys.domain.DjSysTodo;
import com.ruoyi.project.sys.service.IDjSysMessageService;
import com.ruoyi.project.sys.service.IDjSysTodoService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.system.service.ISysDictDataService;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjOrgAssessmentyearMapper;
import com.ruoyi.project.party.domain.DjOrgAssessmentyear;
import com.ruoyi.project.party.service.IDjOrgAssessmentyearService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 党组织考核年Service业务层处理
 *
 * @author admin
 * @date 2021-03-10
 */
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class DjOrgAssessmentyearServiceImpl implements IDjOrgAssessmentyearService
{
    @Autowired
    private DjOrgAssessmentyearMapper djOrgAssessmentyearMapper;

    @Autowired
    private DjOrgAssessmentMapper assessmentMapper;
    @Autowired
    private DjPartyOrgMapper djPartyOrgMapper;
    @Autowired
    private DjPartyMemberMapper djPartyMemberMapper;
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

    /**
     * 查询党组织考核年
     *
     * @param id 党组织考核年ID
     * @return 党组织考核年
     */
    @Override
    public DjOrgAssessmentyear selectDjOrgAssessmentyearById(Long id)
    {
        return djOrgAssessmentyearMapper.selectDjOrgAssessmentyearById(id);
    }

    /**
     * 查询党组织考核年
     *
     * @param assessmentyearUuid 党组织考核年UUID
     * @return 党组织考核年
     */
    @Override
    public DjOrgAssessmentyear selectDjOrgAssessmentyearByUuid(String assessmentyearUuid){
        return djOrgAssessmentyearMapper.selectDjOrgAssessmentyearByUuid(assessmentyearUuid);
    }

    /**
     * 查询党组织考核年列表
     *
     * @param djOrgAssessmentyear 党组织考核年
     * @return 党组织考核年
     */
    @Override
    public List<DjOrgAssessmentyear> selectDjOrgAssessmentyearList(DjOrgAssessmentyear djOrgAssessmentyear)
    {
        return djOrgAssessmentyearMapper.selectDjOrgAssessmentyearList(djOrgAssessmentyear);
    }

    /**
     * 新增党组织考核年
     *
     * @param djOrgAssessmentyear 党组织考核年
     * @return 结果
     */
    @Override
    public int insertDjOrgAssessmentyear(DjOrgAssessmentyear djOrgAssessmentyear)
    {
        djOrgAssessmentyear.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djOrgAssessmentyear.setCreateTime(DateUtils.getNowDate());
        int result = djOrgAssessmentyearMapper.insertDjOrgAssessmentyear(djOrgAssessmentyear);
        publishYearAssessment(djOrgAssessmentyear);
        return result;
    }


    //年度考核计划发布
    private void publishYearAssessment(DjOrgAssessmentyear djOrgAssessmentyear){
        if("2".equals(djOrgAssessmentyear.getOrgAssessmentStatus())){
            DjOrgAssessment djOrgAssessment =new DjOrgAssessment();
            djOrgAssessment.setAssessmentyearUuid(djOrgAssessmentyear.getAssessmentyearUuid());
            List<DjOrgAssessment> assessmentList = assessmentMapper.selectDjOrgAssessmentList(djOrgAssessment);

            AtomicBoolean flag = new AtomicBoolean(false);
            assessmentList.stream().forEach(assessment -> {
                DjPartyOrg djPartyOrg = djPartyOrgMapper.selectDjPartyOrgById(assessment.getPartyOrgId());
                assessment.setDjPartyOrg(djPartyOrg);
                assessment.setOrgAssessmentStatus("1");
                assessmentMapper.updateDjOrgAssessment(assessment);
                createAssessmentTodo(assessment,djOrgAssessmentyear.getAssessmentName()+"("+djPartyOrg.getPartyOrgName()+")");

                if("Y".equals(djPartyOrg.getPerformanceAppraisal())){
                    flag.set(true);
                }
            });

            if(flag.get()){
                createPerformanceAppraisalTodo(djOrgAssessmentyear);
            }


        }
    }

    private void createPerformanceAppraisalTodo(DjOrgAssessmentyear djOrgAssessmentyear){
        SysUser user = null;
        List<SysUser> userList = userService.selectUserByRoleId(16L);
        if( !CollectionUtils.isEmpty(userList)){
            user = userList.get(0);
        }
        if(user == null ){
            return ;
        }

        DjSysTodo sysTodo = new DjSysTodo();
        //sysTodo.setUuid(djPartyOrg.getPartyOrgUuid());
        sysTodo.setType("16"); //项目绩效考核
        sysTodo.setTitle(djOrgAssessmentyear.getAssessmentName());
        sysTodo.setUrlName("PerformanceAppraisal");
        sysTodo.setUrlPath("todo/performanceAppraisal");
        sysTodo.setUserId(user.getUserId());
        sysTodo.setStatus("0");
        Map<String, String> map = new HashMap<String, String>();
        map.put("assessmentyearId", String.valueOf(djOrgAssessmentyear.getId()));
        sysTodo.setUrlParams(JSON.toJSONString(map));
        sysTodo.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
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
        sysMessage.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        sysMessageService.insertDjSysMessage(sysMessage);
    }

    private void createAssessmentTodo(DjOrgAssessment djOrgAssessment,String title){
        DjPartyMember djPartyMember = new DjPartyMember();
        djPartyMember.setPartyOrgId(djOrgAssessment.getPartyOrgId());
        djPartyMember.setPartyPositionType("1");
        List<DjPartyMember> partyMemberList = djPartyMemberMapper.selectPartyMemberList(djPartyMember);
        if(partyMemberList ==null || partyMemberList.size() ==0){ //没有书记 就给副书记
            djPartyMember.setPartyPositionType("2");
            partyMemberList = djPartyMemberMapper.selectPartyMemberList(djPartyMember);
            if(partyMemberList ==null || partyMemberList.size() ==0){
                return ;
            }else{
                djPartyMember =   partyMemberList.get(0);
            }
        }else{
            djPartyMember =   partyMemberList.get(0);
        }

        SysUser user = userMapper.selectUserByPartyMemberId(djPartyMember.getMemberId());

        if(user == null ){
            return ;
        }
        DjSysTodo sysTodo = new DjSysTodo();
        //sysTodo.setUuid(djPartyOrg.getPartyOrgUuid());
        sysTodo.setType("14"); //双项考评
        sysTodo.setTitle(title);
        sysTodo.setUrlName("OrgAssessment");
        sysTodo.setUrlPath("todo/orgAssessment");
        sysTodo.setUserId(user.getUserId());
        sysTodo.setStatus("0");
        Map<String, String> map = new HashMap<String, String>();
        map.put("assessmentId", String.valueOf(djOrgAssessment.getId()));
        map.put("orgAssessmentStatus", djOrgAssessment.getOrgAssessmentStatus() );
        sysTodo.setUrlParams(JSON.toJSONString(map));
        sysTodo.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
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
        sysMessage.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        sysMessageService.insertDjSysMessage(sysMessage);
    }

    /**
     * 修改党组织考核年
     *
     * @param djOrgAssessmentyear 党组织考核年
     * @return 结果
     */
    @Override
    public int updateDjOrgAssessmentyear(DjOrgAssessmentyear djOrgAssessmentyear)
    {
        djOrgAssessmentyear.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserId().toString());
        djOrgAssessmentyear.setUpdateTime(DateUtils.getNowDate());
        int result = djOrgAssessmentyearMapper.updateDjOrgAssessmentyear(djOrgAssessmentyear);
        publishYearAssessment(djOrgAssessmentyear);
        return result;
    }

    /**
     * 批量删除党组织考核年
     *
     * @param ids 需要删除的党组织考核年ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentyearByIds(Long[] ids)
    {
        return djOrgAssessmentyearMapper.deleteDjOrgAssessmentyearByIds(ids);
    }

    /**
     * 删除党组织考核年信息
     *
     * @param id 党组织考核年ID
     * @return 结果
     */
    @Override
    public int deleteDjOrgAssessmentyearById(Long id)
    {
        return djOrgAssessmentyearMapper.deleteDjOrgAssessmentyearById(id);
    }
}
