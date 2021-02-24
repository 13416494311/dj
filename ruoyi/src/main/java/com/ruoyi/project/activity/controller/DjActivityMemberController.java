package com.ruoyi.project.activity.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.server.WebSocketServer;
import com.ruoyi.project.activity.domain.DjActivityDetailed;
import com.ruoyi.project.activity.service.IDjActivityDetailedService;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.activity.domain.DjActivityMember;
import com.ruoyi.project.activity.service.IDjActivityMemberService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 活动参与人Controller
 *
 * @author ruoyi
 * @date 2020-08-16
 */
@RestController
@RequestMapping("/activity/member")
public class DjActivityMemberController extends BaseController
{
    @Autowired
    private IDjActivityMemberService djActivityMemberService;
    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private IDjActivityDetailedService djActivityDetailedService;

    /**
     * 查询活动参与人列表 签到大屏
     */

    @GetMapping("/listForScreen")
    public AjaxResult listForScreen(DjActivityMember djActivityMember)
    {
        Map<String , Object> map = new HashMap<String , Object>();
        List<DjActivityMember> activityMemberList = djActivityMemberService.selectDjActivityMemberList(djActivityMember);
        map.put("activityMemberList",activityMemberList);

        final int[] memberArriveNum = {0};
        final int[] memberNoArriveNum = { 0 };
        activityMemberList.stream().forEach(member->{
            switch (member.getStatus()){
                case "1" :
                    memberNoArriveNum[0] += 1;
                    break;
                case "2" :
                    memberArriveNum[0] += 1;
                    break;
                case "3" :
                    memberArriveNum[0] += 1;
                    break;
                case "4" :
                    memberArriveNum[0] += 1;
                    break;
                case "5" :
                    memberNoArriveNum[0] += 1;
                    break;
                case "6" :
                    memberNoArriveNum[0] += 1;
                    break;
                default:break;
            }
        });
        map.put("planCount",String.valueOf(activityMemberList.size()) );
        map.put("arriveCount",String.valueOf(memberArriveNum[0]) );
        map.put("noArriveCount",String.valueOf(memberNoArriveNum[0]));

        return AjaxResult.success(map);
    }


    @GetMapping(value = "/signIn/{detailedUuid}")
    public AjaxResult signIn(@PathVariable("detailedUuid") String detailedUuid) throws Exception {

        AjaxResult result = AjaxResult.success(djActivityMemberService.signIn(detailedUuid));

        ConcurrentHashMap<String, WebSocketServer> groupHashMap = webSocketServer.getWebSocketuGroup(detailedUuid);
        if(groupHashMap!=null && groupHashMap.size() >0){
            webSocketServer.sendMessageToGroup(detailedUuid, JSON.toJSONString(result));
        }
        return result;
    }



    /**
     * 查询活动参与人列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DjActivityMember djActivityMember)
    {
        startPage();
        List<DjActivityMember> list = djActivityMemberService.selectDjActivityMemberList(djActivityMember);
        return getDataTable(list);
    }

    /**
     * 导出活动参与人列表
     */

    @Log(title = "活动参与人", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DjActivityMember djActivityMember)
    {
        List<DjActivityMember> list = djActivityMemberService.selectDjActivityMemberList(djActivityMember);
        ExcelUtil<DjActivityMember> util = new ExcelUtil<DjActivityMember>(DjActivityMember.class);
        return util.exportExcel(list, "member");
    }

    /**
     * 获取活动参与人详细信息
     */

    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId)
    {
        return AjaxResult.success(djActivityMemberService.selectDjActivityMemberById(memberId));
    }

    /**
     * 新增活动参与人
     */

    @Log(title = "活动参与人", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DjActivityMember djActivityMember)
    {
        return toAjax(djActivityMemberService.insertDjActivityMember(djActivityMember));
    }

    /**
     * 新增活动参与人
     */

    @Log(title = "活动参与人", businessType = BusinessType.INSERT)
    @PostMapping("/addByArrange")
    public AjaxResult addByArrange(String planUuid,String detailedUuid,String partyOrgId,String partyMemberIds,String type)
    {
        for(String partyMemberId:partyMemberIds.split(",")){
            DjActivityMember activityMember = new DjActivityMember();
            activityMember.setPlanUuid(planUuid);
            activityMember.setDetailedUuid(detailedUuid);
            if(StringUtils.isNotNull(partyOrgId)){
                activityMember.setPartyOrgId(Long.parseLong(partyOrgId));
            }
            activityMember.setPartyMemberId(Long.parseLong(partyMemberId));
            activityMember.setType(type);
            activityMember.setStatus("1");
            djActivityMemberService.insertDjActivityMember(activityMember);
        }
        return AjaxResult.success();
    }
    /**
     * 修改活动参与人
     */

    @Log(title = "活动参与人", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DjActivityMember djActivityMember)
    {
        return toAjax(djActivityMemberService.updateDjActivityMember(djActivityMember));
    }

    @Log(title = "活动参与人", businessType = BusinessType.UPDATE)
    @PostMapping("/updateMembers")
    public AjaxResult updateMembers(String memberIds,String status)
    {
        for(String memberId:memberIds.split(",")){
            DjActivityMember activityMember = new DjActivityMember();
            activityMember.setMemberId(Long.parseLong(memberId));
            activityMember.setStatus(status);
            djActivityMemberService.updateDjActivityMember(activityMember);
        }
        return AjaxResult.success();
    }

    /**
     * 删除活动参与人
     */

    @Log(title = "活动参与人", businessType = BusinessType.DELETE)
	@DeleteMapping("/{memberIds}")
    public AjaxResult remove(@PathVariable Long[] memberIds)
    {
        return toAjax(djActivityMemberService.deleteDjActivityMemberByIds(memberIds));
    }
}
